package Test;


    public class BST<T> {
    	BSTNode<T> root, current;
    	
    	public BST() {
    		root = current = null;
    	}
    	
    	public boolean empty() {
    		return root == null;
    	}
    	
    	public boolean full() {
    		return false;
    	}
    	
    	public T retrieve () {
    		// it must not be a null
    		return current.data;
    	}


    	public boolean findkey(int tkey) {
    		BSTNode<T> p = root, q = root;
    				
    		if(empty())
    			return false;
    		
    		while(p != null) {
    			q = p;
    			if(p.key == tkey) {
    				current = p;
    				return true;
    			}
    			else if(tkey < p.key)
    				p = p.left;
    			else
    				p = p.right;
    		}
    		
    		current = q;
    		return false;
    	}


    	public boolean insert(int k, T val) {
    		BSTNode<T> p, q = current;
    		
    		if(findkey(k)) {
    			current = q;  
    			return false; 
    		}
    		
    		p = new BSTNode<T>(k, val);
    		if (empty()) {
    			root = current = p;
    			return true;
    		}
    		else {
    			if (k < current.key)
    				current.left = p;
    			else
    				current.right = p;
    			current = p;
    			return true;
    		}
    	}
    	
    	public boolean remove_key(int k){
    	    BooleanWrapper flag = new BooleanWrapper(false);
    	    root = remove_aux(root, k, flag);
    	    current = root;
    	    return flag.get();
    	}

    	private BSTNode<T> remove_aux(BSTNode<T> node, int k, BooleanWrapper flag){
    	    if(node == null) return null;

    	    if(k < node.key){
    	        node.left = remove_aux(node.left, k, flag);
    	        return node;
    	    }
    	    if(k > node.key){
    	        node.right = remove_aux(node.right, k, flag);
    	        return node;
    	    }

    	    // found
    	    flag.set(true);

    	    // two children
    	    if(node.left != null && node.right != null){
    	        BSTNode<T> min = find_min(node.right);
    	        node.key = min.key;
    	        node.data = min.data;
    	        node.right = remove_aux(node.right, min.key, flag);
    	        return node;
    	    }

    	    // zero/one child
    	    return (node.left != null) ? node.left : node.right;
    	}

    	private BSTNode<T> find_min(BSTNode<T> node){
    	    while(node != null && node.left != null) node = node.left;
    	    return node;
    	}

    	
    	public T search(int key) {
    	    if (findkey(key)) return current.data;
    	    return null;
    	}

    	public boolean delete(int key) {
    	    return remove_key(key); // لأن remove_key موجود في نسخة السلايدات/المحاضرة
    	}



    public boolean updateCurrent(T newData) {
        if (empty() || current == null) return false;
        current.data = newData;
        return true;
    }

    private static <E> void appendToList(List<E> l, E e) {
        if (l.full()) return;
        if (l.empty()) { l.insert(e); return; }
        l.findFirst();
        while (!l.last()) l.findNext();
        l.insert(e);
    }

    public LinkedList<T> inOrderTraversal() {
        LinkedList<T> out = new LinkedList<T>();
        inOrderRec(root, out);
        return out;
    }

    private void inOrderRec(BSTNode<T> n, List<T> out) {
        if (n == null) return;
        inOrderRec(n.left, out);
        appendToList(out, n.data);
        inOrderRec(n.right, out);
    }

    // Range Query: يرجع العناصر اللي key بينها [low, high] مع pruning
    public LinkedList<T> rangeQuery(int low, int high) {
        LinkedList<T> out = new LinkedList<T>();
        rangeRec(root, low, high, out);
        return out;
    }

    private void rangeRec(BSTNode<T> n, int low, int high, List<T> out) {
        if (n == null) return;

        // إذا low أصغر من key ممكن في يسار أشياء تفيد
        if (low < n.key) rangeRec(n.left, low, high, out);

        // إذا النود داخل المدى خذها
        if (low <= n.key && n.key <= high) appendToList(out, n.data);

        // إذا high أكبر من key ممكن في يمين أشياء تفيد
        if (high > n.key) rangeRec(n.right, low, high, out);
    }
}

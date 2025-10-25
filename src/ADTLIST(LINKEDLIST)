public class LinkedList<T> implements List<T>{
	private Node<T> head;
	private Node<T> current;

	public LinkedList () {
		head = current = null;
	}

	public boolean empty () {
		return head == null;
	}

	public boolean last () {
		return !empty() && current.next == null;
	}
  public boolean full () {
		return false;
	}
	public void findfirst () {
		current = head;
	}
	public void findnext () {
      if(!empty() && !last())
         current = current.next;
	}
	public T retrieve () {
		return !empty() ? current.data : null;
	}
	public void update (T val) {
      if(!empty())
         current.data = val;
    }
  public void insert (T val) {
		Node<T> tmp;
		if (empty()) {
			current = head = new Node<T> (val);
		}
		else {
			tmp = current.next;
			current.next = new Node<T> (val);
			current = current.next;
			current.next = tmp;
		}
	}
public void remove () {
   if(!empty()) {
		if (current == head) {
			head = head.next;
		}
		else {
			Node<T> tmp = head;

			while (tmp.next != current)
				tmp = tmp.next;

			tmp.next = current.next;
		}

		if (current.next == null)
			current = head;
		else
			current = current.next;
	}
}

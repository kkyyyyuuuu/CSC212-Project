public class Review {

    private int reviewId;
    private int productId;
    private int customerId;
    private int ratingScore;
    private String textComment;

    public Review(int reviewId, int productId, int customerId, int ratingScore, String textComment) {
        this.reviewId = reviewId;
        this.productId = productId;
        this.customerId = customerId;
        this.ratingScore = ratingScore;
        this.textComment = textComment;
    }

    public void editReview(int newRating, String newComment) {
        this.ratingScore = newRating;
        this.textComment = newComment;
    }

    public int getReviewId() {
        return reviewId;
    }

    public int getProductId() {
        return productId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public int getRatingScore() {
        return ratingScore;
    }

    public String getTextComment() {
        return textComment;
    }
}

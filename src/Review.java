public class Review {

    private int reviewId;
    private int productId;
    private int customerId;
    private int ratingScore;
    private String comment;

    public Review(int reviewId, int productId, int customerId, int ratingScore, String textComment) {
        this.reviewId = reviewId;
        this.productId = productId;
        this.customerId = customerId;
        this.ratingScore = ratingScore;
        this.comment = textComment;
    }

    public void editReview(int newRating, String newComment) {
        this.ratingScore = newRating;
        this.comment = newComment;
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
        return comment;
    }
	@Override
	public String toString() {
		return "Review [reviewId=" + reviewId + ", productId=" + productId + ", customerId=" + customerId
				+ ", ratingScore=" + ratingScore + ", comment=" + comment + "]";
	}
}

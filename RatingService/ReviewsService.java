package RatingService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ReviewsService {
	
	public Mono<Review> create(Review review);
	public Flux<Review> getAllReviewsByProductId(String productId, String sortBy);
	public Flux<Review> getAllReviewsOfProductIdByMinReviewValue(String productId, String sortBy, String filterValue);
	public Flux<Review> getAllReviewsOfProductIdByMaxReviewValue(String productId, String sortBy, String filterValue);
	public Flux<Review> getAllReviewsOfProductIdFromTime(String productId, String sortBy, String filterValue);
	public Flux<Review> getAllReviewsOfProductIdToTime(String productId, String sortBy, String filterValue);
	public Flux<Review> getAllReviewsByReviewersEmail(String email, String sortBy);
	public Flux<Review> getReviewsByReviewerAndByMinRating(String email, String sortBy, String filterValue);
	public Flux<Review> getReviewsByReviewerAndByMaxRating(String email, String sortBy, String rating);
	public Flux<Review> getReviewsByReviewerAndByTimestampFrom(String email, String sortBy, String filterValue);
	public Flux<Review> getReviewsByReviewerAndByTimestampTo(String email, String sortBy, String filterValue);

	

}

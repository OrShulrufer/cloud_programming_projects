package RatingService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Date;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestParam;

public interface ReviewsServiceCrud extends ReactiveMongoRepository<Review, String>{
	
	public Flux<Review> findAllByProductId(
			@Param("id") String id, 
			Sort sort);

	
	public Flux<Review> findAllByProductIdAndRatingGreaterThanEqual(
			@Param("id") String id, 
			@Param("rating") int rating,
			Sort sort);


	public Flux<Review> findAllByProductIdAndRatingLessThanEqual(
			@Param("id") String id, 
			@Param("rating") int rating, 
			Sort sort);


	public Flux<Review> findAllByProductIdAndReviewTimestampGreaterThanEqual(
			@Param("id") String productId,
			@Param("reviewTimestamp") Date filterValueDate,
			Sort by);


	public Flux<Review> findAllByProductIdAndReviewTimestampLessThanEqual(
			@Param("id") String productId, 
			@Param("reviewTimestamp") Date filterValueDate,
			Sort by);


	public Flux<Review> findAllBycustomer_email(
			@Param("email") String email, 
			Sort by);
	
	public Flux<Review> findAllBycustomer_emailAndRatingGreaterThanEqual(
			@Param("email") String email, 
			@Param("rating") int rating, 
			Sort by);
	
	public Flux<Review> findAllBycustomer_emailAndRatingLessThanEqual(
			@Param("email") String email, 
			@Param("rating") int rating, 
			Sort by);


	public Flux<Review> findAllBycustomer_emailAndReviewTimestampGreaterThanEqual(
			@Param("email")String email,
			@Param("email") Date filterValueDate,
			Sort by);


	public Flux<Review> findAllBycustomer_emailAndReviewTimestampLessThanEqual(
			@Param("email")String email,
			@Param("email")Date filterValueDate,
			Sort by);
	
	
	
}

package RatingService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ReviewsServiceImp implements ReviewsService {

	private ReviewsServiceCrud reviews;
	
	public static String VALID_EMAIL_PATTERN = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";

	@Autowired
	public ReviewsServiceImp(ReviewsServiceCrud reviews) {
		super();
		this.reviews = reviews;
	}
	
	//1 
	@Override
	public Mono<Review> create(Review review) {
		setReviewWhenCreate(review);
		
			
		if(!checkIsRatingOK(review.getRating())) {
			throw new IllegalInputException("Rating must be between 1 to 5 !");
		}
		if(!checkIsEmailOK(review)) {
			throw new IllegalInputException("Consumer's Email is NOT valid");
		}
		
		return this.reviews.save(review);
	}
	
	private void setReviewWhenCreate(Review review) {
		if(review.getId()!=null)
			review.setId(null);
		
		
		Date date = new Date(System.currentTimeMillis()); // this object contains the current date value
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ\n");
		try {
			review.setReviewTimestamp(formatter.parse(formatter.format(date)));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}

	//2
	@Override
	public Flux<Review> getAllReviewsByProductId(String productId, String sortBy) {
		return this.reviews.findAllByProductId(productId, Sort.by(sortBy));
	}

	//3
	@Override
	public Flux<Review> getAllReviewsOfProductIdByMinReviewValue(String productId, String sortBy, String filterValue) {
		int filterValueInteger = Integer.parseInt(filterValue);
		if(!checkIsRatingOK(filterValueInteger)){
			throw new IllegalInputException("Rating must be between 1 to 5 !");
		}						
		return this.reviews
				.findAllByProductIdAndRatingGreaterThanEqual
				(productId, filterValueInteger, Sort.by(sortBy));
	}
	
	//4
	@Override
	public Flux<Review> getAllReviewsOfProductIdByMaxReviewValue(String productId, String sortBy, String filterValue) {
		int filterValueIteger = Integer.parseInt(filterValue);
		if(!checkIsRatingOK(filterValueIteger)){
			throw new IllegalInputException("Rating must be between 1 to 5 !");
		}						
		return this.reviews
				.findAllByProductIdAndRatingLessThanEqual
				(productId, filterValueIteger, Sort.by(sortBy));
	}
	
	//5
	@Override
	public Flux<Review> getAllReviewsOfProductIdFromTime(String productId, String sortBy, String filterValue) {
		SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
		Date filterValueDate = null;
		try {
			filterValueDate = originalFormat.parse(filterValue);
		} catch (ParseException e) {
			e.printStackTrace();
		}	
		return  this.reviews
				.findAllByProductIdAndReviewTimestampGreaterThanEqual
				(productId, filterValueDate, Sort.by(sortBy));
	}

	//6
	@Override
	public Flux<Review> getAllReviewsOfProductIdToTime(String productId, String sortBy, String filterValue) {
		SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
		Date filterValueDate = null;
		try {
			filterValueDate = originalFormat.parse(filterValue);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return  this.reviews
				.findAllByProductIdAndReviewTimestampLessThanEqual
				(productId, filterValueDate, Sort.by(sortBy));
	}
	
	//7
	@Override
	public Flux<Review> getAllReviewsByReviewersEmail(String email, String sortBy) {
		if (!Pattern.matches(VALID_EMAIL_PATTERN, email.trim()))
			throw new IllegalInputException("Ilegal Mail Address");
		return this.reviews.findAllBycustomer_email
				(email,  Sort.by(sortBy));
	}
	
	
	//8
	@Override
	public Flux<Review> getReviewsByReviewerAndByMinRating(String email, String sortBy, String rating) {
		if (!Pattern.matches(VALID_EMAIL_PATTERN, email.trim()))
			throw new IllegalInputException("Ilegal Mail Address");
		int ratingInt=Integer.parseInt(rating);
		return this.reviews.findAllBycustomer_emailAndRatingGreaterThanEqual(email, ratingInt,Sort.by(sortBy));
	}
	
	//9
	@Override
	public Flux<Review> getReviewsByReviewerAndByMaxRating(String email, String sortBy, String rating) {
		if (!Pattern.matches(VALID_EMAIL_PATTERN, email.trim()))
			throw new IllegalInputException("Ilegal Mail Address");
		int ratingInt=Integer.parseInt(rating);
		return this.reviews.findAllBycustomer_emailAndRatingLessThanEqual(email, ratingInt,Sort.by(sortBy));
	}
	
	//10
	@Override
	public Flux<Review> getReviewsByReviewerAndByTimestampFrom(String email, String sortBy, String filterValue) {
		if (!Pattern.matches(VALID_EMAIL_PATTERN, email.trim()))
			throw new IllegalInputException("Ilegal Mail Address");		
		
		
		
		SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
		try {
			Date filterValueDate = originalFormat.parse(filterValue);
					return this.reviews.findAllBycustomer_emailAndReviewTimestampGreaterThanEqual(email, filterValueDate,Sort.by(sortBy));

		} catch (ParseException e) {
			throw new IllegalInputException("Ilegal Timestamp");	
		}	

		

	}
	
	//11
		@Override
		public Flux<Review> getReviewsByReviewerAndByTimestampTo(String email, String sortBy, String filterValue) {
			if (!Pattern.matches(VALID_EMAIL_PATTERN, email.trim()))
				throw new IllegalInputException("Ilegal Mail Address");		
			
			
			
			SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
			try {
				Date filterValueDate = originalFormat.parse(filterValue);
						return this.reviews.findAllBycustomer_emailAndReviewTimestampLessThanEqual(email, filterValueDate,Sort.by(sortBy));

			} catch (ParseException e) {
				throw new IllegalInputException("Ilegal Timestamp");	
			}	

			

		}
	
	private boolean checkIsEmailOK(Review review) {
		if (Pattern.matches(VALID_EMAIL_PATTERN, review.getCustomer().getEmail().trim())) {
			return true;
		}
		return false;
	}
	
	private boolean checkIsRatingOK(int rating) {
		if(rating < 1 || rating > 5) {
			return false;
		}
		return true;
	}

	



	





	
}










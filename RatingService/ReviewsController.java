package RatingService;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Stream;

import org.springframework.web.bind.annotation.ExceptionHandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class ReviewsController {
	
	private ReviewsService reviews;

	@Autowired
	public ReviewsController(ReviewsService reviews) {
		super();
		this.reviews = reviews;
	}
	
	//1: POST /reviews
	@RequestMapping(path = "/reviews", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Mono<Review> create (@RequestBody Review review){
		return this.reviews.create(review);
	}
		
    // 2+3+4+5+6
	@RequestMapping(path = "/reviews/byProduct/{productId}", method = RequestMethod.GET, produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<Review> getAllReviewsByProductIdController( @PathVariable("productId") String productId,
			@RequestParam(name = "sortBy", required = false, defaultValue = "id") String sortBy,
			@RequestParam(name = "filterType", required = false, defaultValue = "") String filterType,
			@RequestParam(name = "filterValue", required = false, defaultValue = "") String filterValue
			){
		
		Flux<Review> reviews = null;
		
		//3: GET /reviews/byProduct/{productId}?filterType=byMinRating&filterValue={minRating}&sortBy={sortAttr}
		if(filterType.equals("byMinRating")) {
			reviews = this.reviews
					.getAllReviewsOfProductIdByMinReviewValue(productId, sortBy, filterValue);
		} 
		
		//4: GET /reviews/byProduct/{productId}?filterType=byMaxRating&filterValue={maxRating}&sortBy={sortAttr}
		else if (filterType.equals("byMaxRating")){
			reviews = this.reviews
					.getAllReviewsOfProductIdByMaxReviewValue(productId, sortBy, filterValue);
		}
		
		//5: GET /reviews/byProduct/{productId}?filterType=byTimestampFrom&filterValue={fromTime} &sortBy={sortAttr}
		else if (filterType.equals("byTimestampFrom")) {
			reviews = this.reviews
					.getAllReviewsOfProductIdFromTime(productId, sortBy, filterValue);
		}
	    
		//6: GET /reviews/byProduct/{productId}?filterType=byTimestampTo&filterValue={toTime} &sortBy={sortAttr}
		else if (filterType.equals("byTimestampTo")) {
			reviews = this.reviews
					.getAllReviewsOfProductIdToTime(productId, sortBy, filterValue);	
		}
		
		
		//2: GET /reviews/byProduct/{productId}?sortBy={sortAttr}
		else if (filterType.equals("") && filterValue.equals("")) {
			reviews = this.reviews.getAllReviewsByProductId(productId, sortBy);
		}
		
		else {
			throw new IllegalInputException("Ilegal Parameters");
		}	
		
		return reviews;	
	}

	@RequestMapping(path = "/reviews/byReviewer/{email}", method = RequestMethod.GET, produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<Review> getAllReviewsByReviewerController( @PathVariable("email") String email,
			@RequestParam(name = "sortBy", required = false, defaultValue = "id") String sortBy,
			@RequestParam(name = "filterType", required = false, defaultValue = "") String filterType,
			@RequestParam(name = "filterValue", required = false, defaultValue = "") String filterValue
		
			
			){
		
		//7: GET /reviews/byReviewer/{email}?sortBy={sortAttr}
		if(filterType.equals("")&&(filterValue.equals("")))
		return this.reviews.getAllReviewsByReviewersEmail(email, sortBy);
		
		
			//8: GET /reviews/byReviewer/{email}?filterType=byMinRating&filterValue={minRating} &sortBy={sortAttr}
		else if(filterType.equals("byMinRating"))
		{
			return this.reviews.getReviewsByReviewerAndByMinRating(email, sortBy, filterValue);

		}
		
		
			//9: GET /reviews/byReviewer/{email}?filterType=byMaxRating&filterValue={maxRating} &sortBy={sortAttr}
		else if(filterType.equals("byMaxRating"))
		{
			return this.reviews.getReviewsByReviewerAndByMaxRating(email, sortBy, filterValue);

		}
		
		
			//10: GET /reviews/byReviewer/{email}?filterType=byTimestampFrom&filterValue={fromTime} &sortBy={sortAttr}
		else if(filterType.equals("byTimestampFrom"))
			return this.reviews.getReviewsByReviewerAndByTimestampFrom(email, sortBy, filterValue);
		
		
			//11: GET /reviews/byReviewer/{email}?filterType=byTimestampTo&filterValue={toTime} &sortBy={sortAttr}
		else if(filterType.equals("byTimestampTo"))
			return this.reviews.getReviewsByReviewerAndByTimestampTo(email, sortBy, filterValue);

		else
			throw new IllegalInputException("Ilegal Parameters");
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	//12: GET /reviews/byRatingBetween/{minRatingInclusive}/{maxRatingInclusice}? sortBy={sortAttr}
	
	
	//13: GET /reviews/byRatingBetween/{minRatingInclusive}/{maxRatingInclusice}?filterType=byTimestampFrom&filterValue={fromTime} &sortBy={sortAttr}
	
	
	//14: GET /reviews/byRatingBetween/{minRatingInclusive}/{maxRatingInclusice}?filterType=byTimestampTo&filterValue={toTime}&sortBy={sortAttr}
	
	
	//15: DELETE /reviews

	
	
	
	//Error code 500
	@ExceptionHandler
	@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
	public Map<String, String> handleExistException(ExistException e) {
		String message = e.getMessage();
		if (message == null) {
			message = "Already exist!";
		}
		return Collections.singletonMap("Error", message);
	}

	//Error code 400
	@ExceptionHandler
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	public Map<String, String> handleIllegalInputException(IllegalInputException e) {
		String message = e.getMessage();
		if (message == null) {
			message = "Error due to illegal arguments";
		}
		return Collections.singletonMap("Error", message);
	}
	
	//Error code 404
	@ExceptionHandler
	@ResponseStatus(code = HttpStatus.NOT_FOUND)
	public Map<String, String> handleNotFoundException(NotFoundException e) {
		String message = e.getMessage();
		if (message == null) {
			message = "NOT Found!";
		}
		return Collections.singletonMap("Error", message);
	}
	
}

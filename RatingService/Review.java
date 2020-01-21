package RatingService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

//import javax.persistence.GeneratedValue;

//import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "reviews")
public class Review {
	private String id;
	private Customer customer;
	private Product product;
	private int rating;
	private Date reviewTimestamp;
	private Map<String, String> reviewContent;
	
	public Review() {
		reviewContent = new HashMap<>();
	}
	
	public Review(Customer customer, Product product, int rating, Map<String, String> reviewContent) {
		super();
		//this.id = ObjectId();
		this.customer = customer;
		this.product = product;
		this.rating = rating;
		Date date = new Date(System.currentTimeMillis()); // this object contains the current date value
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ\n");
		try {
			this.reviewTimestamp = formatter.parse(formatter.format(date));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		this.reviewContent = reviewContent;
	}

	@Id 
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public Customer getCustomer() {
		return customer;
	}
	
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	
	public Product getProduct() {
		return product;
	}
	
	public void setProduct(Product product) {
		this.product = product;
	}
	
	public int getRating() {
		return rating;
	}
	
	public void setRating(int rating) {
		this.rating = rating;
	}
	
	public Date getReviewTimestamp() {
		return reviewTimestamp;
	}
	
	public void setReviewTimestamp(Date reviewTimestamp) {
		this.reviewTimestamp = reviewTimestamp;
	}

	public Map<String, String> getReviewContent() {
		return reviewContent;
	}

	public void setReviewContent(Map<String, String> reviewContent) {
		this.reviewContent = reviewContent;		
	}
	
	
}

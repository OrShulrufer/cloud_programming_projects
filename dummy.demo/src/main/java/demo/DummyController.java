package demo;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DummyController {
	private Map<String, Dummy> dummies;
	private int id;
	
	@PostConstruct
	public void init() {
		id = 1000;
		this.dummies = Collections.synchronizedMap(new TreeMap<>());
		
		IntStream.range(1, 1001)
			.mapToObj(i->"message_" + i)
			.map(msg->new Dummy("" + id++, msg))
			.forEach(d->this.dummies.put(d.getId(), d));
	}
	
	@RequestMapping(path = "/dummies", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Dummy[] getAll (
			@RequestParam(name="page", required = false, defaultValue = "0") int page, 
			@RequestParam(name="size", required = false, defaultValue = "20") int size,
			
			@RequestParam(name="message", required = false, defaultValue = "") String byMessage,
			@RequestParam(name="maxLength", required = false, defaultValue = "-1") int maxLength,
			@RequestParam(name="minLength", required = false, defaultValue = "-1") int minLength) {
		Stream<Dummy> col;
		
		if (!byMessage.isEmpty()) {
			if (maxLength < 0 && minLength < 0) {
				col = this.dummies
						.values()
						.stream()
						.filter(msg->byMessage.equals(msg.getMessage()));
			}else {
				throw new IllegalInputException("invalid set of filters");
			}
			
		}else if(maxLength >= 0 && minLength < 0){
			col = this.dummies
					.values()
					.stream()
					.filter(d->d.getMessage() == null || d.getMessage().length() <= maxLength);
		}else if(maxLength >= 0 && minLength >= 0){
			col = this.dummies
					.values()
					.stream()
					.filter(d->d.getMessage() == null || d.getMessage().length() <= maxLength)
					.filter(d->d.getMessage() != null && d.getMessage().length() >= minLength);
		}else if (minLength >= 0){
			throw new IllegalInputException("can not search by minimum length only");
		}
		else {
			col = this.dummies
					.values()
					.stream();
		}
		
		return col
				.skip(size*page)
				.limit(size)
				.collect(Collectors.toList())
				.toArray(new Dummy[0]);
	}
	
	@ExceptionHandler
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	public Map<String, String> handleError(IllegalInputException e){
		String message = e.getMessage();
		if (message == null) {
			message = "Error due to illegal arguments";
		}
		
		return Collections.singletonMap("error", message);
	}
}

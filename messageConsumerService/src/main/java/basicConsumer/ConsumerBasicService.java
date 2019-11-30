package basicConsumer;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@Validated
public class ConsumerBasicService {
	private RestTemplate rest;
	private String url;
	
	@Value("${basicService.url}")
	public void setUrl(String url) {
		this.url = url;
	}
	
	@PostConstruct
	public void init() {
		this.rest = new RestTemplate();
		if (this.url == null || this.url.trim().isEmpty()) {
			throw new RuntimeException("no basic service url is defined");
		}else {
			System.err.println(this.url);
		}
	}
	
	@RequestMapping(path = "/create/{count}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object>[] createMany (
			@PathVariable("count") int count){
		return (Map<String, Object>[])
				IntStream.range(0, count)
				.map(i->i+1)
				.mapToObj(i->Collections.singletonMap("message", "new message #" + i))
				.map(msg->rest.postForObject(url, msg, Map.class))
				.collect(Collectors.toList())
				.toArray(new Map<?, ?>[0]);
	}
	
	@RequestMapping(path = "/delete", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object>[] deleteAll(
			@RequestParam(name="size", required = false, defaultValue = "20") @Min(1) @Max(20) int size){
		int page = 0;
		
		List<Map<String, Object>> rv = new ArrayList<>();
		
		long deleted;
		do {
			deleted =
			Stream.of(
					this.rest.getForObject(this.url + "?page={page}&size={size}", Map[].class, page, size))
					.peek(rv::add)
					.map(msg->msg.get("id"))
					.peek(id->rest.delete(url + "/{id}", id))
					.count();
		}while (deleted == size);
		
		return (Map<String, Object>[]) rv
				.toArray(new Map<?, ?>[0]);		
	}
	
	@RequestMapping(path = "/replace/{prefix}/{newContent}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object>[] updateAllMessagesByPrefix (
			@PathVariable("prefix") String prefix, 
			@PathVariable("newContent") String newContent){
		int size = 3;
		int page = 0;
		String lowerPref = prefix.toLowerCase();
		List<Map<String, Object>> rv = new ArrayList<>();
		AtomicLong theCounter;
		
		do {
			AtomicLong counter = new AtomicLong(0L);
			Stream.of(rest.getForObject(url + "?size={size}&page={page}", Map[].class, size, page++))
.peek(System.err::println)			
				.peek(msg->counter.incrementAndGet())
				.filter(msg->msg.get("message").toString().toLowerCase().startsWith(lowerPref))
.peek(System.err::println)				
				.peek(msg->msg.put("message", newContent))
				.peek(msg->rest.put(url + "/{id}", msg, msg.get("id")))
				.map(msg->msg.get("id"))
				.map(id->rest.getForObject(url + "/{id}", Map.class, id))
				.forEach(rv::add);
			
			theCounter = counter;
			System.err.println("counter = " + theCounter + ", size = " + size);
		}while(theCounter.get() == size);
		
		return (Map<String, Object>[]) rv
				.toArray(new Map<?, ?>[0]);	
	}
	
}

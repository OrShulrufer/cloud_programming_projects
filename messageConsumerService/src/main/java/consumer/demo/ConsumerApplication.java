package consumer.demo;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.web.client.RestTemplate;

public class ConsumerApplication {

	public static void main(String[] args) {
		//String url = "http://localhost:8095/message";
		// DANIEL
//		String url = "http://basicmicroservice-env.hr2szs2rdf.us-east-2.elasticbeanstalk.com:8095/message";
		
		// YOSSI
//		String url = "http://helloclouddev-env.gxjpd3nupf.us-east-2.elasticbeanstalk.com:8095/message";
		
		// Sean
//		String url = "http://ec2-18-221-60-116.us-east-2.compute.amazonaws.com:8080/messages";

		// Binyamin
//		String url = "http://ec2-52-200-85-240.compute-1.amazonaws.com:8095/message";

		// Itai
		String url = "http://18.219.107.52:8088/message";
		
		RestTemplate restTemplate = new RestTemplate();
		
		String no = null;
		try {
			System.out.println("creating new message...");
			Object newMessage = Collections.singletonMap("message", "test");
			Map<?, ?> ouputJson = restTemplate.postForObject(url, newMessage, Map.class);
			no = ouputJson.get("id").toString();
			System.out.println(ouputJson);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			System.out.println("updating message #" + no);
			Map<String, Object> object = Collections.singletonMap("message", "updated message");
			restTemplate.put(url + "/{id}", object, no);
			
			System.out.println("successfully updated message #" + no);
		} catch (Exception e) {
			e.printStackTrace();
		}
		

		try {
			System.out.println("\nretrieving message #" + no);
			Map<?,?> message = restTemplate.getForObject(url + "/{id}", Map.class, no);
			System.out.println("message retrieved: " + message);
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			System.out.println("deleting message #" + no);
			restTemplate.delete(url + "/{id}", no);
			
			System.out.println("successfully deleted message #" + no);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			int size = 3;
			int page = 1;
			System.out.println("\nretrieving " + size + "  messages from page #" + (page + 1));
			List<?> messages = restTemplate.getForObject(url + "?size={size}&page={page}", List.class, size, page);
			System.out.println("messages retrieved: ");
			messages
				.forEach(System.out::println);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}

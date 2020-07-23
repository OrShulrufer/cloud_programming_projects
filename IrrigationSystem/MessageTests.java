package acs;

import javax.annotation.PostConstruct;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.web.client.RestTemplate;


@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class MessageTests {	
	private RestTemplate restTemplate;
	private String url;
	private int port;
	
	@Test
	public void testContext() {
		
	}
	
	@LocalServerPort
	public void setPort(int port) {
		this.port = port;
	}
	
	@PostConstruct
	public void init() {
		this.restTemplate = new RestTemplate();
		this.url = "http://localhost:" + this.port + "/acs/elements/";
	}
	
	@BeforeEach
	public void setup() {
		
	}
	
	@AfterEach 
	public void teardown() {
//		this.restTemplate
//			.delete(this.url + "deleteAll/adminDomain/adminEmail");
	}

}

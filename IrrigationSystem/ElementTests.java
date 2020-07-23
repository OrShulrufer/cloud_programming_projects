package acs;


import javax.annotation.PostConstruct;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Map;

import acs.data.CreatedByConverter;
import acs.data.ElementEntity;
import acs.data.ElementId;
import acs.data.NewUserDetails;
import acs.data.UserId;
import acs.data.UserRole;
import acs.rest.boudanries.ActionBoundary;
import acs.rest.boudanries.ElementBoundary;
import acs.rest.boudanries.UserBoundary;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ElementTests {
	private RestTemplate restTemplate;
	private String url;
	private int port;
	private CreatedByConverter createdByConverter = new CreatedByConverter();
	
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
		this.restTemplate
			.delete(this.url + "deleteAll/adminDomain/adminEmail");
	}
	
	
	@Test
	public void test_create_new_element_and_then_element_in_database() throws Exception{ 
		// GIVEN the server is up and system contains manager
		NewUserDetails userDitails = new NewUserDetails();
		userDitails.setAvatar(":-");
		userDitails.setEmail("aaa@ff.ff");
		userDitails.setRole(UserRole.MANAGER);
		userDitails.setUsername("aaa");
		UserId userId = this.restTemplate.postForObject("http://localhost:" + this.port + "/acs/users", 
				userDitails, 
				UserBoundary.class).getUserId(); 
		System.out.println(userId.toString()+"user");  

		// WHEN I POST new element by manager"
		ElementBoundary elementBoundary = new ElementBoundary();
		ElementId elementId = new ElementId();
		elementId.setDomain(userId.getDomain());
		elementBoundary.setElementId(elementId);
		
		elementBoundary.setCreatedBy(this.createdByConverter.fromUserIdToCreatedBy(userId));
		elementBoundary.setIsActive(true);
	
		ElementId putElementId = this.restTemplate
			.postForObject(this.url + userId.getDomain() + "/" + userId.getEmail(), 
					elementBoundary, 
					ElementBoundary.class).getElementId();
		 
		
		// THEN the database contains that element
		ElementId retriveElementId = this.restTemplate
				.getForObject(this.url + userId.getDomain() + "/" + userId.getEmail() +
						"/" + putElementId.getDomain() + "/" + putElementId.getId(),
						ElementBoundary.class).getElementId();
		
		assertThat(retriveElementId)
		.isNotNull();
	}
	
	

	@Test
	public void test_create_new_element_if_user_is_player_and_fail() throws Exception{
		// GIVEN the server is up and system contains player
		NewUserDetails userDitails = new NewUserDetails();
		userDitails.setAvatar(":-");
		userDitails.setEmail("aaa@ff.ff");
		userDitails.setRole(UserRole.PLAYER);
		userDitails.setUsername("aaa");
		UserId userId = this.restTemplate.postForObject("http://localhost:" + this.port + "/acs/users", 
				userDitails, 
				UserBoundary.class).getUserId();

		// WHEN I POST new element by player"
		ElementBoundary elementBoundary = new ElementBoundary();
		ElementId elementId = new ElementId();
		elementId.setDomain(userId.getDomain());
		elementBoundary.setElementId(elementId);
		elementBoundary.setCreatedBy(this.createdByConverter.fromUserIdToCreatedBy(userId));
		elementBoundary.setIsActive(true);
	
		// Then throw exception
		assertThrows(Exception.class, ()-> this.restTemplate
			.postForObject(this.url + userId.getDomain() + "/" + userId.getEmail(), 
					elementBoundary, 
					ElementBoundary.class).getElementId());
	}
	
	
	@Test
	public void test_update_new_element_if_user_is_player_and_fail() throws Exception{
		// GIVEN the server is up and system contains player and element(created by manager)
		NewUserDetails userDitails = new NewUserDetails();
		userDitails.setAvatar(":-");
		userDitails.setEmail("aaa@ff.ff");
		userDitails.setRole(UserRole.PLAYER);
		userDitails.setUsername("aaa");
		UserId userIdPlayer= this.restTemplate.postForObject("http://localhost:" + this.port + "/acs/users", 
				userDitails, 
				UserBoundary.class).getUserId();
		
		userDitails.setAvatar(":-");
		userDitails.setEmail("aaa@ff.ff");
		userDitails.setRole(UserRole.MANAGER);
		userDitails.setUsername("aaa");
		UserId userIdManager = this.restTemplate.postForObject("http://localhost:" + this.port + "/acs/users", 
				userDitails, 
				UserBoundary.class).getUserId();
		
		ElementBoundary elementBoundary = new ElementBoundary();
		ElementId elementId = new ElementId();
		elementId.setDomain(userIdManager.getDomain());
		elementBoundary.setElementId(elementId);
		elementBoundary.setCreatedBy(this.createdByConverter.fromUserIdToCreatedBy(userIdManager));
		elementBoundary.setIsActive(true);	
		ElementBoundary postedElement = this.restTemplate
			.postForObject(this.url + userIdManager.getDomain() + "/" + userIdManager.getEmail(), 
					elementBoundary, 
					ElementBoundary.class);

		// WHEN I PUT element by player"
		// Then throw exception
		assertThrows(Exception.class, ()-> this.restTemplate
				.put(url + userIdPlayer.getDomain() + "/" + userIdPlayer.getEmail() +
						"/" + elementId.getDomain() + "/" + elementId.getId(),
						postedElement));

	}
	
	
	@Test
	public void test_update_new_element_if_user_is_manager_and_element_in_DB() throws Exception{
		// GIVEN the server is up and system contains manager and element
		NewUserDetails userDitails = new NewUserDetails();
		userDitails.setAvatar(":-");
		userDitails.setEmail("aaa@ff.ff");
		userDitails.setRole(UserRole.MANAGER);
		userDitails.setUsername("aaa");
		UserId userIdManager = this.restTemplate.postForObject("http://localhost:" + this.port + "/acs/users", 
				userDitails, 
				UserBoundary.class).getUserId();
		
		ElementBoundary elementBoundaryPosted = new ElementBoundary();
		ElementId postedElementId = new ElementId();
		postedElementId.setDomain(userIdManager.getDomain());
		elementBoundaryPosted.setElementId(postedElementId);
		elementBoundaryPosted.setCreatedBy(this.createdByConverter.fromUserIdToCreatedBy(userIdManager));
		elementBoundaryPosted.setIsActive(true);	
		
		ElementId elementIdDB = this.restTemplate
			.postForObject(this.url + userIdManager.getDomain() + "/" + userIdManager.getEmail(), 
					elementBoundaryPosted, 
					ElementBoundary.class).getElementId();
		

		// WHEN I PUT element by manager"
		ElementBoundary elementBoundaryPut = new ElementBoundary();
		elementBoundaryPut.setElementId(elementIdDB);
		elementBoundaryPut.setCreatedBy(this.createdByConverter.fromUserIdToCreatedBy(userIdManager));
		elementBoundaryPut.setName("ggg");
		
		this.restTemplate
				.put(this.url + userIdManager.getDomain() + "/" + userIdManager.getEmail() +
						"/" + elementIdDB.getDomain() + "/" + elementIdDB.getId(),
						elementBoundaryPut);
	
		// THEN the database contains that element
		ElementBoundary  retriveElement = this.restTemplate
				.getForObject(this.url + userIdManager.getDomain() + "/" + userIdManager.getEmail() +
						"/" + elementIdDB.getDomain() + "/" + elementIdDB.getId(),
						ElementBoundary.class);

		assertThat(retriveElement.getName())
		.isNotNull()
		.isEqualTo("ggg");
	}

	
	@Test
	public void get_all_children_from_DB_by_manager() throws Exception{
		// GIVEN the server is up and system contains manager and element and his children
		NewUserDetails userDitails = new NewUserDetails();
		userDitails.setAvatar(":-");
		userDitails.setEmail("aaa@ff.ff");
		userDitails.setRole(UserRole.MANAGER);
		userDitails.setUsername("aaa");
		UserId userIdManager = this.restTemplate.postForObject("http://localhost:" + this.port + "/acs/users", 
				userDitails, 
				UserBoundary.class).getUserId();
		
		ElementBoundary parent = new ElementBoundary();
		ElementId parentId = new ElementId();
		parentId.setDomain(userIdManager.getDomain());
		parent.setElementId(parentId);
		parent.setCreatedBy(this.createdByConverter.fromUserIdToCreatedBy(userIdManager));
		parent.setIsActive(true);	
		
		ElementBoundary parentDB = this.restTemplate
			.postForObject(this.url + userIdManager.getDomain() + "/" + userIdManager.getEmail(), 
					parent, 
					ElementBoundary.class);
		
		
		
		ElementBoundary chiled = new ElementBoundary();
		ElementId chiledId = new ElementId();
		chiledId.setDomain(userIdManager.getDomain());
		chiled.setElementId(chiledId);
		chiled.setCreatedBy(this.createdByConverter.fromUserIdToCreatedBy(userIdManager));
		chiled.setIsActive(true);
		
		ElementBoundary chiledDB = this.restTemplate
				.postForObject(this.url + userIdManager.getDomain() + "/" + userIdManager.getEmail(), 
						chiled, 
						ElementBoundary.class);
		
		
		//bound parent with child
		this.restTemplate
		.put(this.url + userIdManager.getDomain() + "/" + userIdManager.getEmail() +
				"/" + parentDB.getElementId().getDomain() + "/" + parentDB.getElementId().getId()
				+ "/children",
				chiledDB.getElementId());
		
		// WHEN  manager trying to get children of element 
		ElementBoundary[]  retriveElements = this.restTemplate
				.getForObject(this.url + userIdManager.getDomain() + "/" + userIdManager.getEmail() +
						"/" + parentDB.getElementId().getDomain() + "/" + parentDB.getElementId().getId()
						+ "/children",
						ElementBoundary[].class);
		
		// THEN he succeeds
		assertThat(retriveElements[0].getElementId())
		.isNotNull()
		.isEqualTo(chiledDB.getElementId());	
	}
	
	@Test
	public void get_all_parents_from_DB_by_manager() throws Exception{
		
		// GIVEN the server is up and system contains manager and element and his children
		NewUserDetails userDitails = new NewUserDetails();
		userDitails.setAvatar(":-");
		userDitails.setEmail("aaa@ff.ff");
		userDitails.setRole(UserRole.MANAGER);
		userDitails.setUsername("aaa");
		UserId userIdManager = this.restTemplate.postForObject("http://localhost:" + this.port + "/acs/users", 
				userDitails, 
				UserBoundary.class).getUserId();
		
		ElementBoundary parent = new ElementBoundary();
		ElementId parentId = new ElementId();
		parentId.setDomain(userIdManager.getDomain());
		parent.setElementId(parentId);
		parent.setCreatedBy(this.createdByConverter.fromUserIdToCreatedBy(userIdManager));
		parent.setIsActive(true);	
		
		ElementBoundary parentDB = this.restTemplate
			.postForObject(this.url + userIdManager.getDomain() + "/" + userIdManager.getEmail(), 
					parent, 
					ElementBoundary.class);
		
		
		
		ElementBoundary chiled = new ElementBoundary();
		ElementId chiledId = new ElementId();
		chiledId.setDomain(userIdManager.getDomain());
		chiled.setElementId(chiledId);
		chiled.setCreatedBy(this.createdByConverter.fromUserIdToCreatedBy(userIdManager));
		chiled.setIsActive(true);
		
		ElementBoundary chiledDB = this.restTemplate
				.postForObject(this.url + userIdManager.getDomain() + "/" + userIdManager.getEmail(), 
						chiled, 
						ElementBoundary.class);  
		
		//bound parent with child
		this.restTemplate
		.put(this.url + userIdManager.getDomain() + "/" + userIdManager.getEmail() +
				"/" + parentDB.getElementId().getDomain() + "/" + parentDB.getElementId().getId()
				+ "/children",
				chiledDB.getElementId());
		
		// WHEN  manager trying to get parents of element 
		
		ElementBoundary[]  retriveElements = this.restTemplate
				.getForObject(this.url + userIdManager.getDomain() + "/" + userIdManager.getEmail() +
						"/" + chiledDB.getElementId().getDomain() + "/" + chiledDB.getElementId().getId()
						+ "/parents",
						ElementBoundary[].class);

		// THEN manager succeeds to get parent from DB
		assertThat(retriveElements[0].getElementId())
		.isNotNull()
		.isEqualTo(parentDB.getElementId());			
	}
	
	@Test
	public void get_children_of_deleted_parent_by_player_and_get_exeption() throws Exception{

		// GIVEN the server is up and system contains player and element and his children
		NewUserDetails userDitailsPlayer = new NewUserDetails();
		userDitailsPlayer.setAvatar(":-");
		userDitailsPlayer.setEmail("aaa@ff.ff");
		userDitailsPlayer.setRole(UserRole.PLAYER);
		userDitailsPlayer.setUsername("aaa");
		UserId userIdPlayer = this.restTemplate.postForObject("http://localhost:" + this.port + "/acs/users", 
				userDitailsPlayer, 
				UserBoundary.class).getUserId();
		
		NewUserDetails userDitailsManager = new NewUserDetails();
		userDitailsManager.setEmail("aaas@ff.fff");
		userDitailsManager.setRole(UserRole.MANAGER);
		userDitailsManager.setAvatar(":-)");
		userDitailsManager.setUsername("aaaa");
		UserId userIdManager = this.restTemplate.postForObject("http://localhost:" + this.port + "/acs/users", 
				userDitailsManager, 
				UserBoundary.class).getUserId();
	
		ElementBoundary parent = new ElementBoundary();
		ElementId parentId = new ElementId();
		parentId.setDomain(userIdManager.getDomain());
		parent.setElementId(parentId);
		parent.setCreatedBy(this.createdByConverter.fromUserIdToCreatedBy(userIdManager));
		parent.setIsActive(true);			
		ElementBoundary parentDB = this.restTemplate
			.postForObject(this.url + userIdManager.getDomain() + "/" + userIdManager.getEmail(), 
					parent, 
					ElementBoundary.class);
			
		
		ElementBoundary chiled = new ElementBoundary();
		ElementId chiledId = new ElementId();
		chiledId.setDomain(userIdManager.getDomain());
		chiled.setElementId(chiledId);
		chiled.setCreatedBy(this.createdByConverter.fromUserIdToCreatedBy(userIdManager));
		chiled.setIsActive(true);	
		ElementBoundary chiledDB = this.restTemplate
				.postForObject(this.url + userIdManager.getDomain() + "/" + userIdManager.getEmail(), 
						chiled, 
						ElementBoundary.class);
		
		
		//bound parent with child
		this.restTemplate
		.put(this.url + userIdManager.getDomain() + "/" + userIdManager.getEmail() +
				"/" + parentDB.getElementId().getDomain() + "/" + parentDB.getElementId().getId()
				+ "/children",
				chiledDB.getElementId());
		
		
		//parent not active
		ActionBoundary action = new ActionBoundary();
		action.setType("Delete");
		action.setElement(parentDB.getElementId());
		action.setInvokedBy(userIdPlayer);
		
		this.restTemplate
				.postForObject("http://localhost:" + this.port + "/acs/actions",
				 		action,
						Object.class);
		// WHEN player trying to get children of not active element
		// THEN the database throws exception
		assertThrows(Exception.class, ()-> this.restTemplate
						.getForObject(this.url + userIdPlayer.getDomain() + "/" + userIdPlayer.getEmail() +
								"/" + parentDB.getElementId().getDomain() + "/" + parentDB.getElementId().getId()
								+ "/children",
								ElementBoundary[].class));	
	}
	
	@Test
	public void get_all_element_atribute_data_action_test() throws Exception{
		// GIVEN the server is up and system contains manager, player and element
		NewUserDetails userDitails = new NewUserDetails();
		userDitails.setAvatar(":-");
		userDitails.setEmail("aaa@ff.ff");
		userDitails.setRole(UserRole.MANAGER);
		userDitails.setUsername("aaa");
		UserId userIdManager = this.restTemplate.postForObject("http://localhost:" + this.port + "/acs/users", 
				userDitails, 
				UserBoundary.class).getUserId();
		

		NewUserDetails playerDitails = new NewUserDetails();
		playerDitails.setAvatar(":-");
		playerDitails.setEmail("aaa@kk.ff");
		playerDitails.setRole(UserRole.PLAYER);
		playerDitails.setUsername("aaaa");
		UserId userIdPlayer= this.restTemplate.postForObject("http://localhost:" + this.port + "/acs/users", 
				playerDitails, 
				UserBoundary.class).getUserId();
		
		ElementBoundary element = new ElementBoundary();
		ElementId elementId = new ElementId();
		elementId.setDomain(userIdManager.getDomain());
		element.setElementId(elementId);
		element.setCreatedBy(this.createdByConverter.fromUserIdToCreatedBy(userIdManager));
		element.setIsActive(true);	
		
		ElementBoundary elementDB = this.restTemplate
			.postForObject(this.url + userIdManager.getDomain() + "/" + userIdManager.getEmail(), 
					element, 
					ElementBoundary.class);
		
		// WHEN player trying to get element's attribute data	
		ActionBoundary action = new ActionBoundary();
		action.setType("GetAllElementAtributeData");
		action.setElement(elementDB.getElementId());
		action.setInvokedBy(userIdPlayer);
		
		
		Object atributeData =this.restTemplate
		.postForObject("http://localhost:" + this.port + "/acs/actions",
				action,
				Object.class);
		
		// THEN he successfully do so
		boolean b = areEqual((Map<String, Object>)atributeData, elementDB.getElementAttributes());
		assertThat(b)
		.isNotNull()
		.isEqualTo(true);				
	}
	

	
	
	private boolean areEqual(Map<String, Object> first, Map<String, Object> second) {
		
	    if (first.size() != second.size()) {
	        return false;
	    }
	 
	    return first.entrySet().stream()
	      .allMatch(e -> e.getValue().equals(second.get(e.getKey())));
	}
	
	
	
}

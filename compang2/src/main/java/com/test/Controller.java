package com.test;

import java.util.Collections;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class Controller {
	
	private MyService myService;

	
	@Autowired
	public Controller(MyService myService) {
		this.myService = myService;
	}
	
	
	@CrossOrigin
    @PostMapping("/marketing/ws/partner/campaign/{id}")
    public void register(
    		@PathVariable("id") Long id,
    		@RequestBody CompangBoundary register_ditales) {
		this.myService.register(id, register_ditales);
    }


	@ExceptionHandler
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	public Map<String, Object> handleException (IncorrectInputExeption e){
		return Collections.singletonMap("error", 
				(e.getMessage() == null)?
						"wrong format for field":
						e.getMessage());
	}
	
	@ExceptionHandler
	@ResponseStatus(code = HttpStatus.CONFLICT)
	public Map<String, Object> handleException (ExistExeption e){
		return Collections.singletonMap("error", 
				(e.getMessage() == null)?
						"compang exist":
						e.getMessage());
	}

}

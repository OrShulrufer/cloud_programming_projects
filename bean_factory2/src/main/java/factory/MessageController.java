package factory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessageController {
    @Autowired
    public ApplicationContext applicationContext;
    
	@RequestMapping("/")
	public String index() {
		Message message = applicationContext.getBean(Message.class);
		return  message.getMessage().toString();
	}
	

}

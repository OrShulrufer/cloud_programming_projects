package factory;


import org.springframework.stereotype.Component;

@Component
public class Message {

    public String getMessage() {
        return "Hello World!";
    }
}
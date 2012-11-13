package examples;

import javax.websocket.annotations.WebSocketEndpoint;
import javax.websocket.annotations.WebSocketMessage;

@WebSocketEndpoint("/hello")
public class MyHelloServer {
    @WebSocketMessage
    public String doListen(String message) {
	return "Got your message (" + message + "). Thanks!";
    }
}

package examples;

import javax.websocket.WebSocketEndpoint;
import javax.websocket.WebSocketMessage;

@WebSocketEndpoint("/hello")
public class MyHelloServer {
    @WebSocketMessage
    public String doListen(String message) {
	return "Got your message (" + message + "). Thanks!";
    }
}

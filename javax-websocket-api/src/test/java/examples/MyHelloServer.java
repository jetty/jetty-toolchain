package examples;

import javax.net.websocket.annotations.WebSocketEndpoint;
import javax.net.websocket.annotations.WebSocketMessage;

@WebSocketEndpoint("/hello")
public class MyHelloServer {
    @WebSocketMessage
    public String doListen(String message) {
	return "Got your message (" + message + "). Thanks!";
    }
}

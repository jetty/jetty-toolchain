package examples;

import javax.net.websocket.Session;
import javax.net.websocket.annotations.WebSocketEndpoint;
import javax.net.websocket.annotations.WebSocketMessage;
import javax.net.websocket.annotations.WebSocketPathParam;

@WebSocketEndpoint("/bookings/{guest-id}")
public class BookingServer {
    @WebSocketMessage
    public void processBookingRequest(
	    @WebSocketPathParam("guest-id") String guestId, String message,
	    Session sesssion) {
	// process booking from the given guest here
    }
}

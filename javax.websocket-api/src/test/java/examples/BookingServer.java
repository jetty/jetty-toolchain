package examples;

import javax.websocket.Session;
import javax.websocket.annotations.WebSocketEndpoint;
import javax.websocket.annotations.WebSocketMessage;
import javax.websocket.annotations.WebSocketPathParam;

@WebSocketEndpoint("/bookings/{guest-id}")
public class BookingServer {
    @WebSocketMessage
    public void processBookingRequest(
	    @WebSocketPathParam("guest-id") String guestId, String message,
	    Session sesssion) {
	// process booking from the given guest here
    }
}

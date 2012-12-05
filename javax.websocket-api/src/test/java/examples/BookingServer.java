package examples;

import javax.websocket.Session;
import javax.websocket.WebSocketEndpoint;
import javax.websocket.WebSocketMessage;
import javax.websocket.WebSocketPathParam;

@WebSocketEndpoint("/bookings/{guest-id}")
public class BookingServer {
    @WebSocketMessage
    public void processBookingRequest(
	    @WebSocketPathParam("guest-id") String guestId, String message,
	    Session sesssion) {
	// process booking from the given guest here
    }
}

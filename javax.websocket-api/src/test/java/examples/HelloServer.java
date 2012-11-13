package examples;

import java.io.IOException;

import javax.websocket.Endpoint;
import javax.websocket.MessageHandler;
import javax.websocket.RemoteEndpoint;
import javax.websocket.Session;

public class HelloServer extends Endpoint {
    @Override
    public void onOpen(Session session) {
	final RemoteEndpoint remote = session.getRemote();
	session.addMessageHandler(new MessageHandler.Text() {
	    @Override
	    public void onMessage(String text) {
		try {
		    remote.sendString("Got your message (" + text
			    + "). Thanks !");
		} catch (IOException ioe) {
		    ioe.printStackTrace();
		}
	    }
	});
    }
}

package examples;

import java.io.IOException;

import javax.net.websocket.Endpoint;
import javax.net.websocket.MessageHandler;
import javax.net.websocket.RemoteEndpoint;
import javax.net.websocket.Session;

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

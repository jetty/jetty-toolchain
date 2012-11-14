package examples;

import java.io.IOException;

import javax.websocket.DefaultServerConfiguration;
import javax.websocket.Endpoint;
import javax.websocket.EndpointConfiguration;
import javax.websocket.MessageHandler;
import javax.websocket.RemoteEndpoint;
import javax.websocket.Session;

public class HelloServer extends Endpoint {
    @Override
    public EndpointConfiguration getEndpointConfiguration() {
	return new DefaultServerConfiguration("/");
    }

    @Override
    public void onOpen(Session session) {
	final RemoteEndpoint remote = session.getRemote();
	session.addMessageHandler(new MessageHandler.Basic<String>() {
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

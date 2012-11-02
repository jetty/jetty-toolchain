package javax.net.websocket.extensions;

import java.util.Map;

/**
 * The Extension interface represents a web socket extension. Extensions are
 * added to a web socket endpoint by adding them to its EndpointConfiguration.
 * The extension consists of a name, a collection of extension parameters and a
 * pair of ExtensionHandlers, one that handles all the frames the web socket
 * implementation uses for representing incoming web socket events and messages,
 * and the other that handles all the frames the web socket implementation uses
 * for representing outgoing web socket events and messages.
 * 
 * @since DRAFT 003
 */
public interface Extension {
    /**
     * The FrameHandler that is invoked for any incoming Frames.
     */
    abstract FrameHandler createIncomingFrameHandler(FrameHandler downstream);

    /**
     * The FrameHandler that is invoked for any outgoing Frames.
     */
    abstract FrameHandler createOutgoingFrameHandler(FrameHandler upstream);

    /**
     * The name of this extension.
     */
    // FIXME: need language about what is an acceptable name syntax
    abstract String getName();

    /**
     * The map name value pairs that are the web socket extension parameters for
     * this extension.
     */
    // FIXME: parameter order is important on permessage-compress
    // FIXME: need clarification of quoting rules for values?
    abstract Map<String, String> getParameters();

}

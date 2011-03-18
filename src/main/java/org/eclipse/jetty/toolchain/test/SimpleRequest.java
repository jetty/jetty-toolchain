package org.eclipse.jetty.toolchain.test;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.UnknownHostException;
import java.util.Properties;


public class SimpleRequest
{
    private URI baseUri;

    public SimpleRequest(URI serverURI) throws UnknownHostException
    {
        this.baseUri = serverURI;
    }

    public String getString(String relativePath) throws IOException
    {
        URI uri = this.baseUri.resolve(relativePath);
        System.out.println("GET (String): " + uri.toASCIIString());

        InputStream in = null;
        InputStreamReader reader = null;
        HttpURLConnection connection = null;

        try
        {
            connection = (HttpURLConnection)uri.toURL().openConnection();
            connection.connect();
            if (HttpURLConnection.HTTP_OK != connection.getResponseCode())
            {
                String body = getPotentialBody(connection);
                String err = String.format("GET request failed (%d %s) %s%n%s",connection.getResponseCode(),connection.getResponseMessage(),
                        uri.toASCIIString(),body);
                throw new IOException(err);
            }
            in = connection.getInputStream();
            reader = new InputStreamReader(in);
            StringWriter writer = new StringWriter();
            IO.copy(reader,writer);
            return writer.toString();
        }
        finally
        {
            IO.close(reader);
            IO.close(in);
        }
    }

    public Properties getProperties(String relativePath) throws IOException
    {
        URI uri = this.baseUri.resolve(relativePath);
        System.out.println("GET (Properties): " + uri.toASCIIString());

        InputStream in = null;
        HttpURLConnection connection = null;

        try
        {
            connection = (HttpURLConnection)uri.toURL().openConnection();
            connection.connect();
            if (HttpURLConnection.HTTP_OK != connection.getResponseCode())
            {
                String body = getPotentialBody(connection);
                String err = String.format("GET request failed (%d %s) %s%n%s",connection.getResponseCode(),connection.getResponseMessage(),
                        uri.toASCIIString(),body);
                throw new IOException(err);
            }
            in = connection.getInputStream();
            Properties props = new Properties();
            props.load(in);
            return props;
        }
        finally
        {
            IO.close(in);
        }
    }

    /**
     * Attempt to obtain the body text if available.
     * Do not throw an exception if body is unable to be fetched.
     * @param connection the connection to fetch the body content from.
     * @return the body content, if present.
     */
    private String getPotentialBody(HttpURLConnection connection)
    {
        InputStream in = null;
        InputStreamReader reader = null;
        try
        {
            in = connection.getInputStream();
            reader = new InputStreamReader(in);
            StringWriter writer = new StringWriter();
            IO.copy(reader,writer);
            return writer.toString();
        }
        catch (IOException e)
        {
            return "<no body:" + e.getMessage() + ">";
        }
        finally
        {
            IO.close(reader);
            IO.close(in);
        }
    }
}

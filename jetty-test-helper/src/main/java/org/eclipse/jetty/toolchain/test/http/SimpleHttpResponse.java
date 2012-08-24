package org.eclipse.jetty.toolchain.test.http;

import java.util.Map;

/**
 * A very simple container for an HttpResponse. Just holding response code, headers and body information.
 */
public class SimpleHttpResponse
{
    private final String code;
    private final Map<String, String> headers;
    private final String body;

    public SimpleHttpResponse(String code, Map<String, String> headers, String body)
    {
        this.code = code;
        this.headers = headers;
        this.body = body;
    }

    public String getCode()
    {
        return code;
    }

    public Map<String, String> getHeaders()
    {
        return headers;
    }

    public String getBody()
    {
        return body;
    }

    @Override
    public String toString()
    {
        return "Response{" +
                "code='" + code + '\'' +
                ", headers=" + headers +
                ", body='" + body + '\'' +
                '}';
    }
}

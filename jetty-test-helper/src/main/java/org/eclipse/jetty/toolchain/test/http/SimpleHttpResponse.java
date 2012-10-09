//
//  ========================================================================
//  Copyright (c) 1995-2012 Mort Bay Consulting Pty. Ltd.
//  ------------------------------------------------------------------------
//  All rights reserved. This program and the accompanying materials
//  are made available under the terms of the Eclipse Public License v1.0
//  and Apache License v2.0 which accompanies this distribution.
//
//      The Eclipse Public License is available at
//      http://www.eclipse.org/legal/epl-v10.html
//
//      The Apache License v2.0 is available at
//      http://www.opensource.org/licenses/apache2.0.php
//
//  You may elect to redistribute this code under either of these licenses.
//  ========================================================================
//

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

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

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A simple http parser implementation to be used in tests. It's feed with a BufferedReader and will read from that
 * reader until the http response has been fully read.
 *
 * It returns a {@link SimpleHttpResponse} object that grants access to the response code,
 * headers and body of the parsed response.
 *
 * This class can easily be extended to provide more features. However keep in mind that it's primary focus is to
 * keep parsing as simple as possible.
 */
public class SimpleHttpParser
{
    /**
     * Reads from the given {@link BufferedReader} and returns the parsed response in a {@link SimpleHttpResponse}
     * object.
     *
     * @param reader the inputReader to parse the response from
     * @return {@link SimpleHttpResponse}   a {@link SimpleHttpResponse} object representing the parsed response
     * @throws IOException
     */
    public SimpleHttpResponse readResponse(BufferedReader reader) throws IOException
    {
        // Simplified parser for HTTP responses
        String line = reader.readLine();
        if (line == null)
            throw new EOFException();
        Matcher responseLine = Pattern.compile("HTTP/1.1" + "\\s+(\\d+)").matcher(line);
        assertThat("http version is 1.1", responseLine.lookingAt(), is(true));
        String code = responseLine.group(1);

        Map<String, String> headers = new LinkedHashMap<String, String>();
        while ((line = reader.readLine()) != null)
        {
            if (line.trim().length() == 0)
                break;

            parseHeader(line, headers);
        }

        StringBuilder body;
        if (headers.containsKey("content-length"))
        {
            body = parseContentLengthDelimitedBody(reader, headers);
        }
        else if ("chunked".equals(headers.get("transfer-encoding")))
        {
            body = parseChunkedBody(reader);
        }
        else
        {
            body = parseEOFDelimitedBody(reader, headers);
        }

        return new SimpleHttpResponse(code, headers, body.toString().trim());
    }

    private void parseHeader(String line, Map<String, String> headers)
    {
        Matcher header = Pattern.compile("([^:]+):\\s*(.*)").matcher(line);
        assertTrue(header.lookingAt());
        String headerName = header.group(1);
        String headerValue = header.group(2);
        headers.put(headerName.toLowerCase(), headerValue.toLowerCase());
    }

    private StringBuilder parseContentLengthDelimitedBody(BufferedReader reader, Map<String, String> headers) throws IOException
    {
        StringBuilder body;
        int readLen = 0;
        int length = Integer.parseInt(headers.get("content-length"));
        body = new StringBuilder(length);
        try
        {
            //TODO: UTF-8 reader from joakim
            for (int i = 0; i < length; ++i)
            {
                char c = (char)reader.read();
                body.append(c);
                readLen++;
            }

        }
        catch (SocketTimeoutException e)
        {
            System.err.printf("Read %,d bytes (out of an expected %,d bytes)%n", readLen, length);
            throw e;
        }
        return body;
    }

    private StringBuilder parseChunkedBody(BufferedReader reader) throws IOException
    {
        StringBuilder body;
        String line;
        body = new StringBuilder(64 * 1024);
        while ((line = reader.readLine()) != null)
        {
            if ("0".equals(line))
            {
                line = reader.readLine();
                assertThat("There's no more content after as 0 indicated the final chunk", line, is(""));
                break;
            }

            int length = Integer.parseInt(line, 16);
            //TODO: UTF-8 reader from joakim
            for (int i = 0; i < length; ++i)
            {
                char c = (char)reader.read();
                body.append(c);
            }
            reader.readLine();
            // assertThat("chunk is followed by an empty line", line, is("")); //TODO: is this right? - NO.  Don't
            // think you can really do chunks with read line generally, but maybe for this test is OK.
        }
        return body;
    }

    private StringBuilder parseEOFDelimitedBody(BufferedReader reader, Map<String, String> headers) throws IOException
    {
        StringBuilder body;

        // read until EOF
        body = new StringBuilder();
        while (true)
        {
            //TODO: UTF-8 reader from joakim
            int read = reader.read();
            if (read == -1)
                break;
            char c = (char)read;
            body.append(c);
        }
        return body;
    }

}

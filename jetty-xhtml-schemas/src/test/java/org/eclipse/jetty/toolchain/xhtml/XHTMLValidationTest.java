//
// ========================================================================
// Copyright (c) 1995 Mort Bay Consulting Pty Ltd and others.
//
// This program and the accompanying materials are made available under the
// terms of the Eclipse Public License v. 2.0 which is available at
// https://www.eclipse.org/legal/epl-2.0, or the Apache License, Version 2.0
// which is available at https://www.apache.org/licenses/LICENSE-2.0.
//
// SPDX-License-Identifier: EPL-2.0 OR Apache-2.0
// ========================================================================
//

package org.eclipse.jetty.toolchain.xhtml;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import javax.xml.catalog.Catalog;
import javax.xml.catalog.CatalogManager;
import javax.xml.catalog.CatalogResolver;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class XHTMLValidationTest
{
    @Test
    public void testXhtmlValidation()
    {
        String xhtml = """
            <?xml version="1.0" encoding="utf-8"?>
            <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
            <html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
              <head>
                <title>Test XHTML</title>
              </head>
              <body>
                <p>
                  Let's use some HTML named entities.
                  &nbsp;
                </p>
                <p>
                  What about <a href="/foo?a=b&amp;c=d">link with query params</a>
                </p>
              </body>
            </html>
            """;
        assertTrue(isValidXHtml(xhtml));
    }

    private static boolean isValidXHtml(String content)
    {
        // we expect that our generated output conforms to text/xhtml is well formed
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8)))
        {
            Catalog catalog = CatalogXHTML.getCatalog();
            CatalogResolver resolver = CatalogManager.catalogResolver(catalog);

            DocumentBuilderFactory xmlDocumentBuilderFactory = DocumentBuilderFactory.newInstance();
            xmlDocumentBuilderFactory.setValidating(true);
            DocumentBuilder db = xmlDocumentBuilderFactory.newDocumentBuilder();
            db.setEntityResolver(resolver);
            List<SAXParseException> errors = new ArrayList<>();
            db.setErrorHandler(new ErrorHandler()
            {
                @Override
                public void warning(SAXParseException exception)
                {
                    exception.printStackTrace();
                }

                @Override
                public void error(SAXParseException exception)
                {
                    errors.add(exception);
                }

                @Override
                public void fatalError(SAXParseException exception)
                {
                    errors.add(exception);
                }
            });

            // We consider this content to be XML well-formed if these 2 lines do not throw an Exception
            Document doc = db.parse(inputStream);
            doc.getDocumentElement().normalize();

            if (errors.size() > 0)
            {
                IOException ioException = new IOException("Failed to validate XHTML");
                for (SAXException saxException : errors)
                {
                    ioException.addSuppressed(saxException);
                }
                fail(ioException);
            }

            return true; // it's well-formed
        }
        catch (IOException | ParserConfigurationException | SAXException e)
        {
            e.printStackTrace(System.err);
            return false; // XHTML has got issues
        }
    }
}

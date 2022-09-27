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

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import javax.xml.catalog.Catalog;
import javax.xml.catalog.CatalogFeatures;
import javax.xml.catalog.CatalogManager;

public final class CatalogXHTML
{
    private static final String CATALOG_XHTML_XML = "catalog-xhtml.xml";

    public static URI getURI()
    {
        URL url = CatalogXHTML.class.getResource(CATALOG_XHTML_XML);
        if (url == null)
            throw new IllegalStateException("Unable to find: " + CATALOG_XHTML_XML);
        try
        {
            return url.toURI();
        }
        catch (URISyntaxException e)
        {
            throw new IllegalStateException("Unable to convert URL to URI", e);
        }
    }

    public static Catalog getCatalog()
    {
        CatalogFeatures f = CatalogFeatures.builder().with(CatalogFeatures.Feature.RESOLVE, "continue").build();
        return CatalogManager.catalog(f, CatalogXHTML.getURI());
    }
}

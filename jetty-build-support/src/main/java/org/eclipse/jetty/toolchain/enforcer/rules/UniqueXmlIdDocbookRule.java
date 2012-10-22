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

package org.eclipse.jetty.toolchain.enforcer.rules;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.maven.enforcer.rule.api.EnforcerRule;
import org.apache.maven.enforcer.rule.api.EnforcerRuleException;
import org.apache.maven.enforcer.rule.api.EnforcerRuleHelper;
import org.codehaus.plexus.util.FileUtils;

/**
 * Validates that the xml:id namespace is unique across all docbook xml files
 *
 */
public class UniqueXmlIdDocbookRule implements EnforcerRule
{
    private Map<String,List<XmlId>> idMap = new HashMap<String,List<XmlId>>();
    
    /**
     * Simple param. This rule will fail if the value is true.
     */
    private boolean shouldIfail = false;

    public void execute(EnforcerRuleHelper helper) throws EnforcerRuleException
    {                
        try
        {
            Pattern pattern = Pattern.compile(".*?(xml:id=)\"(.*?)\"", Pattern.DOTALL);
                       
            @SuppressWarnings("unchecked")
            List<File> files = FileUtils.getFiles(new File("src/docbkx"),"**/*.xml","");
                        
            for ( File file : files )
            {                
                Scanner scanner = new Scanner(file).useDelimiter("\n");
                int lineNumber = 0;
                            
                Matcher matcher = null;
                while ( scanner.hasNextLine())
                {
                    String line = scanner.nextLine();
                    ++lineNumber;
                    matcher = pattern.matcher(line);
                    
                    if ( matcher.find() )
                    {
                        String id = matcher.group(2);
                        
                        if ( !idMap.containsKey(id) )
                        {
                            List<XmlId> idlist = new ArrayList<XmlId>();
                            idlist.add(new XmlId(file.getAbsolutePath(),lineNumber));
                            idMap.put(id,idlist);
                        }
                        else
                        {
                            List<XmlId> idlist = idMap.get(id);
                            idlist.add(new XmlId(file.getAbsolutePath(),lineNumber));
                            idMap.put(id,idlist);
                            shouldIfail = true;
                        }                    
                    }               
                }
                
                scanner.close();
            }
            
            if (this.shouldIfail)
            {
                for ( String id : idMap.keySet() )
                {
                    List<XmlId> idList = idMap.get(id);
                    
                    if (idList.size() > 1)
                    {
                        System.out.println("Duplicate Global Id: " + id);
                        for ( XmlId xid : idList )
                        {
                            System.out.println(" - " + xid.getLineNumber() + ": " + xid.getFilename() );
                        }
                    }
                }
                
                throw new EnforcerRuleException("Duplicate xml:id values found in global namespace.");
            }
        }
        catch (IOException e)
        {
            throw new EnforcerRuleException("EnforcerIOException", e);
        }
    }

    public String getCacheId()
    {
        return "" + shouldIfail;
    }

    public boolean isCacheable()
    {
        return true;
    }

    public boolean isResultValid(EnforcerRule arg0)
    {
        return true;
    }
    
    private class XmlId
    {
        private String filename;
        private int lineNumber;
        
        public XmlId(String filename, int lineNumber)
        {
            this.filename = filename;
            this.lineNumber = lineNumber;
        }

        public String getFilename()
        {
            return filename;
        }

        public int getLineNumber()
        {
            return lineNumber;
        }
        
        
    }
    
    
}
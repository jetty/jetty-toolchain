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

package org.eclipse.jetty.xslt.tools;

import japa.parser.JavaParser;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.visitor.VoidVisitorAdapter;

import java.net.URL;

public class JavaSourceFetchExtension
{
    
    public static String fetch( String location ) throws Exception
    {        
        System.out.println( "fetch() with one parameter ");

        String[] bits  = location.split(",");
        
        if ( bits.length == 1 )
        {
            URL url = new URL(bits[0]);

            // String source = IO.toString(url.openStream());

            CompilationUnit cu = JavaParser.parse(url.openStream());
            
            return cu.toString();
        }
        else if ( bits.length == 2 )
        {

            URL url = new URL(bits[0]);

            // String source = IO.toString(url.openStream());

            CompilationUnit cu = JavaParser.parse(url.openStream());

            // visit and print the methods names
            MethodVisitor mv = new MethodVisitor(bits[1]);

            mv.visit(cu,null);

            return mv.source;
        }
        else
        {
            return "SourceFetchExtension broke!";
        }
    }
    
    public static String fetch( String location, String method ) throws Exception
    {        
                
        if ( method == null || "".equals(method) )
        {
            System.out.println("fetching " + location.trim());
            
            URL url = new URL(location);

            // String source = IO.toString(url.openStream());

            CompilationUnit cu = JavaParser.parse(url.openStream());
            
            return cu.toString();
        }
        else
        {
            System.out.println("fetching " + location.trim() + " / " + method.trim() );

            URL url = new URL(location);

            // String source = IO.toString(url.openStream());

            CompilationUnit cu = JavaParser.parse(url.openStream());

            // visit and print the methods names
            MethodVisitor mv = new MethodVisitor(method);

            mv.visit(cu,null);

            return mv.source;
        }
       
    }
    
    /**
     * Simple visitor implementation for visiting MethodDeclaration nodes. 
     */
    private static class MethodVisitor extends VoidVisitorAdapter {

        String methodName;
        String source;
        
        MethodVisitor( String methodName )
        {
            this.methodName = methodName;
        }
        
        @Override
        public void visit(MethodDeclaration n, Object arg) {
            
            if ( methodName.equals( n.getName() ) )
            {
                source = n.toString();
               // System.out.println(n.getName());
               // System.out.println( n.getBody() );
            }
        }
    }
    
}

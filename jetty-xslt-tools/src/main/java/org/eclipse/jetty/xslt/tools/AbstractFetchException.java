package org.eclipse.jetty.xslt.tools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.StringReader;

import org.eclipse.jetty.toolchain.test.FS;
import org.eclipse.jetty.toolchain.test.MavenTestingUtils;
import org.eclipse.jetty.util.IO;

public class AbstractFetchException
{

    private static File _fetchCache;

    static
    {
        _fetchCache = MavenTestingUtils.getTargetTestingDir("fetch-cache");
        _fetchCache.mkdirs();
    }
    
    protected static File checkCache(String resource)
    {
        String original = resource;
        
        File test = new File(_fetchCache, mangle(resource));
        
        if ( test.exists() )
        {
            System.out.println("In Cache: " + original );
            return test;
        }
        else
        {
            System.out.println("Not Cached: " + original );
            return null;
        }
    }  
    
    protected static File cache(String filename, InputStream stream) throws Exception
    {
        File toCache = new File(_fetchCache, mangle(filename));
        
        //System.out.println(toCache.getAbsolutePath());
        
        FS.touch(toCache);
        
        IO.copy(stream,new FileOutputStream(toCache));
        
        return toCache;
    }
    
    protected static String mangle(String toMangle)
    {
        toMangle = toMangle.replace(":","_");
        toMangle = toMangle.replace("/","_");

        return toMangle;
    }
}
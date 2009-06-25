package org.eclipse.jetty.toolchain.test.policy;

import java.security.AccessController;
import java.security.PrivilegedAction;

public class Tester
{
    
    public Tester()
    {
        
    }
    

    public String testEcho( String echo )
    {
        return echo;
    }
    
    public boolean testReadSystemProperty( String property ) throws Exception
    {
        AccessController.doPrivileged( new TestAction( property ) );
       
        return true;
    }
    
    class TestAction implements PrivilegedAction
    {
        private String _value;
        
        TestAction ( String value )
        {
            _value = value;
        }
        
        public Object run()
        {  
            System.out.println( "checking out system property -=> " + _value );
            
            return System.getProperty( _value ); 
        }
    }
    
}

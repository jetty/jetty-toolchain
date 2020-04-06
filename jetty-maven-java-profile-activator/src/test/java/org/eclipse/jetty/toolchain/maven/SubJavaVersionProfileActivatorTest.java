
package org.eclipse.jetty.toolchain.maven;

import org.apache.maven.model.Activation;
import org.apache.maven.model.Profile;
import org.apache.maven.model.profile.DefaultProfileActivationContext;
import org.apache.maven.model.profile.ProfileActivationContext;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class SubJavaVersionProfileActivatorTest
{
    @Test
    public void notActivatedSubJavaVersion4digitsRangeAndVersion()
    {
        SubJavaVersionProfileActivator activator = new SubJavaVersionProfileActivator();
        Profile profile = new Profile();
        profile.setId( "test" );
        profile.setActivation( new Activation(){
            @Override
            public String getJdk()
            {
                return "[1.8,1.8.0_200)";
            }
        } );

        ProfileActivationContext context = new DefaultProfileActivationContext(){
            @Override
            public Map<String, String> getSystemProperties()
            {
                Map<String,String> props = new HashMap<>();
                props.put( "java.version", "1.8.0_222-b10" );
                return props;
            }
        };

        boolean activated = activator.isActive( profile, context, null);
        assertThat("Activated", activated, is(false));
    }

    @Test
    public void activatedSubJavaVersion4digitsRangeAndVersion()
    {
        SubJavaVersionProfileActivator activator = new SubJavaVersionProfileActivator();
        Profile profile = new Profile();
        profile.setId( "test" );
        profile.setActivation( new Activation(){
            @Override
            public String getJdk()
            {
                return "[1.8,1.8.0_200)";
            }
        } );

        ProfileActivationContext context = new DefaultProfileActivationContext(){
            @Override
            public Map<String, String> getSystemProperties()
            {
                Map<String,String> props = new HashMap<>();
                props.put( "java.version", "1.8.0_199-b24" );
                return props;
            }
        };

        boolean activated = activator.isActive( profile, context, null);
        assertThat("Not Activated", activated, is(true));
    }

    @Test
    public void notActivatedSubJavaVersion2digitsRange()
    {
        SubJavaVersionProfileActivator activator = new SubJavaVersionProfileActivator();
        Profile profile = new Profile();
        profile.setId( "test" );
        profile.setActivation( new Activation(){
            @Override
            public String getJdk()
            {
                return "[1.9,)";
            }
        } );

        ProfileActivationContext context = new DefaultProfileActivationContext(){
            @Override
            public Map<String, String> getSystemProperties()
            {
                Map<String,String> props = new HashMap<>();
                props.put( "java.version", "1.8.0_222-b10" );
                return props;
            }
        };

        boolean activated = activator.isActive( profile, context, null);
        assertThat("Activated", activated, is(false));
    }

    @Test
    public void notActivatedSubJavaVersion4digitsRangeAnd2digitsVersion()
    {
        SubJavaVersionProfileActivator activator = new SubJavaVersionProfileActivator();
        Profile profile = new Profile();
        profile.setId( "test" );
        profile.setActivation( new Activation(){
            @Override
            public String getJdk()
            {
                return "[1.8,1.8.0_200)";
            }
        } );

        ProfileActivationContext context = new DefaultProfileActivationContext(){
            @Override
            public Map<String, String> getSystemProperties()
            {
                Map<String,String> props = new HashMap<>();
                props.put( "java.version", "1.8" );
                return props;
            }
        };

        boolean activated = activator.isActive( profile, context, null);
        assertThat("Not Activated", activated, is(true));
    }

    @Test
    public void notActivatedSubJavaVersion4digitsRangeMinAnd2digitsVersion()
    {
        SubJavaVersionProfileActivator activator = new SubJavaVersionProfileActivator();
        Profile profile = new Profile();
        profile.setId( "test" );
        profile.setActivation( new Activation(){
            @Override
            public String getJdk()
            {
                return "[1.8.0_200,)";
            }
        } );

        ProfileActivationContext context = new DefaultProfileActivationContext(){
            @Override
            public Map<String, String> getSystemProperties()
            {
                Map<String,String> props = new HashMap<>();
                props.put( "java.version", "1.8" );
                return props;
            }
        };

        boolean activated = activator.isActive( profile, context, null);
        assertThat("Activated", activated, is(false));
    }

    @Test
    public void activatedSubJavaVersion4digitsRangeMinAnd2digitsVersion()
    {
        SubJavaVersionProfileActivator activator = new SubJavaVersionProfileActivator();
        Profile profile = new Profile();
        profile.setId( "test" );
        profile.setActivation( new Activation(){
            @Override
            public String getJdk()
            {
                return "[1.8.0_200,)";
            }
        } );

        ProfileActivationContext context = new DefaultProfileActivationContext(){
            @Override
            public Map<String, String> getSystemProperties()
            {
                Map<String,String> props = new HashMap<>();
                props.put( "java.version", "1.9" );
                return props;
            }
        };

        boolean activated = activator.isActive( profile, context, null);
        assertThat("Activated", activated, is(true));
    }

    @Test
    public void activatedSubJavaVersion4digitsRangeMinAnd4digitsVersion()
    {
        SubJavaVersionProfileActivator activator = new SubJavaVersionProfileActivator();
        Profile profile = new Profile();
        profile.setId( "test" );
        profile.setActivation( new Activation(){
            @Override
            public String getJdk()
            {
                return "[1.8.0_200,)";
            }
        } );

        ProfileActivationContext context = new DefaultProfileActivationContext(){
            @Override
            public Map<String, String> getSystemProperties()
            {
                Map<String,String> props = new HashMap<>();
                props.put( "java.version", "1.8.0_201" );
                return props;
            }
        };

        boolean activated = activator.isActive( profile, context, null);
        assertThat("Not Activated", activated, is(true));
    }

    @Test
    public void notActivatedSubJavaVersion4digitsRangeMinAnd4digitsVersion()
    {
        SubJavaVersionProfileActivator activator = new SubJavaVersionProfileActivator();
        Profile profile = new Profile();
        profile.setId( "test" );
        profile.setActivation( new Activation(){
            @Override
            public String getJdk()
            {
                return "[1.8.0_200,)";
            }
        } );

        ProfileActivationContext context = new DefaultProfileActivationContext(){
            @Override
            public Map<String, String> getSystemProperties()
            {
                Map<String,String> props = new HashMap<>();
                props.put( "java.version", "1.8.0_199" );
                return props;
            }
        };

        boolean activated = activator.isActive( profile, context, null);
        assertThat("Activated", activated, is(false));
    }
}

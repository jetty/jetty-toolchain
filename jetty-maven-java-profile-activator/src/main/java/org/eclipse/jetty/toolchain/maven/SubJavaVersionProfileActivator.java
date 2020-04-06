package org.eclipse.jetty.toolchain.maven;

import org.apache.maven.model.Activation;
import org.apache.maven.model.Profile;
import org.apache.maven.model.building.ModelProblem;
import org.apache.maven.model.building.ModelProblemCollector;
import org.apache.maven.model.building.ModelProblemCollectorRequest;
import org.apache.maven.model.profile.ProfileActivationContext;
import org.apache.maven.model.profile.activation.ProfileActivator;
import org.codehaus.plexus.component.annotations.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component(role = ProfileActivator.class, hint = "jetty-sub-java-version-profiles")
/**
 * it's a copy of Maven Core class JdkVersionProfileActivator except sub version are managed
 * such 1.8.0_222
 */
public class SubJavaVersionProfileActivator
    implements ProfileActivator
{
    @Override
    public boolean isActive( Profile profile, ProfileActivationContext context, ModelProblemCollector problems )
    {
        Activation activation = profile.getActivation();

        if ( activation == null )
        {
            return false;
        }

        String jdk = activation.getJdk();

        if ( jdk == null )
        {
            return false;
        }

        String version = context.getSystemProperties().get( "java.version" );

        if ( version == null || version.length() <= 0 )
        {
            problems.add( new ModelProblemCollectorRequest( ModelProblem.Severity.ERROR, ModelProblem.Version.BASE )
                              .setMessage( "Failed to determine Java version for profile " + profile.getId() )
                              .setLocation( activation.getLocation( "jdk" ) ) );
            return false;
        }

        if ( jdk.startsWith( "!" ) )
        {
            return !version.startsWith( jdk.substring( 1 ) );
        }
        else if ( isRange( jdk ) )
        {
            return isInRange( version, getRange( jdk ) );
        }
        else
        {
            return version.startsWith( jdk );
        }
    }

    @Override
    public boolean presentInConfig( Profile profile, ProfileActivationContext context, ModelProblemCollector problems )
    {
        Activation activation = profile.getActivation();

        if ( activation == null )
        {
            return false;
        }

        String jdk = activation.getJdk();

        if ( jdk == null )
        {
            return false;
        }
        return true;
    }

    private static boolean isInRange( String value, List<RangeValue> range )
    {
        int leftRelation = getRelationOrder( value, range.get( 0 ), true );

        if ( leftRelation == 0 )
        {
            return true;
        }

        if ( leftRelation < 0 )
        {
            return false;
        }

        return getRelationOrder( value, range.get( 1 ), false ) <= 0;
    }

    private static int getRelationOrder( String value, RangeValue rangeValue, boolean isLeft )
    {
        if ( rangeValue.value.length() <= 0 )
        {
            return isLeft ? 1 : -1;
        }

        value = value.replaceAll( "[^0-9\\.\\-\\_]", "" );
        rangeValue.value = rangeValue.value.replaceAll( "[^0-9\\.\\-\\_]", "" );

        List<String> valueTokens = new ArrayList<>( Arrays.asList( value.split( "[\\.\\-\\_]" ) ) );
        List<String> rangeValueTokens = new ArrayList<>( Arrays.asList( rangeValue.value.split( "[\\.\\-\\_]" ) ) );

        // we want to manage such version 1.8.0_199-b24
        addZeroTokens( valueTokens, 4 );
        addZeroTokens( rangeValueTokens, 4 );

        int digits = Math.min( rangeValueTokens.size(), valueTokens.size() );
        //int rangeValueMaxDigits = Math.min( 4, rangeValueTokens.size() );

        for ( int i = 0; i < digits; i++ )
        {
            int x = Integer.parseInt( valueTokens.get( i ) );
            int y = Integer.parseInt( rangeValueTokens.get( i ) );
            if ( x < y )
            {
                return -1;
            }
            else if ( x > y )
            {
                return 1;
            }
        }
        if ( !rangeValue.closed )
        {
            return isLeft ? -1 : 1;
        }
        return 0;
    }

    private static void addZeroTokens( List<String> tokens, int max )
    {
        while ( tokens.size() < max )
        {
            tokens.add( "0" );
        }
    }

    private static boolean isRange( String value )
    {
        return value.startsWith( "[" ) || value.startsWith( "(" );
    }

    private static List<RangeValue> getRange( String range )
    {
        List<RangeValue> ranges = new ArrayList<>();

        for ( String token : range.split( "," ) )
        {
            if ( token.startsWith( "[" ) )
            {
                ranges.add( new RangeValue( token.replace( "[", "" ), true ) );
            }
            else if ( token.startsWith( "(" ) )
            {
                ranges.add( new RangeValue( token.replace( "(", "" ), false ) );
            }
            else if ( token.endsWith( "]" ) )
            {
                ranges.add( new RangeValue( token.replace( "]", "" ), true ) );
            }
            else if ( token.endsWith( ")" ) )
            {
                ranges.add( new RangeValue( token.replace( ")", "" ), false ) );
            }
            else if ( token.length() <= 0 )
            {
                ranges.add( new RangeValue( "", false ) );
            }
        }
        if ( ranges.size() < 2 )
        {
            ranges.add( new RangeValue( "99999999", false ) );
        }
        return ranges;
    }

    private static class RangeValue
    {
        private String value;

        private boolean closed;

        RangeValue( String value, boolean closed )
        {
            this.value = value.trim();
            this.closed = closed;
        }

        @Override
        public String toString()
        {
            return value;
        }
    }

}

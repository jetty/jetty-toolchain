package org.eclipse.jetty.toolchain.test;

import static org.hamcrest.CoreMatchers.*;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class JettyDistroTest
{

    @Test
    public void testSplitAndUnescapeCommandLine()
    {
        StringBuilder rawCmdLine = new StringBuilder();
        rawCmdLine.append("/usr/lib/jvm/jdk1.6.0_27/jre/bin/java ");
        rawCmdLine.append("\"-Djetty.home=/home/joakim/code/intalio/jetty (7)/tests/test-integration/target/tests/oejtm.JmxServiceTest/jettyHome\" ");
        rawCmdLine.append("-cp /home/joakim/code/intalio/jetty\\ (7)/tests/test-integration/target/tests/oejtm.JmxServiceTest/jettyHome/lib/");
        rawCmdLine.append("jetty-xml-7.6.1-SNAPSHOT.jar:/home/joakim/code/intalio/jetty\\ (7)/tests/test-integration/target/tests/");
        rawCmdLine.append("oejtm.JmxServiceTest/jettyHome/lib/servlet-api-2.5.jar:/home/joakim/code/intalio/jetty\\ (7)/tests/");
        rawCmdLine.append("test-integration/target/tests/oejtm.JmxServiceTest/jettyHome/lib/jetty-http-7.6.1-SNAPSHOT.jar:");
        rawCmdLine.append("/home/joakim/code/intalio/jetty\\ (7)/tests/test-integration/target/tests/oejtm.JmxServiceTest/");
        rawCmdLine.append("jettyHome/lib/jetty-continuation-7.6.1-SNAPSHOT.jar:/home/joakim/code/intalio/jetty\\ (7)/tests/");
        rawCmdLine.append("test-integration/target/tests/oejtm.JmxServiceTest/jettyHome/lib/jetty-server-7.6.1-SNAPSHOT.jar:");
        rawCmdLine.append("/home/joakim/code/intalio/jetty\\ (7)/tests/test-integration/target/tests/oejtm.JmxServiceTest/jettyHome/");
        rawCmdLine.append("lib/jetty-security-7.6.1-SNAPSHOT.jar:/home/joakim/code/intalio/jetty\\ (7)/tests/test-integration/target/tests/");
        rawCmdLine.append("oejtm.JmxServiceTest/jettyHome/lib/jetty-servlet-7.6.1-SNAPSHOT.jar:/home/joakim/code/intalio/jetty\\ (7)/tests/");
        rawCmdLine.append("test-integration/target/tests/oejtm.JmxServiceTest/jettyHome/lib/jetty-webapp-7.6.1-SNAPSHOT.jar:/home/joakim/code/");
        rawCmdLine.append("intalio/jetty\\ (7)/tests/test-integration/target/tests/oejtm.JmxServiceTest/jettyHome/lib/");
        rawCmdLine.append("jetty-deploy-7.6.1-SNAPSHOT.jar:/home/joakim/code/intalio/jetty\\ (7)/tests/test-integration/target/tests/");
        rawCmdLine.append("oejtm.JmxServiceTest/jettyHome/lib/jetty-servlets-7.6.1-SNAPSHOT.jar:/home/joakim/code/intalio/jetty\\ (7)/tests/");
        rawCmdLine.append("test-integration/target/tests/oejtm.JmxServiceTest/jettyHome/lib/jetty-jmx-7.6.1-SNAPSHOT.jar:/home/joakim/code/");
        rawCmdLine.append("intalio/jetty\\ (7)/tests/test-integration/target/tests/oejtm.JmxServiceTest/jettyHome/lib/jsp/");
        rawCmdLine.append("com.sun.el_1.0.0.v201004190952.jar:/home/joakim/code/intalio/jetty\\ (7)/tests/test-integration/target/tests/");
        rawCmdLine.append("oejtm.JmxServiceTest/jettyHome/lib/jsp/ecj-3.6.jar:/home/joakim/code/intalio/jetty\\ (7)/tests/test-integration/");
        rawCmdLine.append("target/tests/oejtm.JmxServiceTest/jettyHome/lib/jsp/javax.el_2.1.0.v201004190952.jar:/home/joakim/code/intalio/");
        rawCmdLine.append("jetty\\ (7)/tests/test-integration/target/tests/oejtm.JmxServiceTest/jettyHome/lib/jsp/");
        rawCmdLine.append("javax.servlet.jsp_2.1.0.v201004190952.jar:/home/joakim/code/intalio/jetty\\ (7)/tests/test-integration/target/tests/");
        rawCmdLine.append("oejtm.JmxServiceTest/jettyHome/lib/jsp/javax.servlet.jsp.jstl_1.2.0.v201004190952.jar:/home/joakim/code/intalio/");
        rawCmdLine.append("jetty\\ (7)/tests/test-integration/target/tests/oejtm.JmxServiceTest/jettyHome/lib/jsp/");
        rawCmdLine.append("org.apache.jasper.glassfish_2.1.0.v201110031002.jar:/home/joakim/code/intalio/jetty\\ (7)/tests/test-integration/");
        rawCmdLine.append("target/tests/oejtm.JmxServiceTest/jettyHome/lib/jsp/org.apache.taglibs.standard.glassfish_1.2.0.v201004190952.jar:");
        rawCmdLine.append("/home/joakim/code/intalio/jetty\\ (7)/tests/test-integration/target/tests/oejtm.JmxServiceTest/jettyHome/resources:");
        rawCmdLine.append("/home/joakim/code/intalio/jetty\\ (7)/tests/test-integration/target/tests/oejtm.JmxServiceTest/jettyHome/lib/");
        rawCmdLine.append("jetty-websocket-7.6.1-SNAPSHOT.jar:/home/joakim/code/intalio/jetty\\ (7)/tests/test-integration/target/tests/");
        rawCmdLine.append("oejtm.JmxServiceTest/jettyHome/lib/jetty-util-7.6.1-SNAPSHOT.jar:/home/joakim/code/intalio/jetty\\ (7)/tests/");
        rawCmdLine.append("test-integration/target/tests/oejtm.JmxServiceTest/jettyHome/lib/jetty-io-7.6.1-SNAPSHOT.jar ");
        rawCmdLine.append("org.eclipse.jetty.xml.XmlConfiguration /tmp/start6880437372100559823.properties /home/joakim/code/intalio/");
        rawCmdLine.append("jetty\\ (7)/tests/test-integration/target/tests/oejtm.JmxServiceTest/jettyHome/etc/jetty-jmx.xml /home/joakim/");
        rawCmdLine.append("code/intalio/jetty\\ (7)/tests/test-integration/target/tests/oejtm.JmxServiceTest/jettyHome/etc/jetty.xml /home/");
        rawCmdLine.append("joakim/code/intalio/jetty\\ (7)/tests/test-integration/target/tests/oejtm.JmxServiceTest/jettyHome/etc/");
        rawCmdLine.append("jetty-deploy.xml /home/joakim/code/intalio/jetty\\ (7)/tests/test-integration/target/tests/oejtm.JmxServiceTest/");
        rawCmdLine.append("jettyHome/etc/jetty-webapps.xml /home/joakim/code/intalio/jetty\\ (7)/tests/test-integration/target/tests/");
        rawCmdLine.append("oejtm.JmxServiceTest/jettyHome/etc/jetty-contexts.xml /home/joakim/code/intalio/jetty\\ (7)\\ \\\"testing\\\"/tests/test-integration/");
        rawCmdLine.append("target/tests/oejtm.JmxServiceTest/jettyHome/etc/jetty-testrealm.xml");

        List<String> commands = JettyDistro.splitAndUnescapeCommandLine(rawCmdLine.toString());

        for (int i = 0; i < commands.size(); i++)
        {
            // System.out.printf("[%2d] %s%n",i,commands.get(i));
        }

        Assert.assertThat("Commands.size",commands.size(),is(12));
        
        Assert.assertThat("commands[2]", commands.get(2), is("-cp"));
        Assert.assertThat("commands[4]", commands.get(4), is("org.eclipse.jetty.xml.XmlConfiguration"));
        Assert.assertThat("commands[7]", commands.get(7), endsWith("etc/jetty.xml"));
        Assert.assertThat("commands[10]", commands.get(10), is("/home/joakim/code/intalio/jetty (7)/tests/test-integration/target/tests/oejtm.JmxServiceTest/jettyHome/etc/jetty-contexts.xml"));
    }

}

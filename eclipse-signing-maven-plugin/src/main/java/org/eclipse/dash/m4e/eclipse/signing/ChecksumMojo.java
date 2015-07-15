package org.eclipse.dash.m4e.eclipse.signing;

//========================================================================
//Copyright (c) 2010-2015 Intalio, Inc.
//------------------------------------------------------------------------
//All rights reserved. This program and the accompanying materials
//are made available under the terms of the Eclipse Public License v1.0
//and Apache License v2.0 which accompanies this distribution.
//The Eclipse Public License is available at 
//http://www.eclipse.org/legal/epl-v10.html
//The Apache License v2.0 is available at
//http://www.opensource.org/licenses/apache2.0.php
//You may elect to redistribute this code under either of these licenses. 
//========================================================================

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.MessageDigest;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.archiver.zip.ZipArchiver;
import org.codehaus.plexus.archiver.zip.ZipUnArchiver;
import org.codehaus.plexus.util.FileUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * This plugin walks through a p2 repository and repairs the artifacts.xml.
 * <p>
 * It also insert the property <code>&lt;property name='publishPackFilesAsSiblings' value='true'/&gt;</code>
 * and the mapping rules if they were not present already:
 * </p>
 * <code>&lt;rule filter='(&amp; (classifier=osgi.bundle) (format=packed))'
 *    output='${repoUrl}/plugins/${id}_${version}.jar.pack.gz'/&gt;</code>
 * <code>&lt;rule filter='(&amp; (classifier=org.eclipse.update.feature) (format=packed))'
 *    output='${repoUrl}/features/${id}_${version}.jar.pack.gz'/&gt;</code>
 * @goal fixCheckSums
 * @phase package
 * @description updates the md5 checksum and file size
 */
public class ChecksumMojo extends AbstractEclipseSigningMojo
{
    /**
     * zip file to get checksum'ed
     * 
     * @parameter default-value="${project.build.directory}/packed/site_assembly.zip"
     * @required
     */
    protected String inputFile;

    /**
     * zip file to get checksum'ed
     * 
     * @parameter default-value="${project.build.directory}/fixed/site_assembly.zip"
     * @required
     */
    protected String outputFile;

    /**
     * zip file to get checksum'ed
     * 
     * @parameter default-value="${project.build.directory}/checksumFix"
     * @required
     */
    protected String unzipDir;

    /**
     * unpack path for artifact
     * 
     * @parameter default-value="/"
     * @required
     */
    protected String unpackPath;

    /**
     * checksum file
     * 
     * @parameter default-value="${project.build.directory}/checksumFix/artifacts.xml
     * @required
     */
    protected String artifactsXml;

    public void execute() throws MojoExecutionException, MojoFailureException
    {
        try
        {

            if (!FileUtils.fileExists(unzipDir))
            {
                info("creating directory to hold unzipped artifact");
                FileUtils.mkdir(unzipDir);
            }

            if (!FileUtils.fileExists(FileUtils.dirname(outputFile)))
            {
                info("creating directory to hold output");
                FileUtils.mkdir(FileUtils.dirname(outputFile));
            }

            ZipUnArchiver unzipper = new ZipUnArchiver(new File(inputFile));
            unzipper.extract("",new File(unzipDir));

            ZipUnArchiver unzipper2 = new ZipUnArchiver(new File(unzipDir + "/artifacts.jar"));
            unzipper2.extract("",new File(unzipDir));

            FileUtils.fileDelete(unzipDir + "/artifacts.jar.pack.gz");
            FileUtils.fileDelete(unzipDir + "/content.jar.pack.gz");
            
            insertPropertyAndMappingRules();

            @SuppressWarnings("unchecked")
            List<String> files = FileUtils.getFileNames(new File(unzipDir),"**/*.jar","",true);

            for (Iterator<String> i = files.iterator(); i.hasNext();)
            {
                String filename = i.next();
                info("Processing: " + filename);
                if (filename.endsWith("artifacts.jar"))
                {
                    continue;
                }
                else if (filename.endsWith("content.jar"))
                {
                    continue;
                }
                else
                {
                    update(filename);
                }
            }

            FileUtils.fileDelete(unzipDir + "/artifacts.jar");

            ZipArchiver zipper = new ZipArchiver();
            zipper.addFile(new File(unzipDir + "/artifacts.xml"),"artifacts.xml");
            // zipper.addDirectory(new File(unzipDir));
            zipper.setDestFile(new File(unzipDir + "/artifacts.jar"));
            zipper.createArchive();

            FileUtils.fileDelete(unzipDir + "/artifacts.xml");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    private void update(String plugin)
    {
        try
        {
            /*
             * some people have features the same as plugins features so we need to tweak the xpath
             */
            boolean isPlugin = plugin.contains("/plugins/");
            
            String pluginFile = FileUtils.basename(plugin,".jar");

            Pattern p = Pattern.compile("_\\d*\\.");
        	
        	String[] bits = p.split(pluginFile);
        	
        	//System.out.println(file.substring(0, bits[0].length()) + " / " + file.substring(bits[0].length() + 1 , file.length()));
        	            
            int versionSplitUnderscore = bits[0].length();
            
            String[] artifactBits = new String[2];

            artifactBits[0] = pluginFile.substring(0,versionSplitUnderscore);
            artifactBits[1] = pluginFile.substring(versionSplitUnderscore + 1, pluginFile.length());
            
            getLog().info(" Artifact: " + artifactBits[0] + " / Version: " + artifactBits[1] + " / isPlugin: " + isPlugin );

            File inFile = new File(plugin);
            String inFileSize = Long.toString(inFile.length());

            int count = 0;
            byte[] buffer = new byte[1024 * 16];
            InputStream in = new FileInputStream(inFile);
            MessageDigest md = MessageDigest.getInstance("MD5");

            while ((count = in.read(buffer)) > 0)
            {
                md.update(buffer,0,count);
            }
            in.close();

            String inFileMD5 = convert(md.digest());// (new BigInteger(1, md.digest())).toString(16);

            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = builder.parse(new File(artifactsXml));

            String expr;
            XPath xpath = XPathFactory.newInstance().newXPath();

            if ( isPlugin )
            {
                expr = "/repository/artifacts//artifact[@id='" + artifactBits[0] + "' and @version='" + artifactBits[1] + "' and @classifier='osgi.bundle']/properties//property[@name='artifact.size']/@value";
            }
            else
            {
                expr = "/repository/artifacts//artifact[@id='" + artifactBits[0] + "' and @version='" + artifactBits[1] + "' and @classifier='org.eclipse.update.feature']/properties//property[@name='artifact.size']/@value";
            }
            Node artifactSize = (Node)xpath.evaluate(expr,document,XPathConstants.NODE);
            if (artifactSize != null)
            {
                artifactSize.setNodeValue(inFileSize);
            }

            if (isPlugin)
            {
                expr = "/repository/artifacts//artifact[@id='" + artifactBits[0] + "' and @version='" + artifactBits[1] + "' and @classifier='osgi.bundle']/properties//property[@name='download.size']/@value";
            }
            else
            {
                expr = "/repository/artifacts//artifact[@id='" + artifactBits[0] + "' and @version='" + artifactBits[1] + "' and @classifier='org.eclipse.update.feature']/properties//property[@name='download.size']/@value";   
            }
            
            Node downloadSize = (Node)xpath.evaluate(expr,document,XPathConstants.NODE);
            if (downloadSize != null)
            {
                downloadSize.setNodeValue(inFileSize);
            }

            if ( isPlugin )
            {
                expr = "/repository/artifacts//artifact[@id='" + artifactBits[0] + "' and @version='" + artifactBits[1] + "' and @classifier='osgi.bundle']/properties//property[@name='download.md5']/@value";
            }
            else
            {
                expr = "/repository/artifacts//artifact[@id='" + artifactBits[0] + "' and @version='" + artifactBits[1] + "' and @classifier='org.eclipse.update.feature']/properties//property[@name='download.md5']/@value";
            }
            Node downloadMD5 = (Node)xpath.evaluate(expr,document,XPathConstants.NODE);
            if (downloadMD5 != null)
            {
                downloadMD5.setNodeValue(inFileMD5);
            }

            if (FileUtils.fileExists(plugin + ".pack.gz"))
            {
                File packFile = new File(plugin + ".pack.gz");
                String packFileSize = Long.toString(packFile.length());

                expr = "/repository/artifacts";// //artifact[@id='" + artifactBits[0] + "' and @version='" + artifactBits[1] + "']";
                Node normal = (Node)xpath.evaluate(expr,document,XPathConstants.NODE);
                Element packed = document.createElement("artifact");

                packed.setAttribute("classifier", isPlugin ? "osgi.bundle" : "org.eclipse.update.feature");
                packed.setAttribute("id",artifactBits[0]);
                packed.setAttribute("version",artifactBits[1]);

                Element processing = document.createElement("processing");
                processing.setAttribute("size","1");
                packed.appendChild(processing);

                Element step = document.createElement("step");
                step.setAttribute("id","org.eclipse.equinox.p2.processing.Pack200Unpacker");
                step.setAttribute("required","true");
                processing.appendChild(step);

                Element properties = document.createElement("properties");
                properties.setAttribute("size","3");
                packed.appendChild(properties);

                Element prop1 = document.createElement("property");
                prop1.setAttribute("name","artifact.size");
                prop1.setAttribute("value",inFileSize);
                properties.appendChild(prop1);

                Element prop2 = document.createElement("property");
                prop2.setAttribute("name","download.size");
                prop2.setAttribute("value",packFileSize);
                properties.appendChild(prop2);

                Element prop3 = document.createElement("property");
                prop3.setAttribute("name","format");
                prop3.setAttribute("value","packed");
                properties.appendChild(prop3);

                normal.appendChild(packed);

            }

            Transformer txformer = TransformerFactory.newInstance().newTransformer();
            DOMSource src = new DOMSource(document);
            StreamResult res = new StreamResult(artifactsXml);
            txformer.transform(src,res);
        }
        catch (Exception e)
        {
            getLog().error(e);
        }
    }
    
    /**
     * Creates the extra property and mapping rules for the pack.gz
     */
    protected void insertPropertyAndMappingRules()
    {
    	try
    	{
	        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
	        Document document = builder.parse(new File(artifactsXml));
	        
	        boolean documentWasModified = false;
	
	        {
	        //<property name='publishPackFilesAsSiblings' value='true'/>
	        String selectPublishPackfilesAsSiblings = "/repository/properties/property[@name='publishPackFilesAsSiblings']";
	        XPath xpath = XPathFactory.newInstance().newXPath();
	        Element propertyEl = (Element)xpath.evaluate(selectPublishPackfilesAsSiblings,document,XPathConstants.NODE);

	        String selectProperties = "/repository/properties";
	        xpath = XPathFactory.newInstance().newXPath();
	        Element propertiesEl = (Element)xpath.evaluate(selectProperties,document,XPathConstants.NODE);
	        if (propertiesEl == null)
	        {
	        	throw new IllegalArgumentException("The artifacts.xml document must have a /repository/properties element.");
	        }

	        if (propertyEl == null)
    		{
		        Element publishPackElem = document.createElement("property");
		        publishPackElem.setAttribute("name", "publishPackFilesAsSiblings");
		        publishPackElem.setAttribute("value", "true");
		        propertiesEl.appendChild(publishPackElem);
		        documentWasModified = true;
    		}
	        else
	        {
	        	if (!"true".equals(propertyEl.getAttribute("value")))
	        	{
	        		propertyEl.setAttribute("value", "true");
	        		documentWasModified = true;
	        	}
	        }
	        
	        //update the number of properties attribute
	        if (documentWasModified)
	        {
	        	int size = propertiesEl.getElementsByTagName("property").getLength();
	        	propertiesEl.setAttribute("size", String.valueOf(size));
	        }
	        }//end of properties processor
	        
	        {
	        //<rule filter='(&amp; (classifier=osgi.bundle) (format=packed))'
	        //      output='${repoUrl}/plugins/${id}_${version}.jar.pack.gz'/>
	        //<rule filter='(&amp; (classifier=org.eclipse.update.feature) (format=packed))'
	        //      output='${repoUrl}/features/${id}_${version}.jar.pack.gz'/>
	        String selectMappings = "/repository/mappings";
	        XPath xpath = XPathFactory.newInstance().newXPath();
	        Element mappingsEl = (Element)xpath.evaluate(selectMappings,document,XPathConstants.NODE);
	        if (mappingsEl == null)
	        {
	        	throw new IllegalArgumentException("The artifacts.xml document must have a /repository/mappings element.");
	        }
	        
	        //Just iterate over the rules elements and look at the value of the attributes.
	        boolean foundPackedBundleFilter = false;
	        boolean foundPackedFeatureFilter = false;
	        NodeList nl = mappingsEl.getChildNodes();
	        for (int i = 0; i < nl.getLength(); i++)
	        {
	        	Node c = nl.item(i);
	        	if (c.getNodeType() == Node.ELEMENT_NODE)
	        	{
	        		Element rule = (Element)c;
	        		String filter = rule.getAttribute("filter");
	        		if (filter.indexOf("format=packed") != -1)
	        		{
	        			if (filter.indexOf("classifier=osgi.bundle") != -1)
	        			{
	        				foundPackedBundleFilter = true;
	        			}
	        			else if (filter.indexOf("classifier=org.eclipse.update.feature") != -1)
	        			{
	        				foundPackedFeatureFilter = true;
	        			}
	        			if (foundPackedBundleFilter && foundPackedFeatureFilter)
	        			{
	        				break;
	        			}
	        		}
	        	}
	        }
	        if (!foundPackedBundleFilter)
	        {
		        Element ruleElem = document.createElement("rule");
		        ruleElem.setAttribute("filter", "(& (classifier=osgi.bundle) (format=packed))");
		        ruleElem.setAttribute("output", "${repoUrl}/plugins/${id}_${version}.jar.pack.gz");
		        // Bug 356931 format=packed must be first rule in mappings
		        Node firstRule = mappingsEl.getFirstChild();
		        mappingsEl.insertBefore(ruleElem, firstRule);
		        documentWasModified = true;
	        }
	        if (!foundPackedFeatureFilter)
	        {
		        Element ruleElem = document.createElement("rule");
		        ruleElem.setAttribute("filter", "(& (classifier=org.eclipse.update.feature) (format=packed))");
		        ruleElem.setAttribute("output", "${repoUrl}/features/${id}_${version}.jar.pack.gz");
		        // Bug 356931 format=packed must be first rule in mappings
		        Node firstRule = mappingsEl.getFirstChild();
		        mappingsEl.insertBefore(ruleElem, firstRule);

		        documentWasModified = true;
	        }
	        //update the number of properties attribute
	        if (!foundPackedFeatureFilter || !foundPackedBundleFilter)
	        {
	        	int size = mappingsEl.getElementsByTagName("rule").getLength();
	        	mappingsEl.setAttribute("size", String.valueOf(size));
	        }
	        
	        }//end of rules processor
	        
	        if (documentWasModified)
	        {
	            Transformer txformer = TransformerFactory.newInstance().newTransformer();
	            DOMSource src = new DOMSource(document);
	            StreamResult res = new StreamResult(artifactsXml);
	            txformer.transform(src,res);
	        }
        }
        catch (Exception e)
        {
            getLog().error(e);
        }
        
    }

    private String convert(byte[] bytes)
    {
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < bytes.length; ++i)
        {
            sb.append(Character.forDigit((bytes[i] >> 4) & 0xf,16));
            sb.append(Character.forDigit(bytes[i] & 0xf,16));
        }
        return sb.toString();
    }

    private void info(String log)
    {
        getLog().info("[FIX] " + log);
    }

    public static void main(String[] args ) throws Exception
    {
    	String file = "org.eclipse.equinox.server.core_1.1.1.R37x_v20110907-7K7TFBYDzbeA3ypK_98cDL15A4A";
    	Pattern p = Pattern.compile("_\\d");
    	
    	String[] bits = p.split(file);
    	
    	System.out.println(file.substring(0, bits[0].length()) + " / " + file.substring(bits[0].length() + 1 , file.length()));
    	
    	
    	
    	
    }
    
    
}

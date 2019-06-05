package com.arondor.common.reflection.parser.spring;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.LogManager;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import com.arondor.common.reflection.model.config.ObjectConfigurationMap;
import com.arondor.common.w3c2gwt.XMLParser;
import com.google.gwt.xml.client.Document;

public class XMLBeanDefinitionReaderTest
{
    static
    {
        try
        {
            LogManager.getLogManager().readConfiguration(
                    XMLBeanDefinitionReaderTest.class.getClassLoader().getResourceAsStream("logging.properties"));
            for (Enumeration<String> ens = LogManager.getLogManager().getLoggerNames(); ens.hasMoreElements();)
            {
                String e = ens.nextElement();
                System.err.println("At loggerName : '" + e + "'");
            }
            LogManager.getLogManager().getLogger("").setLevel(Level.FINEST);
        }
        catch (SecurityException e)
        {
            System.err.println("Could not setup " + e.getMessage());
        }
        catch (IOException e)
        {
            System.err.println("Could not setup " + e.getMessage());
        }
    }

    @Test
    public void testParseSampleFile() throws FileNotFoundException, IOException
    {
        String xmlPath = "spring/arondor-fast2p8-config.xml";

        XMLBeanDefinitionParser parser = new XMLBeanDefinitionParser(xmlPath);
        ObjectConfigurationMap expectedObjectConfigurationMap = parser.parse();

        String xmlContents = IOUtils.toString(new FileInputStream("src/test/resources/" + xmlPath));
        Document document = XMLParser.parse(xmlContents);

        XMLBeanDefinitionReader reader = new XMLBeanDefinitionReader();

        ObjectConfigurationMap readObjectConfigurationMap = reader.read(document);

        ObjectConfigurationComparator.getInstance().compareObjectConfigurationMap(expectedObjectConfigurationMap,
                readObjectConfigurationMap);

        XMLBeanDefinitionWriter writer = new XMLBeanDefinitionWriter();

        Document targetDocument = XMLParser.createDocument();
        writer.write(targetDocument, readObjectConfigurationMap);

        IOUtils.write(targetDocument.toString(), new FileOutputStream("target/read-fast2-config.xml"));
    }
}

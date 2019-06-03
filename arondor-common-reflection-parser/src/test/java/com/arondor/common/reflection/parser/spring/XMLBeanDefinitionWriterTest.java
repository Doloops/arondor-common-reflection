package com.arondor.common.reflection.parser.spring;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

import com.arondor.common.reflection.model.config.ObjectConfigurationMap;
import com.arondor.common.w3c2gwt.XMLParser;
import com.google.gwt.xml.client.Document;

public class XMLBeanDefinitionWriterTest
{
    private static final Logger LOGGER = Logger.getLogger(XMLBeanDefinitionWriterTest.class);

    @Test
    public void testWriteSimple() throws IOException
    {
        XMLBeanDefinitionParser parser = new XMLBeanDefinitionParser("spring/arondor-fast2p8-config.xml");

        ObjectConfigurationMap expectedObjectConfigurationMap = parser.parse();

        XMLBeanDefinitionWriter writer = new XMLBeanDefinitionWriter();

        Document document = XMLParser.createDocument();

        writer.write(document, expectedObjectConfigurationMap);

        File targetFile = new File("target/mapBeanDefinition.out.xml");
        FileOutputStream fos = new FileOutputStream(targetFile);
        fos.write(document.toString().getBytes());
        fos.close();
        // targetFile.delete();
        // writer.write(expectedObjectConfigurationMap,
        // targetFile.getAbsolutePath());

        Assert.assertTrue("Target " + targetFile.getAbsolutePath() + " does not exist", targetFile.exists());

        XMLBeanDefinitionParser parser2 = new XMLBeanDefinitionParser("file:///" + targetFile.getAbsolutePath());
        ObjectConfigurationMap resultObjectConfigurationMap = parser2.parse();

        ObjectConfigurationComparator.getInstance().compareObjectConfigurationMap(expectedObjectConfigurationMap,
                resultObjectConfigurationMap);
    }
}

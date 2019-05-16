package com.arondor.common.reflection.parser.spring;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

import com.arondor.common.reflection.model.config.ObjectConfigurationMap;

public class XMLBeanDefinitionWriterTest
{
    private static final Logger LOGGER = Logger.getLogger(XMLBeanDefinitionWriterTest.class);

    @Test
    public void testWriteSimple() throws IOException
    {
        XMLBeanDefinitionParser parser = new XMLBeanDefinitionParser("spring/arondor-fast2p8-config.xml");

        ObjectConfigurationMap expectedObjectConfigurationMap = parser.parse();

        XMLBeanDefinitionWriter writer = new XMLBeanDefinitionWriter();

        File targetFile = new File("target/mapBeanDefinition.out.xml");
        targetFile.delete();
        writer.write(expectedObjectConfigurationMap, targetFile.getAbsolutePath());

        Assert.assertTrue("Target " + targetFile.getAbsolutePath() + " does not exist", targetFile.exists());

        XMLBeanDefinitionParser parser2 = new XMLBeanDefinitionParser("file:///" + targetFile.getAbsolutePath());
        ObjectConfigurationMap resultObjectConfigurationMap = parser2.parse();

        ObjectConfigurationComparator.getInstance().compareObjectConfigurationMap(expectedObjectConfigurationMap,
                resultObjectConfigurationMap);
    }
}

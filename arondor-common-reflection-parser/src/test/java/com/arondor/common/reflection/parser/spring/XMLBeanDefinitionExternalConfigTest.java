package com.arondor.common.reflection.parser.spring;

import java.io.File;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.parsing.BeanDefinitionParsingException;

import com.arondor.common.reflection.model.config.ObjectConfigurationMap;

public class XMLBeanDefinitionExternalConfigTest
{
    @Test
    public void testExternalConfiguration()
    {
        File externalFile = new File("src/test/resources/spring/mapBeanDefinition.xml");
        Assert.assertTrue(externalFile.exists());
        String pathProperty = "FD_externalPath";
        System.setProperty(pathProperty, externalFile.getAbsolutePath());
        XMLBeanDefinitionParser parser = new XMLBeanDefinitionParser("spring/external-configuration.xml");
        System.clearProperty(pathProperty);
        ObjectConfigurationMap map = parser.parse();
        Assert.assertTrue(!map.isEmpty());
    }

    @Test(expected = BeanDefinitionParsingException.class)
    public void testExternalConfiguration_WithCurly()
    {
        File externalFile = new File("src/test/resources/spring/{file-with-curly-brackets}");
        Assert.assertTrue(externalFile.exists());
        String pathProperty = "FD_externalPath";
        System.setProperty(pathProperty, externalFile.getAbsolutePath());
        XMLBeanDefinitionParser parser = new XMLBeanDefinitionParser("spring/external-configuration.xml");
        System.clearProperty(pathProperty);
        ObjectConfigurationMap map = parser.parse();
        Assert.assertTrue(!map.isEmpty());
    }

    /**
     * Spring 5 changed semantics while evaluating context variables containing
     * curly brackets. This unit test to reflect such issue.
     */
    @Test
    public void testExternalConfiguration_WithCurly_Alone()
    {
        File externalFile = new File("src/test/resources/spring/alone/{file-with-curly-brackets}");
        Assert.assertTrue(externalFile.exists());
        String pathProperty = "FD_externalPath";
        System.setProperty(pathProperty, externalFile.getAbsolutePath());
        XMLBeanDefinitionParser parser = new XMLBeanDefinitionParser("spring/external-configuration.xml");
        System.clearProperty(pathProperty);
        ObjectConfigurationMap map = parser.parse();
        Assert.assertTrue(!map.isEmpty());
    }
}

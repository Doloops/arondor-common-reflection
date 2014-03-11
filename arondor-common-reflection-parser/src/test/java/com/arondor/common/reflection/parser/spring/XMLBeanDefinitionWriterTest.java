package com.arondor.common.reflection.parser.spring;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

import com.arondor.common.reflection.model.config.ElementConfiguration;
import com.arondor.common.reflection.model.config.ListConfiguration;
import com.arondor.common.reflection.model.config.MapConfiguration;
import com.arondor.common.reflection.model.config.ObjectConfiguration;
import com.arondor.common.reflection.model.config.ObjectConfigurationMap;
import com.arondor.common.reflection.model.config.ReferenceConfiguration;

public class XMLBeanDefinitionWriterTest
{

    private static final Logger LOGGER = Logger.getLogger(XMLBeanDefinitionWriterTest.class);

    @Test
    public void testWriteSimple() throws IOException
    {

        XMLBeanDefinitionParser parser = new XMLBeanDefinitionParser("spring/arondor-fast2p8-config.xml");
//        XMLBeanDefinitionParser parser = new XMLBeanDefinitionParser("spring/mapBeanDefinitionToWrite.xml");
        
        ObjectConfigurationMap expectedObjectConfiguration = parser.parse();

        XMLBeanDefinitionWriter writer = new XMLBeanDefinitionWriter();

        File targetFile = new File("target/mapBeanDefinition.out.xml");
        targetFile.delete();
        writer.write(expectedObjectConfiguration, targetFile.getAbsolutePath());

        Assert.assertTrue("Target " + targetFile.getAbsolutePath() + " does not exist", targetFile.exists());

        XMLBeanDefinitionParser parser2 = new XMLBeanDefinitionParser("file:///" + targetFile.getAbsolutePath());
        ObjectConfigurationMap resultObjectConfiguration = parser2.parse();

        for (String confName : expectedObjectConfiguration.keySet()) {            
            ObjectConfiguration objectConf = expectedObjectConfiguration.get(confName);
            ObjectConfiguration objectConf2 = resultObjectConfiguration.get(confName);
            compareObjectConfiguration(objectConf, objectConf2);            
        }
    }

    private void compareObjectConfiguration(ObjectConfiguration expectedObjConf, ObjectConfiguration resultObjConf)
    {
        LOGGER.debug("Bean definition, name=" + expectedObjConf.getObjectName() + ", class=" + expectedObjConf.getClassName() + ",isSingleton=" + expectedObjConf.isSingleton());
        Assert.assertEquals(expectedObjConf.getObjectName(), resultObjConf.getObjectName());
        Assert.assertEquals(expectedObjConf.getClassName(), resultObjConf.getClassName());
        Assert.assertEquals(expectedObjConf.isSingleton(), resultObjConf.isSingleton());

        for (String fieldName : expectedObjConf.getFields().keySet())
        {

            LOGGER.debug("Compare elementConfiguration for property=" + fieldName);
            ElementConfiguration expectedElementConf = expectedObjConf.getFields().get(fieldName);
            ElementConfiguration resultElementConf = resultObjConf.getFields().get(fieldName);
            compareElementConfiguration(expectedElementConf, resultElementConf);

        }

        int idx = 0;
        for (ElementConfiguration expectedElementConf : expectedObjConf.getConstructorArguments())
        {
            ElementConfiguration resultElementConf = resultObjConf.getConstructorArguments().get(idx);
            compareElementConfiguration(expectedElementConf, resultElementConf);
            idx++;
        }

    }

    private void compareElementConfiguration(ElementConfiguration expectedElementConf,
            ElementConfiguration resultElementConf)
    {

        switch (expectedElementConf.getFieldConfigurationType())
        {
        case Primitive:
            Assert.assertEquals(expectedElementConf, resultElementConf);
            break;
        case List:
            List<ElementConfiguration> elmtConfList = ((ListConfiguration) resultElementConf).getListConfiguration();
            int idx = 0;
            for (ElementConfiguration resultElmtConf : ((ListConfiguration) expectedElementConf).getListConfiguration())
            {
                ElementConfiguration expectedElmtConf = elmtConfList.get(idx);
                compareElementConfiguration(resultElmtConf, expectedElmtConf);
                idx++;
            }
            break;
        case Object:
            compareObjectConfiguration((ObjectConfiguration) expectedElementConf,
                    (ObjectConfiguration) resultElementConf);
            break;
        case Map:
            Map<ElementConfiguration, ElementConfiguration> expectedMap = ((MapConfiguration) expectedElementConf)
                    .getMapConfiguration();
            Map<ElementConfiguration, ElementConfiguration> resultMap = ((MapConfiguration) resultElementConf)
                    .getMapConfiguration();
            int expectedMapIdx = 0;
            for (ElementConfiguration keyExpectedElementConf : expectedMap.keySet())
            {
                ElementConfiguration valueExpectedElementConf = expectedMap.get(keyExpectedElementConf);
                int resultMapIdx = 0;
                for (ElementConfiguration keyResultElementConf : resultMap.keySet())
                {
                    if (resultMapIdx == expectedMapIdx)
                    {
                        ElementConfiguration valueResultElementConf = resultMap.get(keyResultElementConf);
                        compareElementConfiguration(valueExpectedElementConf, valueResultElementConf);
                        compareElementConfiguration(keyExpectedElementConf, keyResultElementConf);                        
                    }
                    resultMapIdx++;
                }
                expectedMapIdx++;
            }
            break;
        case Reference :
            Assert.assertEquals(((ReferenceConfiguration)expectedElementConf).getReferenceName(), ((ReferenceConfiguration)resultElementConf).getReferenceName());
            break;
        }
    }
}

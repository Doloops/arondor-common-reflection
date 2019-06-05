package com.arondor.common.reflection.parser.spring;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.junit.Assert;

import com.arondor.common.reflection.model.config.ElementConfiguration;
import com.arondor.common.reflection.model.config.ListConfiguration;
import com.arondor.common.reflection.model.config.MapConfiguration;
import com.arondor.common.reflection.model.config.ObjectConfiguration;
import com.arondor.common.reflection.model.config.ObjectConfigurationMap;
import com.arondor.common.reflection.model.config.ReferenceConfiguration;

public class ObjectConfigurationComparator
{
    private static final Logger LOGGER = Logger.getLogger(ObjectConfigurationComparator.class);

    private static final ObjectConfigurationComparator INSTANCE = new ObjectConfigurationComparator();

    public static ObjectConfigurationComparator getInstance()
    {
        return INSTANCE;
    }

    public void compareObjectConfigurationMap(ObjectConfigurationMap expectedObjectConfigurationMap,
            ObjectConfigurationMap resultObjectConfigurationMap)
    {
        for (String confName : expectedObjectConfigurationMap.keySet())
        {
            ObjectConfiguration objectConf = expectedObjectConfigurationMap.get(confName);
            ObjectConfiguration objectConf2 = resultObjectConfigurationMap.get(confName);
            Assert.assertNotNull("Object not present on target : " + confName, objectConf2);
            compareObjectConfiguration(objectConf, objectConf2);
        }
        for (String confName : resultObjectConfigurationMap.keySet())
        {
            Assert.assertNotNull(expectedObjectConfigurationMap.get(confName));
        }
    }

    private void compareObjectConfiguration(ObjectConfiguration expectedObjConf, ObjectConfiguration resultObjConf)
    {
        LOGGER.debug("Bean definition, name=" + expectedObjConf.getObjectName() + ", class="
                + expectedObjConf.getClassName() + ",isSingleton=" + expectedObjConf.isSingleton());
        Assert.assertEquals(expectedObjConf.getObjectName(), resultObjConf.getObjectName());
        Assert.assertEquals(expectedObjConf.getClassName(), resultObjConf.getClassName());
        if (expectedObjConf.isSingleton() != resultObjConf.isSingleton())
        {
            LOGGER.warn("Diverging singletons at bean definition, name=" + expectedObjConf.getObjectName() + ", class="
                    + expectedObjConf.getClassName() + ",isSingleton=" + expectedObjConf.isSingleton());
            // Assert.assertEquals(expectedObjConf.isSingleton(),
            // resultObjConf.isSingleton());
        }

        for (String fieldName : expectedObjConf.getFields().keySet())
        {
            LOGGER.debug("Compare elementConfiguration for property=" + fieldName);
            Assert.assertNotNull(expectedObjConf.getFields());
            ElementConfiguration expectedElementConf = expectedObjConf.getFields().get(fieldName);

            Assert.assertNotNull(resultObjConf.getFields());
            ElementConfiguration resultElementConf = resultObjConf.getFields().get(fieldName);
            compareElementConfiguration(expectedElementConf, resultElementConf);

        }

        int idx = 0;
        if (expectedObjConf.getConstructorArguments() != null)
        {
            Assert.assertNotNull(resultObjConf.getConstructorArguments());
            Assert.assertEquals(expectedObjConf.getConstructorArguments().size(),
                    resultObjConf.getConstructorArguments().size());
        }
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
        Assert.assertNotNull(expectedElementConf);
        Assert.assertNotNull(resultElementConf);
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
            Assert.assertEquals(expectedMap.size(), resultMap.size());
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
        case Reference:
            Assert.assertEquals(((ReferenceConfiguration) expectedElementConf).getReferenceName(),
                    ((ReferenceConfiguration) resultElementConf).getReferenceName());
            break;
        }
    }

}

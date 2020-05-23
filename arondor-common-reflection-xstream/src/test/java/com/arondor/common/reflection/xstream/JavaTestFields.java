package com.arondor.common.reflection.xstream;

import java.util.HashMap;
import java.util.logging.Logger;

import org.junit.Test;

import com.arondor.common.reflection.bean.config.ObjectConfigurationBean;
import com.arondor.common.reflection.bean.config.PrimitiveConfigurationBean;
import com.arondor.common.reflection.model.config.ElementConfiguration;

public class JavaTestFields extends AbstractTestParserSerializer
{
    private static final Logger LOG = Logger.getLogger(JavaTestFields.class.getName());

    @Test(expected = RuntimeException.class)
    public void test_serializeNullField()
    {
        ObjectConfigurationBean oc = new ObjectConfigurationBean();
        oc.setFields(new HashMap<String, ElementConfiguration>());
        oc.getFields().put("name", null);
        String resultXml = serializer.serialize(oc);
        LOG.info("resultXml=" + resultXml);
    }

    @Test(expected = RuntimeException.class)
    public void test_serializeField_NullPrimitive()
    {
        ObjectConfigurationBean oc = new ObjectConfigurationBean();
        oc.setFields(new HashMap<String, ElementConfiguration>());
        PrimitiveConfigurationBean prim = new PrimitiveConfigurationBean();
        prim.setValue(null);
        oc.getFields().put("name", prim);
        String resultXml = serializer.serialize(oc);
        LOG.info("resultXml=" + resultXml);
    }
}

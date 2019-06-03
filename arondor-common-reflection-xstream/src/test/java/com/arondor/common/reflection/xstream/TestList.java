package com.arondor.common.reflection.xstream;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.xml.sax.SAXException;

import com.arondor.common.reflection.bean.config.ListConfigurationBean;
import com.arondor.common.reflection.bean.config.ObjectConfigurationBean;
import com.arondor.common.reflection.bean.config.PrimitiveConfigurationBean;
import com.arondor.common.reflection.model.config.ElementConfiguration;
import com.arondor.common.reflection.model.config.ListConfiguration;
import com.arondor.common.reflection.model.config.ObjectConfiguration;
import com.arondor.common.reflection.xstream.testing.PrimitiveClass;
import com.google.gwt.dev.util.collect.HashMap;

import junit.framework.Assert;

public class TestList extends AbstractTestXStream
{
    private static final Logger LOG = Logger.getLogger(TestList.class);

    private final ObjectConfigurationSerializer serializer = new GWTObjectConfigurationSerializerJava();

    private final ObjectConfigurationReader converter = new GWTObjectConfigurationParserJava();

    protected ObjectConfiguration toAndFromXML(ObjectConfiguration oc)
    {
        String xml = serializer.serialize(oc);
        LOG.info("Result xml :" + xml);
        return converter.parse(xml);
    }

    @Test
    public void testPrimitive0_Parse() throws ParserConfigurationException, SAXException, IOException
    {
        PrimitiveClass pc = new PrimitiveClass();
        pc.setIntValue(42);
        pc.setFloatValue(87f);
        pc.setStringValue("Some string");

        ObjectConfiguration oc = serializeAndParse(pc);
    }

    @Test
    public void testList() throws ParserConfigurationException, SAXException, IOException
    {
        ListConfigurationBean lc = new ListConfigurationBean();
        lc.setListConfiguration(new ArrayList<ElementConfiguration>());
        PrimitiveConfigurationBean pc1 = new PrimitiveConfigurationBean();
        pc1.setValue("Value One");
        lc.getListConfiguration().add(pc1);

        PrimitiveConfigurationBean pc2 = new PrimitiveConfigurationBean();
        pc2.setValue("Value Two");
        lc.getListConfiguration().add(pc2);

        ObjectConfigurationBean oc = new ObjectConfigurationBean();
        oc.setClassName("com.arondor.SomeClass");
        oc.setFields(new HashMap<String, ElementConfiguration>());
        oc.getFields().put("myList", lc);

        ObjectConfiguration targetObject = toAndFromXML(oc);
        ListConfiguration targetList = (ListConfiguration) targetObject.getFields().get("myList");

        Assert.assertEquals(2, targetList.getListConfiguration().size());
    }
}

package com.arondor.common.reflection.xstream;

import org.apache.log4j.Logger;
import org.junit.Ignore;
import org.junit.Test;

import com.arondor.common.reflection.model.config.ObjectConfiguration;
import com.arondor.common.reflection.xtream.testing.PrimitiveClass;
import com.google.gwt.junit.client.GWTTestCase;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

@Ignore
public class GWTTestPrimitive extends GWTTestCase
{
    private static final Logger LOG = Logger.getLogger(GWTTestPrimitive.class);

    @Override
    public String getModuleName()
    {
        return "com.arondor.common.reflection.ReflectionXStreamTest";
    }

    @Test
    public void testPrimitive0_Parse()
    {
        PrimitiveClass pc = new PrimitiveClass();
        pc.setIntValue(42);
        pc.setFloatValue(87f);
        pc.setStringValue("Some string");
        XStream xstream = new XStream(new DomDriver());
        String xml = xstream.toXML(pc);

        LOG.info("Result xml :" + xml);

        GWTXStream2ObjectDefinitionConverter converter = new GWTXStream2ObjectDefinitionConverter();
        ObjectConfiguration oc = converter.parse(xml);
    }

    @Test
    public void testPrimitive0_Instantiate()
    {

    }

}

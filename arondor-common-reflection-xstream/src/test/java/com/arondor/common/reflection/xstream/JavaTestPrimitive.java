package com.arondor.common.reflection.xstream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.logging.Logger;

import org.junit.Test;

import com.arondor.common.reflection.model.config.ElementConfiguration;
import com.arondor.common.reflection.model.config.ObjectConfiguration;
import com.arondor.common.reflection.model.config.PrimitiveConfiguration;

public class JavaTestPrimitive extends AbstractTestParserSerializer
{
    private static final Logger LOG = Logger.getLogger(JavaTestPrimitive.class.getName());

    @Test
    public void testPrimitive0_Parse0()
    {
        /*
         * PrimitiveClass pc = new PrimitiveClass(); pc.setIntValue(42);
         * pc.setFloatValue(87f); pc.setStringValue("Some string"); XStream
         * xstream = new XStream(new DomDriver()); String xml =
         * xstream.toXML(pc);
         */

        String xml = "<com.arondor.common.reflection.xstream.testing.PrimitiveClass>" + "<intValue>42</intValue>"
                + "<floatValue>87.0</floatValue>" + "<stringValue>Some string</stringValue>"
                + "</com.arondor.common.reflection.xstream.testing.PrimitiveClass>";

        LOG.info("Result xml :" + xml);

        ObjectConfiguration oc = parser.parse(xml);

        assertEquals("com.arondor.common.reflection.xstream.testing.PrimitiveClass", oc.getClassName());
        assertEquals(3, oc.getFields().size());
    }

    @Test
    public void testPrimitive0_MoreComplex()
    {
        String xml = "<a><b><titi>value23</titi></b></a>";

        LOG.info("Result xml :" + xml);

        ObjectConfiguration oc = parser.parse(xml);

        assertEquals("a", oc.getClassName());
        assertEquals(1, oc.getFields().size());

        ElementConfiguration ec1 = oc.getFields().get("b");
        assertTrue(ec1 instanceof ObjectConfiguration);

        ObjectConfiguration oc1 = (ObjectConfiguration) ec1;
        assertEquals("b", oc1.getClassName());
        assertEquals(1, oc1.getFields().size());

        ElementConfiguration ec2 = oc1.getFields().get("titi");
        assertTrue(ec2 instanceof PrimitiveConfiguration);

        PrimitiveConfiguration pc2 = (PrimitiveConfiguration) ec2;
        assertEquals("value23", pc2.getValue());
    }

    @Test
    public void testPrimitive0_Serialize()
    {
        String xml = "<a class=\"com.arondor.testing.TestCase\"><titi>value23</titi></a>";
        ObjectConfiguration oc = parser.parse(xml);
        String resultXml = serializer.serialize(oc);
        LOG.info("resultXml=" + resultXml);
    }
}

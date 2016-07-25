package com.arondor.common.reflection.xstream;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Assert;
import org.junit.Test;
import org.xml.sax.SAXException;

import com.arondor.common.reflection.model.config.ElementConfiguration;
import com.arondor.common.reflection.model.config.ElementConfiguration.ElementConfigurationType;
import com.arondor.common.reflection.model.config.ListConfiguration;
import com.arondor.common.reflection.model.config.ObjectConfiguration;
import com.arondor.common.reflection.model.config.PrimitiveConfiguration;
import com.arondor.common.reflection.xtream.testing.PrimitiveClass;
import com.arondor.common.reflection.xtream.testing.PrimitiveListClass;

public class TestPrimitive extends AbstractTestXStream
{

    @Test
    public void testPrimitive0_Parse() throws ParserConfigurationException, SAXException, IOException
    {
        PrimitiveClass pc = new PrimitiveClass();
        pc.setIntValue(42);
        pc.setFloatValue(87f);
        pc.setStringValue("Some string");

        ObjectConfiguration oc = serializeAndParse(pc);

        Assert.assertEquals(pc.getClass().getName(), oc.getClassName());

        ElementConfiguration ec0 = oc.getFields().get("intValue");

        Assert.assertNotNull(ec0.getFieldConfigurationType());
        Assert.assertEquals(ElementConfigurationType.Primitive, ec0.getFieldConfigurationType());
        Assert.assertTrue(ec0 instanceof PrimitiveConfiguration);

        Assert.assertEquals("42", ((PrimitiveConfiguration) ec0).getValue());

        ElementConfiguration ec1 = oc.getFields().get("floatValue");
        Assert.assertEquals("87.0", ((PrimitiveConfiguration) ec1).getValue());

        ElementConfiguration ec2 = oc.getFields().get("stringValue");
        Assert.assertEquals("Some string", ((PrimitiveConfiguration) ec2).getValue());

    }

    @Test
    public void testPrimitive0_Instantiate()
    {

    }

    @Test
    public void testPrimitiveList0_Parse() throws ParserConfigurationException, SAXException, IOException
    {
        PrimitiveListClass plc = new PrimitiveListClass();
        plc.setIntegerList(new ArrayList<Integer>());
        plc.getIntegerList().add(42);

        plc.setFloatList(new ArrayList<Float>());
        plc.getFloatList().add(27.2f);

        plc.setStringList(new ArrayList<String>());
        plc.getStringList().add("First string");

        ObjectConfiguration oc = serializeAndParse(plc);

        Assert.assertEquals(plc.getClass().getName(), oc.getClassName());

        ElementConfiguration ec0 = oc.getFields().get("integerList");
        Assert.assertNotNull(ec0);
        Assert.assertEquals(ElementConfigurationType.List, ec0.getFieldConfigurationType());
        Assert.assertTrue(ec0 instanceof ListConfiguration);

        List<ElementConfiguration> list0 = ((ListConfiguration) ec0).getListConfiguration();

        Assert.assertEquals(1, list0.size());

        Assert.assertEquals(ElementConfigurationType.Primitive, list0.get(0).getFieldConfigurationType());
        Assert.assertEquals("42", ((PrimitiveConfiguration) list0.get(0)).getValue());

        ElementConfiguration ec1 = oc.getFields().get("floatList");
        Assert.assertNotNull(ec1);
        Assert.assertEquals(ElementConfigurationType.List, ec1.getFieldConfigurationType());
        Assert.assertTrue(ec1 instanceof ListConfiguration);

        List<ElementConfiguration> list1 = ((ListConfiguration) ec1).getListConfiguration();

        Assert.assertEquals(1, list1.size());

        Assert.assertEquals(ElementConfigurationType.Primitive, list1.get(0).getFieldConfigurationType());
        Assert.assertEquals("27.2", ((PrimitiveConfiguration) list1.get(0)).getValue());

        ElementConfiguration ec2 = oc.getFields().get("stringList");
        Assert.assertNotNull(ec2);
        Assert.assertEquals(ElementConfigurationType.List, ec2.getFieldConfigurationType());
        Assert.assertTrue(ec2 instanceof ListConfiguration);

        List<ElementConfiguration> list2 = ((ListConfiguration) ec2).getListConfiguration();

        Assert.assertEquals(1, list2.size());

        Assert.assertEquals(ElementConfigurationType.Primitive, list2.get(0).getFieldConfigurationType());
        Assert.assertEquals("First string", ((PrimitiveConfiguration) list2.get(0)).getValue());
    }

    @Test
    public void testPrimitiveList1_Parse() throws ParserConfigurationException, SAXException, IOException
    {
        PrimitiveListClass plc = new PrimitiveListClass();
        plc.setIntegerList(new ArrayList<Integer>());
        plc.getIntegerList().add(2);
        plc.getIntegerList().add(42);

        plc.setFloatList(new ArrayList<Float>());
        plc.getFloatList().add(48.3f);
        plc.getFloatList().add(27.2f);

        plc.setStringList(new ArrayList<String>());
        plc.getStringList().add("First string");
        plc.getStringList().add("Second string");

        ObjectConfiguration oc = serializeAndParse(plc);

        Assert.assertEquals(plc.getClass().getName(), oc.getClassName());

        ElementConfiguration ec0 = oc.getFields().get("integerList");
        Assert.assertNotNull(ec0);
        Assert.assertEquals(ElementConfigurationType.List, ec0.getFieldConfigurationType());
        Assert.assertTrue(ec0 instanceof ListConfiguration);

        List<ElementConfiguration> list0 = ((ListConfiguration) ec0).getListConfiguration();

        Assert.assertEquals(2, list0.size());

        Assert.assertEquals(ElementConfigurationType.Primitive, list0.get(0).getFieldConfigurationType());
        Assert.assertEquals("2", ((PrimitiveConfiguration) list0.get(0)).getValue());
        Assert.assertEquals("42", ((PrimitiveConfiguration) list0.get(1)).getValue());

        ElementConfiguration ec1 = oc.getFields().get("floatList");
        Assert.assertNotNull(ec1);
        Assert.assertEquals(ElementConfigurationType.List, ec1.getFieldConfigurationType());
        Assert.assertTrue(ec1 instanceof ListConfiguration);

        List<ElementConfiguration> list1 = ((ListConfiguration) ec1).getListConfiguration();

        Assert.assertEquals(2, list1.size());

        Assert.assertEquals(ElementConfigurationType.Primitive, list1.get(0).getFieldConfigurationType());
        Assert.assertEquals("48.3", ((PrimitiveConfiguration) list1.get(0)).getValue());
        Assert.assertEquals("27.2", ((PrimitiveConfiguration) list1.get(1)).getValue());

        ElementConfiguration ec2 = oc.getFields().get("stringList");
        Assert.assertNotNull(ec2);
        Assert.assertEquals(ElementConfigurationType.List, ec2.getFieldConfigurationType());
        Assert.assertTrue(ec2 instanceof ListConfiguration);

        List<ElementConfiguration> list2 = ((ListConfiguration) ec2).getListConfiguration();

        Assert.assertEquals(2, list2.size());

        Assert.assertEquals(ElementConfigurationType.Primitive, list2.get(0).getFieldConfigurationType());
        Assert.assertEquals("First string", ((PrimitiveConfiguration) list2.get(0)).getValue());
        Assert.assertEquals("Second string", ((PrimitiveConfiguration) list2.get(1)).getValue());
    }

}

package com.arondor.common.reflection.parser.spring;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.arondor.common.reflection.bean.config.ListConfigurationBean;
import com.arondor.common.reflection.model.config.ElementConfiguration;
import com.arondor.common.reflection.model.config.ElementConfiguration.ElementConfigurationType;
import com.arondor.common.reflection.model.config.ListConfiguration;
import com.arondor.common.reflection.model.config.ObjectConfiguration;
import com.arondor.common.reflection.model.config.ObjectConfigurationMap;
import com.arondor.common.reflection.model.config.PrimitiveConfiguration;
import com.arondor.common.reflection.model.config.ReferenceConfiguration;

public class XMLBeanDefinitionParserTest
{
    private XMLBeanDefinitionParser parser;

    private static final String XML_PATH = "/spring/simpleBeanDefinition.xml";

    private final String className = "com.arondor.test.TestBean";

    @Before
    public void setUp() throws Exception
    {
        parser = new XMLBeanDefinitionParser(XML_PATH);
    }

    @Test
    public void testParseBeanWithSinglePrimitiveValue() throws Exception
    {
        ObjectConfiguration objectConfiguration = getAndCheckParsedObjectConfiguration("beanWithSinglePrimitiveValue");
        checkIsValidObjectConfigurationForBeanWithSinglePrimitiveProperty(objectConfiguration);
    }

    @Test
    public void testParseBeanWithMultiplePrimitiveValue()
    {
        ObjectConfiguration objectConfiguration = getAndCheckParsedObjectConfiguration("beanWithMultiplePrimitiveValue");
        checkIsValidObjectConfigurationForBeanWithMultiplePrimitiveValue(objectConfiguration);
    }

    @Test
    public void testParseBeanWithSingleObjectValue()
    {
        ObjectConfiguration objectConfiguration = getAndCheckParsedObjectConfiguration("beanWithSingleObjectValue");
        Map<String, ElementConfiguration> fields = objectConfiguration.getFields();
        assertEquals(1, fields.size());
        ElementConfiguration field = fields.get("object");
        assertNotNull(field);
        assertEquals(ElementConfigurationType.Reference, field.getFieldConfigurationType());
        assertEquals("beanWithSinglePrimitiveValue", ((ReferenceConfiguration) field).getReferenceName());
    }

    @Test
    public void testParseBeanWithMultipleObjectValue()
    {
        ObjectConfiguration objectConfiguration = getAndCheckParsedObjectConfiguration("beanWithMultipleObjectValue");

        Map<String, ElementConfiguration> fields = objectConfiguration.getFields();
        assertEquals(1, fields.size());

        ElementConfiguration field = fields.get("objects");
        assertEquals(ElementConfigurationType.List, field.getFieldConfigurationType());

        List<ElementConfiguration> objectConfigurations = ((ListConfiguration) field).getListConfiguration();
        assertNotNull(objectConfigurations);
        checkIsValidObjectConfigurationForBeanWithSinglePrimitiveProperty(objectConfigurations.get(0));
        checkIsValidObjectConfigurationForBeanWithMultiplePrimitiveValue(objectConfigurations.get(1));
    }

    @Test
    public void testParseBeanWithPrimitiveConstructorArgs()
    {
        ObjectConfiguration objectConfiguration = getAndCheckParsedObjectConfiguration("beanWithPrimitiveConstructorArgs");
        assertEquals(0, objectConfiguration.getFields().size());
        assertEquals(2, objectConfiguration.getConstructorArguments().size());
        assertEquals("my arg 1",
                ((PrimitiveConfiguration) objectConfiguration.getConstructorArguments().get(0)).getValue());
        assertEquals("my arg 2",
                ((PrimitiveConfiguration) objectConfiguration.getConstructorArguments().get(1)).getValue());
    }

    @Test
    public void testParseBeanWithBothPrimitiveAndObjectConstructorArgs()
    {
        ObjectConfiguration objectConfiguration = getAndCheckParsedObjectConfiguration("beanWithBothPrimitiveAndObjectConstructorArgs");
        assertEquals(0, objectConfiguration.getFields().size());
        assertEquals(3, objectConfiguration.getConstructorArguments().size());
        assertEquals("beanWithSinglePrimitiveValue", ((ReferenceConfiguration) objectConfiguration
                .getConstructorArguments().get(0)).getReferenceName());
        assertEquals("my arg 1",
                ((PrimitiveConfiguration) objectConfiguration.getConstructorArguments().get(1)).getValue());

        assertEquals("com.arondor.viewer.client.toppanel.behavior.document.DocumentPrintHandler",
                ((ObjectConfiguration) objectConfiguration.getConstructorArguments().get(2)).getClassName());
    }

    private ObjectConfiguration getAndCheckParsedObjectConfiguration(String beanName)
    {
        ObjectConfigurationMap parsedObjectConfiguration = parser.parse();
        ObjectConfiguration objectConfiguration = parsedObjectConfiguration.get(beanName);
        assertEquals(beanName, objectConfiguration.getObjectName());
        assertNotNull(objectConfiguration);
        assertEquals(className, objectConfiguration.getClassName());
        return objectConfiguration;
    }

    private void checkIsValidObjectConfigurationForBeanWithSinglePrimitiveProperty(
            ElementConfiguration elementConfiguration)
    {
        assertTrue(elementConfiguration instanceof ObjectConfiguration);
        ObjectConfiguration objectConfiguration = (ObjectConfiguration) elementConfiguration;
        Map<String, ElementConfiguration> fields = objectConfiguration.getFields();
        assertEquals(1, fields.size());
        ElementConfiguration field = fields.get("name");
        assertEquals(ElementConfigurationType.Primitive, field.getFieldConfigurationType());
        assertEquals("my object name", ((PrimitiveConfiguration) field).getValue());
    }

    private void checkIsValidObjectConfigurationForBeanWithMultiplePrimitiveValue(
            ElementConfiguration elementConfiguration)
    {
        assertTrue(elementConfiguration instanceof ObjectConfiguration);
        ObjectConfiguration objectConfiguration = (ObjectConfiguration) elementConfiguration;
        Map<String, ElementConfiguration> fields = objectConfiguration.getFields();
        assertEquals(1, fields.size());
        ElementConfiguration field = fields.get("names");
        assertEquals(ElementConfigurationType.List, field.getFieldConfigurationType());

        ListConfiguration listConfiguration = (ListConfigurationBean) field;
        assertNotNull(listConfiguration);
        assertEquals(3, listConfiguration.getListConfiguration().size());
        assertEquals("name1", ((PrimitiveConfiguration) listConfiguration.getListConfiguration().get(0)).getValue());
        assertEquals("name2", ((PrimitiveConfiguration) listConfiguration.getListConfiguration().get(1)).getValue());
        assertEquals("name3", ((PrimitiveConfiguration) listConfiguration.getListConfiguration().get(2)).getValue());
    }

    @Test
    public void testPrintButton()
    {
        ObjectConfigurationMap parsedObjectConfiguration = parser.parse();
        ObjectConfiguration objectConfiguration = parsedObjectConfiguration.get("printButton");

        assertNotNull(objectConfiguration);
        assertEquals("com.arondor.viewer.client.toppanel.presenter.ButtonPresenter", objectConfiguration.getClassName());
        assertEquals("printButton", objectConfiguration.getObjectName());
        assertNotNull(objectConfiguration.getConstructorArguments());
        assertEquals(3, objectConfiguration.getConstructorArguments().size());

        assertEquals(ElementConfigurationType.Reference, objectConfiguration.getConstructorArguments().get(0)
                .getFieldConfigurationType());
        assertEquals("images#printDocument", ((ReferenceConfiguration) objectConfiguration.getConstructorArguments()
                .get(0)).getReferenceName());

        assertEquals(ElementConfigurationType.Primitive, objectConfiguration.getConstructorArguments().get(1)
                .getFieldConfigurationType());
        assertEquals("Print",
                ((PrimitiveConfiguration) objectConfiguration.getConstructorArguments().get(1)).getValue());

        assertEquals(ElementConfigurationType.Object, objectConfiguration.getConstructorArguments().get(2)
                .getFieldConfigurationType());
        assertEquals("com.arondor.viewer.client.toppanel.behavior.document.DocumentPrintHandler",
                ((ObjectConfiguration) objectConfiguration.getConstructorArguments().get(2)).getClassName());

    }

    @Test
    public void testTypedEnum()
    {
        ObjectConfigurationMap parsedObjectConfiguration = parser.parse();
        ObjectConfiguration objectConfiguration = parsedObjectConfiguration.get("typedEnum");

        assertNotNull(objectConfiguration);
        assertEquals("com.arondor.viewer.client.events.downupload.AskDocumentUploadEvent",
                objectConfiguration.getClassName());
        assertEquals(1, objectConfiguration.getConstructorArguments().size());

        assertEquals(ElementConfigurationType.Object, objectConfiguration.getConstructorArguments().get(0)
                .getFieldConfigurationType());

        ObjectConfiguration enumConfig = (ObjectConfiguration) objectConfiguration.getConstructorArguments().get(0);
        assertNotNull(enumConfig);
        assertEquals("com.arondor.viewer.client.events.downupload.AskDocumentUploadEvent.Behavior",
                enumConfig.getClassName());

        assertEquals(1, enumConfig.getConstructorArguments().size());

        assertEquals(ElementConfigurationType.Primitive, enumConfig.getConstructorArguments().get(0)
                .getFieldConfigurationType());
        assertEquals("UPLOAD_FILE", ((PrimitiveConfiguration) enumConfig.getConstructorArguments().get(0)).getValue());
    }

    @Test
    public void testSingleton()
    {
        ObjectConfigurationMap parsedObjectConfiguration = parser.parse();
        ObjectConfiguration objectConfiguration = parsedObjectConfiguration.get("singletonObject");

        assertTrue("Shall be singleton !", objectConfiguration.isSingleton());

    }

    @Test
    public void testNotSingleton()
    {
        ObjectConfigurationMap parsedObjectConfiguration = parser.parse();
        ObjectConfiguration objectConfiguration = parsedObjectConfiguration.get("nonSingletonObject");

        assertFalse("Shall be non-singleton !", objectConfiguration.isSingleton());
    }

}

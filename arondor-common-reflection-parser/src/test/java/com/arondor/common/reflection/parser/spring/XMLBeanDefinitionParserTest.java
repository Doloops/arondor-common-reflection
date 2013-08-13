package com.arondor.common.reflection.parser.spring;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.arondor.common.reflection.model.config.FieldConfiguration;
import com.arondor.common.reflection.model.config.FieldConfiguration.FieldConfigurationType;
import com.arondor.common.reflection.model.config.ObjectConfiguration;
import com.arondor.common.reflection.model.config.ObjectConfigurationMap;

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
        Map<String, FieldConfiguration> fields = objectConfiguration.getFields();
        assertEquals(1, fields.size());
        FieldConfiguration field = fields.get("object");
        assertEquals(FieldConfigurationType.Object_Single, field.getFieldConfigurationType());
        assertNotNull(field.getObjectConfiguration());
        assertEquals("beanWithSinglePrimitiveValue", field.getObjectConfiguration().getReferenceName());
        // checkIsValidObjectConfigurationForBeanWithSinglePrimitiveProperty(field.getObjectConfiguration());
    }

    @Test
    public void testParseBeanWithMultipleObjectValue()
    {
        ObjectConfiguration objectConfiguration = getAndCheckParsedObjectConfiguration("beanWithMultipleObjectValue");

        Map<String, FieldConfiguration> fields = objectConfiguration.getFields();
        assertEquals(1, fields.size());

        FieldConfiguration field = fields.get("objects");
        assertEquals(FieldConfigurationType.Object_Multiple, field.getFieldConfigurationType());

        List<FieldConfiguration> objectConfigurations = field.getObjectConfigurations();
        assertNotNull(objectConfigurations);
        checkIsValidObjectConfigurationForBeanWithSinglePrimitiveProperty(objectConfigurations.get(0)
                .getObjectConfiguration());
        checkIsValidObjectConfigurationForBeanWithMultiplePrimitiveValue(objectConfigurations.get(1)
                .getObjectConfiguration());
    }

    @Test
    public void testParseBeanWithPrimitiveConstructorArgs()
    {
        ObjectConfiguration objectConfiguration = getAndCheckParsedObjectConfiguration("beanWithPrimitiveConstructorArgs");
        assertEquals(0, objectConfiguration.getFields().size());
        assertEquals(2, objectConfiguration.getConstructorArguments().size());
        assertEquals("my arg 1", objectConfiguration.getConstructorArguments().get(0).getValue());
        assertEquals("my arg 2", objectConfiguration.getConstructorArguments().get(1).getValue());
    }

    @Test
    public void testParseBeanWithBothPrimitiveAndObjectConstructorArgs()
    {
        ObjectConfiguration objectConfiguration = getAndCheckParsedObjectConfiguration("beanWithBothPrimitiveAndObjectConstructorArgs");
        assertEquals(0, objectConfiguration.getFields().size());
        assertEquals(3, objectConfiguration.getConstructorArguments().size());
        assertEquals("beanWithSinglePrimitiveValue", objectConfiguration.getConstructorArguments().get(0)
                .getObjectConfiguration().getReferenceName());
        assertEquals("my arg 1", objectConfiguration.getConstructorArguments().get(1).getValue());

        assertEquals("com.arondor.viewer.client.toppanel.behavior.document.DocumentPrintHandler", objectConfiguration
                .getConstructorArguments().get(2).getObjectConfiguration().getClassName());
    }

    private ObjectConfiguration getAndCheckParsedObjectConfiguration(String beanName)
    {
        ObjectConfigurationMap parsedObjectConfiguration = parser.parse();
        ObjectConfiguration objectConfiguration = parsedObjectConfiguration.get(beanName);
        assertEquals(beanName, objectConfiguration.getReferenceName());
        assertNotNull(objectConfiguration);
        assertEquals(className, objectConfiguration.getClassName());
        return objectConfiguration;
    }

    private void checkIsValidObjectConfigurationForBeanWithSinglePrimitiveProperty(
            ObjectConfiguration objectConfiguration)
    {
        Map<String, FieldConfiguration> fields = objectConfiguration.getFields();
        assertEquals(1, fields.size());
        FieldConfiguration field = fields.get("name");
        assertEquals(FieldConfigurationType.Primitive_Single, field.getFieldConfigurationType());
        assertEquals("my object name", field.getValue());
    }

    private void checkIsValidObjectConfigurationForBeanWithMultiplePrimitiveValue(
            ObjectConfiguration objectConfiguration)
    {
        Map<String, FieldConfiguration> fields = objectConfiguration.getFields();
        assertEquals(1, fields.size());
        FieldConfiguration field = fields.get("names");
        assertEquals(FieldConfigurationType.Primitive_Multiple, field.getFieldConfigurationType());
        assertNull(field.getValue());
        assertNotNull(field.getValues());
        assertEquals(3, field.getValues().size());
        assertEquals("name1", field.getValues().get(0));
        assertEquals("name2", field.getValues().get(1));
        assertEquals("name3", field.getValues().get(2));
    }

    @Test
    public void testPrintButton()
    {
        ObjectConfigurationMap parsedObjectConfiguration = parser.parse();
        ObjectConfiguration objectConfiguration = parsedObjectConfiguration.get("printButton");

        assertNotNull(objectConfiguration);
        assertEquals("com.arondor.viewer.client.toppanel.presenter.ButtonPresenter", objectConfiguration.getClassName());
        assertEquals("printButton", objectConfiguration.getReferenceName());
        assertNotNull(objectConfiguration.getConstructorArguments());
        assertEquals(3, objectConfiguration.getConstructorArguments().size());

        assertEquals(FieldConfigurationType.Object_Single, objectConfiguration.getConstructorArguments().get(0)
                .getFieldConfigurationType());
        assertNull(objectConfiguration.getConstructorArguments().get(0).getObjectConfiguration().getClassName());
        assertEquals("images#printDocument", objectConfiguration.getConstructorArguments().get(0)
                .getObjectConfiguration().getReferenceName());

        assertEquals(FieldConfigurationType.Primitive_Single, objectConfiguration.getConstructorArguments().get(1)
                .getFieldConfigurationType());
        assertEquals("Print", objectConfiguration.getConstructorArguments().get(1).getValue());

        assertEquals(FieldConfigurationType.Object_Single, objectConfiguration.getConstructorArguments().get(2)
                .getFieldConfigurationType());
        assertEquals("com.arondor.viewer.client.toppanel.behavior.document.DocumentPrintHandler", objectConfiguration
                .getConstructorArguments().get(2).getObjectConfiguration().getClassName());

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

        assertEquals(FieldConfigurationType.Object_Single, objectConfiguration.getConstructorArguments().get(0)
                .getFieldConfigurationType());

        ObjectConfiguration enumConfig = objectConfiguration.getConstructorArguments().get(0).getObjectConfiguration();
        assertNotNull(enumConfig);
        assertEquals("com.arondor.viewer.client.events.downupload.AskDocumentUploadEvent.Behavior",
                enumConfig.getClassName());

        assertEquals(1, enumConfig.getConstructorArguments().size());

        assertEquals(FieldConfigurationType.Primitive_Single, enumConfig.getConstructorArguments().get(0)
                .getFieldConfigurationType());
        assertEquals("UPLOAD_FILE", enumConfig.getConstructorArguments().get(0).getValue());
    }
}

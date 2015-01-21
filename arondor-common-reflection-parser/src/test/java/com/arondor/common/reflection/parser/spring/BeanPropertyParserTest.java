package com.arondor.common.reflection.parser.spring;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.config.TypedStringValue;
import org.springframework.beans.factory.support.ManagedList;

import com.arondor.common.reflection.api.hash.NoHashHelper;
import com.arondor.common.reflection.bean.config.ObjectConfigurationFactoryBean;
import com.arondor.common.reflection.model.config.ElementConfiguration;
import com.arondor.common.reflection.model.config.ElementConfiguration.ElementConfigurationType;
import com.arondor.common.reflection.model.config.ListConfiguration;
import com.arondor.common.reflection.model.config.ObjectConfiguration;
import com.arondor.common.reflection.model.config.PrimitiveConfiguration;
import com.arondor.common.reflection.model.config.ReferenceConfiguration;

public class BeanPropertyParserTest
{
    private BeanPropertyParser beanPropertyParser;

    private ObjectConfiguration objectConfigurationMock;

    @Before
    public void setUp() throws Exception
    {
        objectConfigurationMock = mock(ObjectConfiguration.class);
        when(objectConfigurationMock.getFieldConfigurationType()).thenReturn(ElementConfigurationType.Object);
        beanPropertyParser = new BeanPropertyParser(new NoHashHelper(), new ObjectConfigurationFactoryBean())
        {
            @Override
            public ObjectConfiguration parseBeanDefinition(BeanDefinition beanDefinition)
            {
                return objectConfigurationMock;
            }

            @Override
            public ObjectConfiguration parseBeanDefinition(String beanDefinitionName)
            {
                return objectConfigurationMock;
            }
        };
    }

    @Test(expected = RuntimeException.class)
    public void testParsePropertyInvalid() throws Exception
    {
        beanPropertyParser.parseProperty(String.class);
    }

    @Test
    public void testParsePropertyEnum() throws Exception
    {
        TypedStringValue enumPropertyValue = mockTypedStringValue("my enum type", "my enum type value");

        ElementConfiguration parsedEnumFieldConfiguration = beanPropertyParser.parseProperty(enumPropertyValue);

        assertEquals(ElementConfigurationType.Object, parsedEnumFieldConfiguration.getFieldConfigurationType());
        assertTrue(parsedEnumFieldConfiguration instanceof ObjectConfiguration);
        ObjectConfiguration enumObjectConfiguration = (ObjectConfiguration) parsedEnumFieldConfiguration;
        assertEquals(enumPropertyValue.getTargetTypeName(), enumObjectConfiguration.getClassName());
        ElementConfiguration actual = enumObjectConfiguration.getConstructorArguments().get(0);
        assertEquals(ElementConfigurationType.Primitive, actual.getFieldConfigurationType());
        PrimitiveConfiguration primitiveConfiguration = (PrimitiveConfiguration) actual;
        assertEquals(enumPropertyValue.getValue(), primitiveConfiguration.getValue());
    }

    @Test
    public void testParsePropertyClassic() throws Exception
    {
        TypedStringValue property = mockTypedStringValue(null, "my string value");
        ElementConfiguration parsedFieldConfiguration = beanPropertyParser.parseProperty(property);
        assertEquals(ElementConfigurationType.Primitive, parsedFieldConfiguration.getFieldConfigurationType());
        assertEquals(property.getValue(), ((PrimitiveConfiguration) parsedFieldConfiguration).getValue());
    }

    @Test
    public void testParsePropertyWithBasicSPELValue() throws Exception
    {
        TypedStringValue property = mockTypedStringValue(null, "#{ true or false }");

        ElementConfiguration parsedFieldConfiguration = beanPropertyParser.parseProperty(property);
        assertEquals(ElementConfigurationType.Primitive, parsedFieldConfiguration.getFieldConfigurationType());
        assertEquals("true", ((PrimitiveConfiguration) parsedFieldConfiguration).getValue());

        beanPropertyParser.setEnableSPEL(false);
        parsedFieldConfiguration = beanPropertyParser.parseProperty(property);
        assertEquals(ElementConfigurationType.Primitive, parsedFieldConfiguration.getFieldConfigurationType());
        assertEquals("#{ true or false }", ((PrimitiveConfiguration) parsedFieldConfiguration).getValue());
    }

    @Test
    public void testParsePropertyClassicList()
    {
        ManagedList<TypedStringValue> list = new ManagedList<TypedStringValue>();
        mock(ManagedList.class);
        list.add(mockTypedStringValue(null, "my first value"));
        list.add(mockTypedStringValue(null, "my second value"));

        ElementConfiguration parsedFieldConfiguration = beanPropertyParser.parseProperty(list);
        assertEquals(ElementConfigurationType.List, parsedFieldConfiguration.getFieldConfigurationType());
        ListConfiguration listConfiguration = (ListConfiguration) parsedFieldConfiguration;

        assertEquals(2, listConfiguration.getListConfiguration().size());

        assertEquals(ElementConfigurationType.Primitive, listConfiguration.getListConfiguration().get(0)
                .getFieldConfigurationType());
        assertEquals(ElementConfigurationType.Primitive, listConfiguration.getListConfiguration().get(1)
                .getFieldConfigurationType());
        assertEquals(list.get(0).getValue(),
                ((PrimitiveConfiguration) listConfiguration.getListConfiguration().get(0)).getValue());
        assertEquals(list.get(1).getValue(),
                ((PrimitiveConfiguration) listConfiguration.getListConfiguration().get(1)).getValue());
    }

    @Test
    public void testParsePropertyBean() throws Exception
    {
        RuntimeBeanReference beanReference = mockRuntimeBeanReference("my referenced bean name");
        ElementConfiguration parsedFieldConfiguration = beanPropertyParser.parseProperty(beanReference);

        assertEquals(ElementConfigurationType.Reference, parsedFieldConfiguration.getFieldConfigurationType());
        assertEquals(beanReference.getBeanName(),
                ((ReferenceConfiguration) parsedFieldConfiguration).getReferenceName());
    }

    @Test
    public void testParsePropertyBeanList() throws Exception
    {
        ManagedList<RuntimeBeanReference> list = new ManagedList<RuntimeBeanReference>();
        list.add(mockRuntimeBeanReference("my referenced bean name 1"));
        list.add(mockRuntimeBeanReference("my referenced bean name 2"));
        ElementConfiguration parsedFieldConfiguration = beanPropertyParser.parseProperty(list);

        assertEquals(ElementConfigurationType.List, parsedFieldConfiguration.getFieldConfigurationType());
        ListConfiguration listConfiguration = (ListConfiguration) parsedFieldConfiguration;

        assertEquals(2, listConfiguration.getListConfiguration().size());
    }

    @Test
    public void testParsePropertyBeanDefinitionHolder() throws Exception
    {
        BeanDefinitionHolder beanReference = mock(BeanDefinitionHolder.class);
        when(beanReference.getBeanName()).thenReturn("my referenced bean name");
        ElementConfiguration parsedFieldConfiguration = beanPropertyParser.parseProperty(beanReference);
        assertNotNull(parsedFieldConfiguration);
        assertEquals(ElementConfigurationType.Object, parsedFieldConfiguration.getFieldConfigurationType());
        assertEquals(objectConfigurationMock, (ObjectConfiguration) parsedFieldConfiguration);
    }

    private TypedStringValue mockTypedStringValue(String typeName, String value)
    {
        TypedStringValue enumPropertyValue = mock(TypedStringValue.class);
        when(enumPropertyValue.getTargetTypeName()).thenReturn(typeName);
        when(enumPropertyValue.getValue()).thenReturn(value);
        return enumPropertyValue;
    }

    private RuntimeBeanReference mockRuntimeBeanReference(String beanReferenceName)
    {
        RuntimeBeanReference beanReference = mock(RuntimeBeanReference.class);
        when(beanReference.getBeanName()).thenReturn(beanReferenceName);
        return beanReference;
    }

}

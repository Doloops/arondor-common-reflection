package com.arondor.common.reflection.parser.spring;

import static org.junit.Assert.assertEquals;
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
import com.arondor.common.reflection.model.config.FieldConfiguration;
import com.arondor.common.reflection.model.config.FieldConfiguration.FieldConfigurationType;
import com.arondor.common.reflection.model.config.ObjectConfiguration;

public class BeanPropertyParserTest
{
    private BeanPropertyParser beanPropertyParser;

    private ObjectConfiguration objectConfigurationMock;

    @Before
    public void setUp() throws Exception
    {
        objectConfigurationMock = mock(ObjectConfiguration.class);
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
                // TODO Auto-generated method stub
                return null;
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

        FieldConfiguration parsedEnumFieldConfiguration = beanPropertyParser.parseProperty(enumPropertyValue);

        assertEquals(FieldConfigurationType.Object_Single, parsedEnumFieldConfiguration.getFieldConfigurationType());
        ObjectConfiguration enumObjectConfiguration = parsedEnumFieldConfiguration.getObjectConfiguration();
        assertEquals(enumPropertyValue.getTargetTypeName(), enumObjectConfiguration.getClassName());
        FieldConfiguration actual = enumObjectConfiguration.getConstructorArguments().get(0);
        assertEquals(FieldConfigurationType.Primitive_Single, actual.getFieldConfigurationType());
        assertEquals(enumPropertyValue.getValue(), actual.getValue());
    }

    @Test
    public void testParsePropertyClassic() throws Exception
    {
        TypedStringValue property = mockTypedStringValue(null, "my string value");
        FieldConfiguration parsedFieldConfiguration = beanPropertyParser.parseProperty(property);
        assertEquals(FieldConfigurationType.Primitive_Single, parsedFieldConfiguration.getFieldConfigurationType());
        assertEquals(property.getValue(), parsedFieldConfiguration.getValue());
    }

    @Test
    public void testParsePropertyClassicList()
    {
        ManagedList<TypedStringValue> list = new ManagedList<TypedStringValue>();
        mock(ManagedList.class);
        list.add(mockTypedStringValue(null, "my first value"));
        list.add(mockTypedStringValue(null, "my second value"));

        FieldConfiguration parsedFieldConfiguration = beanPropertyParser.parseProperty(list);
        assertEquals(FieldConfigurationType.Primitive_Multiple, parsedFieldConfiguration.getFieldConfigurationType());
        assertEquals(2, parsedFieldConfiguration.getValues().size());
        assertEquals(list.get(0).getValue(), parsedFieldConfiguration.getValues().get(0));
        assertEquals(list.get(1).getValue(), parsedFieldConfiguration.getValues().get(1));
    }

    @Test
    public void testParsePropertyBean() throws Exception
    {
        RuntimeBeanReference beanReference = mockRuntimeBeanReference("my referenced bean name");
        FieldConfiguration parsedFieldConfiguration = beanPropertyParser.parseProperty(beanReference);

        assertEquals(FieldConfigurationType.Object_Single, parsedFieldConfiguration.getFieldConfigurationType());
        assertEquals(beanReference.getBeanName(), parsedFieldConfiguration.getObjectConfiguration().getReferenceName());
    }

    @Test
    public void testParsePropertyBeanList() throws Exception
    {
        ManagedList<RuntimeBeanReference> list = new ManagedList<RuntimeBeanReference>();
        list.add(mockRuntimeBeanReference("my referenced bean name 1"));
        list.add(mockRuntimeBeanReference("my referenced bean name 2"));
        FieldConfiguration parsedFieldConfiguration = beanPropertyParser.parseProperty(list);

        assertEquals(FieldConfigurationType.Object_Multiple, parsedFieldConfiguration.getFieldConfigurationType());
        assertEquals(2, parsedFieldConfiguration.getObjectConfigurations().size());
    }

    @Test
    public void testParsePropertyBeanDefinitionHolder() throws Exception
    {
        BeanDefinitionHolder beanReference = mock(BeanDefinitionHolder.class);
        when(beanReference.getBeanName()).thenReturn("my referenced bean name");
        FieldConfiguration parsedFieldConfiguration = beanPropertyParser.parseProperty(beanReference);
        assertEquals(FieldConfigurationType.Object_Single, parsedFieldConfiguration.getFieldConfigurationType());
        assertEquals(objectConfigurationMock, parsedFieldConfiguration.getObjectConfiguration());
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

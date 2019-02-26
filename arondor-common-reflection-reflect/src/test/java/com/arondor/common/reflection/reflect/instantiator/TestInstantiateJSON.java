package com.arondor.common.reflection.reflect.instantiator;

import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

import javax.management.ReflectionException;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.arondor.common.reflection.api.instantiator.InstantiationContext;
import com.arondor.common.reflection.api.instantiator.ReflectionInstantiator;
import com.arondor.common.reflection.bean.config.ObjectConfigurationBean;
import com.arondor.common.reflection.bean.config.ObjectConfigurationFactoryBean;
import com.arondor.common.reflection.model.config.ElementConfiguration;
import com.arondor.common.reflection.model.config.ObjectConfiguration;
import com.arondor.common.reflection.model.config.ObjectConfigurationFactory;
import com.arondor.common.reflection.reflect.testclasses.TestClassA;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TestInstantiateJSON
{
    private final static Logger LOG = Logger.getLogger(TestInstantiateJSON.class);

    private InstantiationContext instantationContext;

    private ReflectionInstantiator reflectionInstantiator;

    private ObjectConfigurationFactory objectConfigurationFactory = new ObjectConfigurationFactoryBean();

    @Before
    public void initialize() throws FileNotFoundException, ClassNotFoundException, NoSuchMethodException,
            SecurityException, InstantiationException, IllegalAccessException
    {
        ReflectionInstantiator reflectionInstantiator = new ReflectionInstantiatorReflect();
        this.reflectionInstantiator = reflectionInstantiator;
        this.instantationContext = this.reflectionInstantiator.createDefaultInstantiationContext();
        // this.parser = new JavaAccessibleClassParser();
    }

    @Test
    public void testGenerateJSON() throws ReflectionException, JsonProcessingException
    {
        ObjectConfiguration configuration = objectConfigurationFactory.createObjectConfiguration();
        configuration.setClassName(TestClassA.class.getName());

        configuration.setFields(new HashMap<String, ElementConfiguration>());

        ElementConfiguration fieldConf1 = objectConfigurationFactory
                .createPrimitiveConfiguration("Value for property1");
        configuration.getFields().put("property1", fieldConf1);

        ElementConfiguration fieldConf2 = objectConfigurationFactory.createPrimitiveConfiguration("82377");
        configuration.getFields().put("property2", fieldConf2);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
        String json = objectMapper.writeValueAsString(configuration);

        LOG.info("Serialized JSON : " + json);
    }

    @Test
    public void instantiateJSON() throws JsonParseException, JsonMappingException, IOException
    {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
        ObjectConfiguration configuration = objectMapper.readValue(new File("src/test/resources/TestClassA.json"),
                ObjectConfigurationBean.class);

        Object objectA = reflectionInstantiator.instanciateObject(configuration, Object.class, instantationContext);
        assertNotNull(objectA);

        Assert.assertTrue(objectA instanceof TestClassA);
        TestClassA testClassA = (TestClassA) objectA;

        Assert.assertEquals("Serialized from JSON", testClassA.getProperty1());
    }
}

package com.arondor.common.reflection.reflect.instantiator;

import static org.junit.Assert.assertEquals;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import com.arondor.common.reflection.api.instantiator.InstantiationContext;
import com.arondor.common.reflection.api.instantiator.ReflectionInstantiator;
import com.arondor.common.reflection.bean.config.ObjectConfigurationFactoryBean;
import com.arondor.common.reflection.model.config.ElementConfiguration;
import com.arondor.common.reflection.model.config.ObjectConfiguration;
import com.arondor.common.reflection.model.config.ObjectConfigurationFactory;

public class TestSpecialPrimitiveCases
{
    private final static Logger LOG = Logger.getLogger(TestReflectionInstantiatorReflect.class);

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
    public void testString()
    {
        ObjectConfiguration objectConfiguration = objectConfigurationFactory.createObjectConfiguration();
        objectConfiguration.setClassName(String.class.getName());
        objectConfiguration.setFields(new HashMap<String, ElementConfiguration>());
        objectConfiguration.setConstructorArguments(new ArrayList<ElementConfiguration>());
        objectConfiguration.getConstructorArguments().add(
                objectConfigurationFactory.createPrimitiveConfiguration("myValue"));

        String result = reflectionInstantiator
                .instanciateObject(objectConfiguration, String.class, instantationContext);
        assertEquals("myValue", result);
    }
}

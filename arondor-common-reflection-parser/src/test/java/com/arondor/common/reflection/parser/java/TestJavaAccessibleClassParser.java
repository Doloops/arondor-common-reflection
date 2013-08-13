package com.arondor.common.reflection.parser.java;

import java.util.Map;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

import com.arondor.common.management.mbean.annotation.Description;
import com.arondor.common.reflection.api.parser.AccessibleClassParser;
import com.arondor.common.reflection.model.java.AccessibleClass;
import com.arondor.common.reflection.model.java.AccessibleField;
import com.arondor.common.reflection.model.java.AccessibleMethod;

public class TestJavaAccessibleClassParser
{
    private static final Logger log = Logger.getLogger(TestJavaAccessibleClassParser.class);

    public static class SimpleClass
    {
        @Description("Dummy map")
        private Map<String, Long> map;

        public void setMap(Map<String, Long> map)
        {
            this.map = map;
        }

        public Map<String, Long> getMap()
        {
            return map;
        }

        public void myLittleMethod(String truff)
        {

        }

    }

    @Test
    public void testSimpleClass()
    {
        AccessibleClassParser parser = new JavaAccessibleClassParser();

        AccessibleClass clazz = parser.parseAccessibleClass(SimpleClass.class);

        Assert.assertNotNull(clazz);
        log.info("Class : " + clazz.getClassBaseName());

        for (String fieldName : clazz.getAccessibleFields().keySet())
        {
            AccessibleField field = clazz.getAccessibleFields().get(fieldName);
            log.info("* Field : " + fieldName + ", class=" + field.getClassName());
        }

        Assert.assertEquals(1, clazz.getAccessibleFields().size());

        AccessibleField field = clazz.getAccessibleFields().get("map");
        Assert.assertNotNull(field);

        Assert.assertEquals("map", field.getName());
        Assert.assertEquals("java.util.Map", field.getClassName());
        Assert.assertEquals(2, field.getGenericParameterClassList().size());
        Assert.assertEquals("java.lang.String", field.getGenericParameterClassList().get(0));
        Assert.assertEquals("java.lang.Long", field.getGenericParameterClassList().get(1));

        Assert.assertEquals("Dummy map", field.getDescription());

        Assert.assertNotNull(clazz.getAccessibleMethods());
        for (AccessibleMethod mth : clazz.getAccessibleMethods())
        {
            log.info("Method : " + mth.getName());
        }

        Assert.assertEquals(1, clazz.getAccessibleMethods().size());
        Assert.assertEquals("myLittleMethod", clazz.getAccessibleMethods().get(0).getName());
    }

}

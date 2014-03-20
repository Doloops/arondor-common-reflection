package com.arondor.common.reflection.gwt.server;

import java.util.Collection;
import java.util.Map;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.arondor.common.reflection.gwt.server.samples.TestClass;
import com.arondor.common.reflection.gwt.server.samples.TestClassTer;
import com.arondor.common.reflection.gwt.server.samples.TestInterface;
import com.arondor.common.reflection.model.java.AccessibleClass;
import com.arondor.common.reflection.model.java.AccessibleField;

public class TestDefaultGWTReflectionService
{
    private static final Logger LOG = Logger.getLogger(TestDefaultGWTReflectionService.class);

    private DefaultGWTReflectionService gwtReflectionService;

    @Before
    public void init()
    {
        gwtReflectionService = new DefaultGWTReflectionService();
    }

    @Test
    public void testImplementingClasses()
    {
        Collection<AccessibleClass> result = gwtReflectionService.getImplementingAccessibleClasses(TestInterface.class
                .getName());
        Assert.assertEquals(2, result.size());
        Assert.assertTrue(result.contains(gwtReflectionService.getAccessibleClass(TestClass.class.getName())));
        Assert.assertTrue(result.contains(gwtReflectionService.getAccessibleClass(TestClassTer.class.getName())));

        Collection<AccessibleClass> allResults = gwtReflectionService
                .getImplementingAccessibleClasses(java.lang.Object.class.getName());

        LOG.info("All classes parsed : " + allResults.size());
    }

    @Test
    public void testAccessibleField()
    {

        AccessibleClass testClass = gwtReflectionService.getAccessibleClass(TestClass.class.getName());

        Map<String, AccessibleField> fieldMap = testClass.getAccessibleFields();
        Assert.assertNotNull(fieldMap);
        Assert.assertEquals(5, fieldMap.size());
        AccessibleField stringField = fieldMap.get("aStringProperty");
        Assert.assertNotNull(stringField);
        Assert.assertEquals("This is a string property and it is mandatory", stringField.getDescription());
        Assert.assertTrue(stringField.isMandatory());
        Assert.assertTrue(fieldMap.containsKey("aLongProperty"));
        Assert.assertTrue(fieldMap.containsKey("subClass"));
        Assert.assertTrue(fieldMap.containsKey("aBooleanProperty"));
        Assert.assertTrue(fieldMap.containsKey("anEnumProperty"));
        // Assert.assertTrue(fieldMap.get("anEnumProperty").isEnumProperty());
        //
        // Map<String, List<String>> enumMap = testClass.getAccessibleEnums();
        // Assert.assertNotNull(enumMap);
        // Assert.assertEquals(1, enumMap.size());
        // List<String> testEnumValues = enumMap.get(TestEnum.class.getName());
        // Assert.assertEquals(2, testEnumValues);
        // Assert.assertTrue(testEnumValues.contains(TestEnum.VALUE1.name()));
        // Assert.assertTrue(testEnumValues.contains(TestEnum.VALUE2.name()));
    }
}

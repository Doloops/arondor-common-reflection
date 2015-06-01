package com.arondor.common.reflection.gwt.server;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import com.arondor.common.reflection.gwt.server.samples.TestClass;
import com.arondor.common.reflection.gwt.server.samples.TestClassTer;
import com.arondor.common.reflection.gwt.server.samples.TestEnum;
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
        assertEquals(2, result.size());
        assertTrue(result.contains(gwtReflectionService.getAccessibleClass(TestClass.class.getName())));
        assertTrue(result.contains(gwtReflectionService.getAccessibleClass(TestClassTer.class.getName())));

        Collection<AccessibleClass> allResults = gwtReflectionService
                .getImplementingAccessibleClasses(java.lang.Object.class.getName());

        LOG.info("All classes parsed : " + allResults.size());
    }

    @Test
    public void testAccessibleField()
    {

        AccessibleClass testClass = gwtReflectionService.getAccessibleClass(TestClass.class.getName());

        Map<String, AccessibleField> fieldMap = testClass.getAccessibleFields();
        assertNotNull(fieldMap);

        // assertEquals(5, fieldMap.size());
        AccessibleField stringField = fieldMap.get("aStringProperty");
        assertNotNull(stringField);
        assertEquals("This is a string property and it is mandatory", stringField.getDescription());
        assertTrue(stringField.isMandatory());
        assertTrue(fieldMap.containsKey("aLongProperty"));
        assertTrue(fieldMap.containsKey("subClass"));
        assertTrue(fieldMap.containsKey("aBooleanProperty"));
        assertTrue(fieldMap.containsKey("anEnumProperty"));
        assertTrue(fieldMap.get("anEnumProperty").isEnumProperty());
        //
        Map<String, List<String>> enumMap = testClass.getAccessibleEnums();
        assertNotNull(enumMap);
        assertEquals(1, enumMap.size());
        List<String> testEnumValues = enumMap.get(TestEnum.class.getName());
        assertEquals(2, testEnumValues.size());
        assertTrue(testEnumValues.contains(TestEnum.VALUE1.name()));
        assertTrue(testEnumValues.contains(TestEnum.VALUE2.name()));
    }
}

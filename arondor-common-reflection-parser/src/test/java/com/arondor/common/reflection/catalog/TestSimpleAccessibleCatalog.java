package com.arondor.common.reflection.catalog;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.arondor.common.reflection.api.catalog.AccessibleClassCatalog;
import com.arondor.common.reflection.api.parser.AccessibleClassParser;
import com.arondor.common.reflection.catalog.SimpleAccessibleClassCatalog;
import com.arondor.common.reflection.model.java.AccessibleClass;
import com.arondor.common.reflection.parser.java.JavaAccessibleClassParser;

public class TestSimpleAccessibleCatalog
{
    public static interface InterfaceA
    {
        int getMyInt();

        String getMyString();
    }

    public static class ClassA implements InterfaceA
    {
        private int myInt;

        private String myString;

        public int getMyInt()
        {
            return myInt;
        }

        public void setMyInt(int myInt)
        {
            this.myInt = myInt;
        }

        public String getMyString()
        {
            return myString;
        }

        public void setMyString(String myString)
        {
            this.myString = myString;
        }
    }

    private AccessibleClassParser accessibleClassParser;

    private AccessibleClassCatalog accessibleClassCatalog;

    @Before
    public void init()
    {
        accessibleClassParser = new JavaAccessibleClassParser();
        accessibleClassCatalog = new SimpleAccessibleClassCatalog();
        accessibleClassCatalog.addAccessibleClass(accessibleClassParser.parseAccessibleClass(InterfaceA.class));
        accessibleClassCatalog.addAccessibleClass(accessibleClassParser.parseAccessibleClass(ClassA.class));
    }

    @Test
    public void testSimpleGet() throws ClassNotFoundException
    {
        AccessibleClass interfaceA = accessibleClassCatalog.getAccessibleClass(InterfaceA.class.getName());
        assertNotNull(interfaceA);
        assertEquals(InterfaceA.class.getName(), interfaceA.getName());

        AccessibleClass classA = accessibleClassCatalog.getAccessibleClass(ClassA.class.getName());
        assertNotNull(classA);
        assertEquals(ClassA.class.getName(), classA.getName());
    }

    @Test
    public void testParsedInheritance() throws ClassNotFoundException
    {
        AccessibleClass classA = accessibleClassCatalog.getAccessibleClass(ClassA.class.getName());
        List<String> interfaces = classA.getAllInterfaces();
        assertEquals(2, interfaces.size());
        assertEquals(Object.class.getName(), interfaces.get(0));
        assertEquals(InterfaceA.class.getName(), interfaces.get(1));
    }

    @Test
    public void testRetrieveInheritance()
    {
        Collection<AccessibleClass> inherited = accessibleClassCatalog
                .getImplementingAccessibleClasses(InterfaceA.class.getName());

        assertNotNull(inherited);
        assertEquals(1, inherited.size());

        AccessibleClass clazz = inherited.iterator().next();

        assertEquals(ClassA.class.getName(), clazz.getName());
    }
}

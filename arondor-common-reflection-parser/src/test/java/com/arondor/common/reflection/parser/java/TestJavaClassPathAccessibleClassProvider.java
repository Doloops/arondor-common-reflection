package com.arondor.common.reflection.parser.java;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;

import com.arondor.common.reflection.api.catalog.AccessibleClassCatalog;
import com.arondor.common.reflection.catalog.SimpleAccessibleClassCatalog;
import com.arondor.common.reflection.model.java.AccessibleClass;
import com.arondor.common.reflection.parser.java.testing.ClassA;
import com.arondor.common.reflection.parser.java.testing.InterfaceA;

public class TestJavaClassPathAccessibleClassProvider
{
    @Test
    public void testClassProvider() throws ClassNotFoundException
    {
        JavaClassPathAccessibleClassProvider provider = new JavaClassPathAccessibleClassProvider();
        List<String> packagePrefixes = new ArrayList<String>();
        packagePrefixes.add(ClassA.class.getPackage().getName());
        provider.setPackagePrefixes(packagePrefixes);
        AccessibleClassCatalog catalog = new SimpleAccessibleClassCatalog();
        provider.provideClasses(catalog);

        AccessibleClass accessibleClass = catalog.getAccessibleClass(ClassA.class.getName());
        assertNotNull(accessibleClass);

        Collection<AccessibleClass> inheritedCollection = catalog.getImplementingAccessibleClasses(InterfaceA.class
                .getName());

        assertNotNull(inheritedCollection);
        assertEquals(1, inheritedCollection.size());

        AccessibleClass inherited1 = inheritedCollection.iterator().next();
        assertEquals(ClassA.class.getName(), inherited1.getName());
    }
}

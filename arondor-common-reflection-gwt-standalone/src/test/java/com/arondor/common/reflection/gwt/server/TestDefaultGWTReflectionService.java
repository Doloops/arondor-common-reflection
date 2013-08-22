package com.arondor.common.reflection.gwt.server;

import java.util.Collection;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.arondor.common.reflection.gwt.server.samples.TestClass;
import com.arondor.common.reflection.gwt.server.samples.TestClassTer;
import com.arondor.common.reflection.gwt.server.samples.TestInterface;
import com.arondor.common.reflection.model.java.AccessibleClass;

public class TestDefaultGWTReflectionService
{
    private DefaultGWTReflectionService gwtReflectionService;

    @Before
    public void init()
    {
        gwtReflectionService = new DefaultGWTReflectionService();
    }

    @Test
    public void testImplementingClasses()
    {
        Collection<AccessibleClass> result = gwtReflectionService.getImplementingAccessibleClasses(TestInterface.class.getName());
        Assert.assertEquals(2, result.size());
        Assert.assertTrue(result.contains(gwtReflectionService.getAccessibleClass(TestClass.class.getName())));
        Assert.assertTrue(result.contains(gwtReflectionService.getAccessibleClass(TestClassTer.class.getName())));
        
    }
}

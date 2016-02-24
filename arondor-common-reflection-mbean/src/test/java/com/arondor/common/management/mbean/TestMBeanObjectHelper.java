package com.arondor.common.management.mbean;

import static org.junit.Assert.assertEquals;

import javax.management.MBeanInfo;

import org.junit.Before;
import org.junit.Test;

public class TestMBeanObjectHelper
{
    private MBeanObjectHelper mbeanObjectHelper;

    @Before
    public void init()
    {
        mbeanObjectHelper = new MBeanObjectHelper();
    }

    public static class Test1
    {
        private int field1;

        public int getField1()
        {
            return field1;
        }

        public void setField1(int field1)
        {
            this.field1 = field1;
        }
    }

    @Test
    public void testField1()
    {
        Test1 test1 = new Test1();
        MBeanInfo info = mbeanObjectHelper.getMBeanInfo(test1, "Description");

        assertEquals(1, info.getAttributes().length);
        assertEquals("field1", info.getAttributes()[0].getName());
        assertEquals("int", info.getAttributes()[0].getType());

        assertEquals(true, info.getAttributes()[0].isReadable());
        assertEquals(true, info.getAttributes()[0].isWritable());

    }

    public static final class TestFieldReadOnly
    {
        private float fieldReadOnly = 437.7f;

        public float getFieldReadOnly()
        {
            return fieldReadOnly;
        }

    }

    @Test
    public void testFieldReadOnly()
    {
        TestFieldReadOnly testFieldReadOnly = new TestFieldReadOnly();
        MBeanInfo info = mbeanObjectHelper.getMBeanInfo(testFieldReadOnly, "");

        assertEquals(1, info.getAttributes().length);
        assertEquals("fieldReadOnly", info.getAttributes()[0].getName());
        assertEquals("float", info.getAttributes()[0].getType());

        assertEquals(true, info.getAttributes()[0].isReadable());
        assertEquals(false, info.getAttributes()[0].isWritable());
    }

}

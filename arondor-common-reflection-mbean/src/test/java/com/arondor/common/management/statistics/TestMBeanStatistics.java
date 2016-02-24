package com.arondor.common.management.statistics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.lang.management.ManagementFactory;
import java.util.Set;

import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectInstance;
import javax.management.ObjectName;

import org.junit.Test;

import com.arondor.common.management.mbean.MBeanObject;

public class TestMBeanStatistics
{
    public Statistics stats = Statistics.getInstance();

    public class TestA extends MBeanObject
    {

        public TestA(String name)
        {
            super(name);
        }

        public void addStat(String name) throws InterruptedException
        {
            StatPoint statPoint = new StatPoint(this, name);
            Thread.sleep((long) (Math.random() * 200));
            stats.updateStat(statPoint);
        }

        private Boolean javaLangBooleanField = true;

        public void setJavaLangBooleanField(Boolean javaLangBooleanField)
        {
            this.javaLangBooleanField = javaLangBooleanField;
        }

        public Boolean getJavaLangBooleanField()
        {
            return javaLangBooleanField;
        }

        private boolean primitiveBooleanField = false;

        public void setPrimitiveBooleanField(boolean primitiveBooleanField)
        {
            this.primitiveBooleanField = primitiveBooleanField;
        }

        public boolean isPrimitiveBooleanField()
        {
            return primitiveBooleanField;
        }

        private String javaLangStringField = "A java.lang.String field";

        public void setJavaLangStringField(String javaLangStringField)
        {
            this.javaLangStringField = javaLangStringField;
        }

        public String getJavaLangStringField()
        {
            return javaLangStringField;
        }

        private long primitiveLongField = 47387;

        public void setPrimitiveLongField(long primitiveLongField)
        {
            this.primitiveLongField = primitiveLongField;
        }

        public long getPrimitiveLongField()
        {
            return primitiveLongField;
        }

        private int primitiveIntField = 42;

        public void setPrimitiveIntField(int primitiveIntField)
        {
            this.primitiveIntField = primitiveIntField;
        }

        public int getPrimitiveIntField()
        {
            return primitiveIntField;
        }

        private java.lang.Long javaLangLongField = 434343L;

        public void setJavaLangLongField(java.lang.Long javaLangLongField)
        {
            this.javaLangLongField = javaLangLongField;
        }

        public java.lang.Long getJavaLangLongField()
        {
            return javaLangLongField;
        }

        private java.lang.Integer javaLangIntegerField = 434342;

        public void setJavaLangIntegerField(java.lang.Integer javaLangIntegerField)
        {
            this.javaLangIntegerField = javaLangIntegerField;
        }

        public java.lang.Integer getJavaLangIntegerField()
        {
            return javaLangIntegerField;
        }

    }

    @Test
    public void testMBeanStats() throws InterruptedException, MalformedObjectNameException
    {
        TestA testA1 = new TestA("testA1");
        TestA testA2 = new TestA("testA2");

        testA1.addStat("My testA1.1");
        testA1.addStat("My testA1.2");

        testA2.addStat("My testA2.1");

        TestA testA2_Dupl = new TestA("testA3");
        testA2_Dupl.addStat("My testA2.1");

        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();

        ObjectName name = new ObjectName(TestA.class.getName(), "type", "My_testA1.1");
        Set<ObjectInstance> insts = mbs.queryMBeans(name, null);

        assertTrue(!insts.isEmpty());
        assertEquals(1, insts.size());
    }
}

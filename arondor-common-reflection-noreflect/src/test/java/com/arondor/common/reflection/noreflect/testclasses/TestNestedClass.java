package com.arondor.common.reflection.noreflect.testclasses;

public class TestNestedClass
{
    public static class EmbeddedClass
    {
        private String fieldInEmbedded;

        public String getFieldInEmbedded()
        {
            return fieldInEmbedded;
        }

        public void setFieldInEmbedded(String fieldInEmbedded)
        {
            this.fieldInEmbedded = fieldInEmbedded;
        }
    }

    private EmbeddedClass embeddedClass;

    public EmbeddedClass getEmbeddedClass()
    {
        return embeddedClass;
    }

    public void setEmbeddedClass(EmbeddedClass embeddedClass)
    {
        this.embeddedClass = embeddedClass;
    }
}

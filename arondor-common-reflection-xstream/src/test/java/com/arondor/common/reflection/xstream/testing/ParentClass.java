package com.arondor.common.reflection.xstream.testing;

public class ParentClass
{
    private ChildClass child;

    public ChildClass getChild()
    {
        return child;
    }

    public void setChild(ChildClass child)
    {
        this.child = child;
    }
}

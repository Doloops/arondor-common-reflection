package com.arondor.common.reflection.w3cgwt;

public class Attr extends Node implements com.google.gwt.xml.client.Attr
{
    protected Attr(org.w3c.dom.Attr impl)
    {
        super(impl);
    }

    private org.w3c.dom.Attr _as(com.google.gwt.xml.client.Attr gNode)
    {
        return (org.w3c.dom.Attr) _impl();
    }

    private org.w3c.dom.Attr _as()
    {
        return _as(this);
    }

    @Override
    public String getName()
    {
        return _as().getName();
    }

    @Override
    public boolean getSpecified()
    {
        return _as().getSpecified();
    }

    @Override
    public String getValue()
    {
        return _as().getValue();
    }
}

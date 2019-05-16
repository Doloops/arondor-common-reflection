package com.arondor.common.reflection.w3cgwt;

public class Element extends Node implements com.google.gwt.xml.client.Element
{
    protected Element(org.w3c.dom.Element impl)
    {
        super(impl);
    }

    private org.w3c.dom.Element _as(com.google.gwt.xml.client.Element gNode)
    {
        return (org.w3c.dom.Element) _impl();
    }

    private org.w3c.dom.Element _as()
    {
        return _as(this);
    }

    @Override
    public String getAttribute(String name)
    {
        return _as().getAttribute(name);
    }

    @Override
    public com.google.gwt.xml.client.Attr getAttributeNode(String name)
    {
        return Node._build(_as().getAttributeNode(name));
    }

    @Override
    public com.google.gwt.xml.client.NodeList getElementsByTagName(String name)
    {
        return NodeList._build(_as().getElementsByTagName(name));
    }

    @Override
    public String getTagName()
    {
        return _as().getTagName();
    }

    @Override
    public boolean hasAttribute(String name)
    {
        return _as().hasAttribute(name);
    }

    @Override
    public void removeAttribute(String name)
    {
        _as().removeAttribute(name);
    }

    @Override
    public void setAttribute(String name, String value)
    {
        _as().setAttribute(name, value);
    }
}

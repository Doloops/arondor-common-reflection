package com.arondor.common.w3c2gwt;

public class NamedNodeMap implements com.google.gwt.xml.client.NamedNodeMap
{
    private final org.w3c.dom.NamedNodeMap impl;

    private NamedNodeMap(org.w3c.dom.NamedNodeMap impl)
    {
        this.impl = impl;
    }

    protected static NamedNodeMap _build(org.w3c.dom.NamedNodeMap impl)
    {
        return new NamedNodeMap(impl);
    }

    private org.w3c.dom.NamedNodeMap _as(com.google.gwt.xml.client.NamedNodeMap gNode)
    {
        return ((NamedNodeMap) gNode).impl;
    }

    private org.w3c.dom.NamedNodeMap _as()
    {
        return _as(this);
    }

    @Override
    public int getLength()
    {
        return _as().getLength();
    }

    @Override
    public Node getNamedItem(String name)
    {
        return Node._build(_as().getNamedItem(name));
    }

    @Override
    public Node item(int index)
    {
        return Node._build(_as().item(index));
    }

}

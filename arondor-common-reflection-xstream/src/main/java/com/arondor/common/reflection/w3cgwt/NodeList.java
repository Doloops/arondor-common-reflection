package com.arondor.common.reflection.w3cgwt;

public class NodeList implements com.google.gwt.xml.client.NodeList
{
    private final org.w3c.dom.NodeList impl;

    private NodeList(org.w3c.dom.NodeList impl)
    {
        this.impl = impl;
    }

    protected static NodeList _build(org.w3c.dom.NodeList impl)
    {
        return new NodeList(impl);
    }

    private org.w3c.dom.NodeList _as(com.google.gwt.xml.client.NodeList gNode)
    {
        return ((NodeList) gNode).impl;
    }

    private org.w3c.dom.NodeList _as()
    {
        return _as(this);
    }

    @Override
    public int getLength()
    {
        return _as().getLength();
    }

    @Override
    public Node item(int index)
    {
        return Node._build(_as().item(index));
    }

}

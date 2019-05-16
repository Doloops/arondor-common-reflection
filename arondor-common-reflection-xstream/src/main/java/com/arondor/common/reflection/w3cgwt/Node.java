package com.arondor.common.reflection.w3cgwt;

import java.util.logging.Logger;

public abstract class Node implements com.google.gwt.xml.client.Node
{
    private static final Logger LOG = Logger.getLogger(Node.class.getName());

    private final org.w3c.dom.Node impl;

    protected Node(org.w3c.dom.Node impl)
    {
        this.impl = impl;
    }

    protected static <T extends com.google.gwt.xml.client.Node> T _build(org.w3c.dom.Node impl)
    {
        if (impl == null)
        {
            return null;
        }
        LOG.info("At node : " + impl.getNodeType());
        switch (impl.getNodeType())
        {
        case org.w3c.dom.Node.ELEMENT_NODE:
            return (T) new Element((org.w3c.dom.Element) impl);
        case org.w3c.dom.Node.ATTRIBUTE_NODE:
            return (T) new Attr((org.w3c.dom.Attr) impl);
        case org.w3c.dom.Node.TEXT_NODE:
            return (T) new Text((org.w3c.dom.Text) impl);
        case org.w3c.dom.Node.CDATA_SECTION_NODE:
            return (T) new CDATASection((org.w3c.dom.CDATASection) impl);
        }
        throw new IllegalArgumentException(
                "Not supported : type=" + impl.getNodeType() + ", class=" + impl.getClass().getName());
    }

    protected org.w3c.dom.Node _impl()
    {
        return impl;
    }

    protected static org.w3c.dom.Node _as(com.google.gwt.xml.client.Node gNode)
    {
        return ((Node) gNode).impl;
    }

    private org.w3c.dom.Node _as()
    {
        return _as(this);
    }

    @Override
    public com.google.gwt.xml.client.Node appendChild(com.google.gwt.xml.client.Node newChild)
    {
        return _build(impl.appendChild(_as(newChild)));
    }

    @Override
    public com.google.gwt.xml.client.Node cloneNode(boolean deep)
    {
        return _build(_as().cloneNode(deep));
    }

    @Override
    public com.google.gwt.xml.client.NamedNodeMap getAttributes()
    {
        return NamedNodeMap._build(_as().getAttributes());
    }

    @Override
    public com.google.gwt.xml.client.NodeList getChildNodes()
    {
        return NodeList._build(_as().getChildNodes());
    }

    @Override
    public com.google.gwt.xml.client.Node getFirstChild()
    {
        return _build(_as().getFirstChild());
    }

    @Override
    public com.google.gwt.xml.client.Node getLastChild()
    {
        return _build(_as().getLastChild());
    }

    @Override
    public String getNamespaceURI()
    {
        return _as().getNamespaceURI();
    }

    @Override
    public com.google.gwt.xml.client.Node getNextSibling()
    {
        return _build(_as().getNextSibling());
    }

    @Override
    public String getNodeName()
    {
        return _as().getNodeName();
    }

    @Override
    public short getNodeType()
    {
        return _as().getNodeType();
    }

    @Override
    public String getNodeValue()
    {
        return _as().getNodeValue();
    }

    @Override
    public com.google.gwt.xml.client.Document getOwnerDocument()
    {
        return Document._build(_as().getOwnerDocument());
    }

    @Override
    public com.google.gwt.xml.client.Node getParentNode()
    {
        return _build(_as().getParentNode());
    }

    @Override
    public String getPrefix()
    {
        return _as().getPrefix();
    }

    @Override
    public com.google.gwt.xml.client.Node getPreviousSibling()
    {
        return _build(_as().getPreviousSibling());
    }

    @Override
    public boolean hasAttributes()
    {
        return _as().hasAttributes();
    }

    @Override
    public boolean hasChildNodes()
    {
        return _as().hasChildNodes();
    }

    @Override
    public com.google.gwt.xml.client.Node insertBefore(com.google.gwt.xml.client.Node newChild,
            com.google.gwt.xml.client.Node refChild)
    {
        return _build(_as().insertBefore(_as(newChild), _as(refChild)));
    }

    @Override
    public void normalize()
    {
        _as().normalize();
    }

    @Override
    public com.google.gwt.xml.client.Node removeChild(com.google.gwt.xml.client.Node oldChild)
    {
        return _build(_as().removeChild(_as(oldChild)));
    }

    @Override
    public com.google.gwt.xml.client.Node replaceChild(com.google.gwt.xml.client.Node newChild,
            com.google.gwt.xml.client.Node oldChild)
    {
        return _build(_as().replaceChild(_as(newChild), _as(oldChild)));
    }

    @Override
    public void setNodeValue(String nodeValue)
    {
        _as().setNodeValue(nodeValue);
    }
}

package com.arondor.common.reflection.w3cgwt;

public class CDATASection extends Text implements com.google.gwt.xml.client.CDATASection
{
    protected CDATASection(org.w3c.dom.CDATASection impl)
    {
        super(impl);
    }

    private org.w3c.dom.CDATASection _as(com.google.gwt.xml.client.CDATASection gNode)
    {
        return (org.w3c.dom.CDATASection) _impl();
    }

    private org.w3c.dom.CDATASection _as()
    {
        return _as(this);
    }
}

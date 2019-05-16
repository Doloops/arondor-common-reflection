package com.arondor.common.w3c2gwt;

public class Comment extends CharacterData implements com.google.gwt.xml.client.Comment
{
    protected Comment(org.w3c.dom.Comment impl)
    {
        super(impl);
    }

    private org.w3c.dom.Comment _as(com.google.gwt.xml.client.Comment gNode)
    {
        return (org.w3c.dom.Comment) _impl();
    }

    private org.w3c.dom.Comment _as()
    {
        return _as(this);
    }
}

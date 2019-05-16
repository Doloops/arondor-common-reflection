package com.arondor.common.w3c2gwt;

public class Text extends CharacterData implements com.google.gwt.xml.client.Text
{
    protected Text(org.w3c.dom.Text impl)
    {
        super(impl);
    }

    private org.w3c.dom.Text _as(com.google.gwt.xml.client.Text gNode)
    {
        return (org.w3c.dom.Text) _impl();
    }

    private org.w3c.dom.Text _as()
    {
        return _as(this);
    }

    @Override
    public com.google.gwt.xml.client.Text splitText(int offset)
    {
        return new Text(_as().splitText(offset));
    }
}

package com.arondor.common.w3c2gwt;

public class ProcessingInstruction extends Node implements com.google.gwt.xml.client.ProcessingInstruction
{
    protected ProcessingInstruction(org.w3c.dom.Comment impl)
    {
        super(impl);
    }

    private org.w3c.dom.ProcessingInstruction _as(com.google.gwt.xml.client.ProcessingInstruction gNode)
    {
        return (org.w3c.dom.ProcessingInstruction) _impl();
    }

    private org.w3c.dom.ProcessingInstruction _as()
    {
        return _as(this);
    }

    @Override
    public String getData()
    {
        return _as().getData();
    }

    @Override
    public String getTarget()
    {
        return _as().getTarget();
    }

    @Override
    public void setData(String data)
    {
        _as().setData(data);
    }
}

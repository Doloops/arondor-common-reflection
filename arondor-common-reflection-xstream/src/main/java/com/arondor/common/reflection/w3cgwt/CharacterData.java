package com.arondor.common.reflection.w3cgwt;

public class CharacterData extends Node implements com.google.gwt.xml.client.CharacterData
{

    public CharacterData(org.w3c.dom.CharacterData impl)
    {
        super(impl);
    }

    private org.w3c.dom.CharacterData _as(com.google.gwt.xml.client.CharacterData gNode)
    {
        return (org.w3c.dom.CharacterData) _impl();
    }

    private org.w3c.dom.CharacterData _as()
    {
        return _as(this);
    }

    @Override
    public void appendData(String appendedData)
    {
        _as().appendData(appendedData);
    }

    @Override
    public void deleteData(int offset, int count)
    {
        _as().deleteData(offset, count);
    }

    @Override
    public String getData()
    {
        return _as().getData();
    }

    @Override
    public int getLength()
    {
        return _as().getLength();
    }

    @Override
    public void insertData(int offset, String insertedData)
    {
        _as().insertData(offset, insertedData);
    }

    @Override
    public void replaceData(int offset, int count, String replacementData)
    {
        _as().replaceData(offset, count, replacementData);
    }

    @Override
    public void setData(String data)
    {
        _as().setData(data);
    }

    @Override
    public String substringData(int offset, int count)
    {
        return _as().substringData(offset, count);
    }
}

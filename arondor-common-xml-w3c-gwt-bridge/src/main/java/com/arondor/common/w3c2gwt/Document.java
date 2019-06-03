package com.arondor.common.w3c2gwt;

import java.io.StringWriter;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class Document extends Node implements com.google.gwt.xml.client.Document
{
    private Document(org.w3c.dom.Document impl)
    {
        super(impl);
    }

    protected static Document _build(org.w3c.dom.Document impl)
    {
        return new Document(impl);
    }

    private org.w3c.dom.Document _as(com.google.gwt.xml.client.Document gNode)
    {
        return (org.w3c.dom.Document) _impl();
    }

    private org.w3c.dom.Document _as()
    {
        return _as(this);
    }

    @Override
    public com.google.gwt.xml.client.CDATASection createCDATASection(String data)
    {
        return Node._build(_as().createCDATASection(data));
    }

    @Override
    public com.google.gwt.xml.client.Comment createComment(String data)
    {
        return Node._build(_as().createComment(data));
    }

    @Override
    public com.google.gwt.xml.client.DocumentFragment createDocumentFragment()
    {
        return Node._build(_as().createDocumentFragment());
    }

    @Override
    public com.google.gwt.xml.client.Element createElement(String tagName)
    {
        return Node._build(_as().createElement(tagName));
    }

    @Override
    public com.google.gwt.xml.client.ProcessingInstruction createProcessingInstruction(String target, String data)
    {
        return Node._build(_as().createProcessingInstruction(target, data));
    }

    @Override
    public com.google.gwt.xml.client.Text createTextNode(String data)
    {
        return Node._build(_as().createTextNode(data));
    }

    @Override
    public com.google.gwt.xml.client.Element getDocumentElement()
    {
        return Node._build(_as().getDocumentElement());
    }

    @Override
    public com.google.gwt.xml.client.Element getElementById(String elementId)
    {
        return Node._build(_as().getElementById(elementId));
    }

    @Override
    public com.google.gwt.xml.client.NodeList getElementsByTagName(String tagname)
    {
        return NodeList._build(_as().getElementsByTagName(tagname));
    }

    @Override
    public com.google.gwt.xml.client.Node importNode(com.google.gwt.xml.client.Node importedNode, boolean deep)
    {
        return Node._build(_as().importNode(Node._as(importedNode), deep));
    }

    @Override
    public String toString()
    {
        try
        {
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer trans = tf.newTransformer();
            StringWriter sw = new StringWriter();
            trans.transform(new DOMSource(_as()), new StreamResult(sw));
            return sw.toString();
        }
        catch (TransformerException e)
        {
            return "#document";
        }
    }
}

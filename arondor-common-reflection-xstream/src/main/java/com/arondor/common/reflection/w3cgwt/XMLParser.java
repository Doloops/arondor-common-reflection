package com.arondor.common.reflection.w3cgwt;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

public class XMLParser
{
    private static final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

    public static Document parse(String xmlString)
    {
        try
        {
            DocumentBuilder builder = factory.newDocumentBuilder();
            org.w3c.dom.Document rootDocument = builder.parse(new ByteArrayInputStream(xmlString.getBytes()));

            return Document._build(rootDocument);
        }
        catch (ParserConfigurationException e)
        {
            throw new RuntimeException("Exception !", e);
        }
        catch (SAXException e)
        {
            throw new RuntimeException("Exception !", e);
        }
        catch (IOException e)
        {
            throw new RuntimeException("Exception !", e);
        }
    }

    public static com.arondor.common.reflection.w3cgwt.Document createDocument()
    {
        try
        {
            DocumentBuilder builder = factory.newDocumentBuilder();
            org.w3c.dom.Document rootDocument = builder.newDocument();
            return Document._build(rootDocument);

        }
        catch (ParserConfigurationException e)
        {
            throw new RuntimeException("Exception !", e);
        }
    }
}

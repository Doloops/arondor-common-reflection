package com.arondor.common.w3c2gwt;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

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

    public static com.arondor.common.w3c2gwt.Document createDocument()
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

    public static String serializeDocument(org.w3c.dom.Document document)
    {
        try
        {
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer trans = tf.newTransformer();
            trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            StringWriter sw = new StringWriter();
            trans.transform(new DOMSource(document), new StreamResult(sw));
            return sw.toString();
        }
        catch (TransformerException e)
        {
            return "#document";
        }
    }
}

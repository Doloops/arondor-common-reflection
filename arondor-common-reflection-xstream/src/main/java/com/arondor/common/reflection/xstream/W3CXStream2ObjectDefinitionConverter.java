package com.arondor.common.reflection.xstream;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import com.arondor.common.reflection.model.config.ObjectConfiguration;

public class W3CXStream2ObjectDefinitionConverter extends W3CXStream2ObjectDefinitionConverterLL
        implements ObjectDefinitionReader
{
    private static final Logger LOG = Logger.getLogger(W3CXStream2ObjectDefinitionConverter.class);

    @Override
    public ObjectConfiguration parse(String xmlString)
    {
        try
        {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document rootDocument = builder.parse(new ByteArrayInputStream(xmlString.getBytes()));

            Element rootElement = rootDocument.getDocumentElement();

            LOG.debug("rootElement=" + rootElement.getNodeName());

            return parseObject(rootElement, null);
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

}

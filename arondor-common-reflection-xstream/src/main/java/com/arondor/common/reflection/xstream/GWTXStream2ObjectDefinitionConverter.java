package com.arondor.common.reflection.xstream;

import com.arondor.common.reflection.model.config.ObjectConfiguration;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.XMLParser;

public class GWTXStream2ObjectDefinitionConverter extends GWTXStream2ObjectDefinitionConverterLL
        implements ObjectDefinitionReader
{
    @Override
    public ObjectConfiguration parse(String xmlString)
    {
        Document document = XMLParser.parse(xmlString);
        return parseObject(document.getDocumentElement(), null);
    }
}

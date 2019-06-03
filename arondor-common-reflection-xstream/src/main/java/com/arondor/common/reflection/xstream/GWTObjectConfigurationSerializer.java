package com.arondor.common.reflection.xstream;

import com.arondor.common.reflection.model.config.ObjectConfiguration;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.XMLParser;

public class GWTObjectConfigurationSerializer extends GWTObjectConfigurationSerializerLL
        implements ObjectConfigurationSerializer
{
    @Override
    public String serialize(ObjectConfiguration oc)
    {
        Document document = XMLParser.createDocument();
        return serialize(document, oc);
    }
}

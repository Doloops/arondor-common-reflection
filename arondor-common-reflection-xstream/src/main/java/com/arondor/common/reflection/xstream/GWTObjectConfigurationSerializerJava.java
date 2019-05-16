package com.arondor.common.reflection.xstream;

import com.arondor.common.reflection.model.config.ObjectConfiguration;

public class GWTObjectConfigurationSerializerJava extends GWTObjectConfigurationSerializerLL
        implements ObjectConfigurationSerializer
{
    public String serialize(ObjectConfiguration ec)
    {
        com.arondor.common.w3c2gwt.Document document = com.arondor.common.w3c2gwt.XMLParser
                .createDocument();
        return serialize(document, ec);
    }
}

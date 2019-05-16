package com.arondor.common.reflection.xstream;

import com.arondor.common.reflection.model.config.ObjectConfiguration;

public class GWTObjectConfigurationSerializerJava extends GWTObjectConfigurationSerializerLL
        implements ObjectConfigurationSerializer
{
    public String serialize(ObjectConfiguration ec)
    {
        com.arondor.common.reflection.w3cgwt.Document document = com.arondor.common.reflection.w3cgwt.XMLParser
                .createDocument();
        return serialize(document, ec);
    }
}

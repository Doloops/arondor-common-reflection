package com.arondor.common.reflection.xstream;

import com.arondor.common.reflection.model.config.ObjectConfiguration;

public class GWTObjectConfigurationParserJava extends GWTXStream2ObjectDefinitionConverterLL
        implements ObjectConfigurationReader
{
    @Override
    public ObjectConfiguration parse(String xmlString)
    {
        com.arondor.common.w3c2gwt.Document document = com.arondor.common.w3c2gwt.XMLParser
                .parse(xmlString);
        return parseObject(document.getDocumentElement(), null);
    }
}

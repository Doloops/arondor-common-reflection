package com.arondor.common.reflection.xstream.catalog;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;

import com.arondor.common.reflection.api.catalog.AccessibleClassCatalog;
import com.google.gwt.xml.client.Document;

public class AccessibleClassCatalagParser
{
    public static AccessibleClassCatalog parseFromResource(String resource)
    {
        InputStream source = AccessibleClassCatalagParser.class.getClassLoader().getResourceAsStream(resource);
        try
        {
            Document document = com.arondor.common.w3c2gwt.XMLParser.parse(IOUtils.toString(source));
            return new GWTAccessibleClassCatalogParser().parseCatalog(document.getDocumentElement());
        }
        catch (IOException e)
        {
            throw new IllegalArgumentException("Could not parse resource : " + resource, e);
        }
    }

}

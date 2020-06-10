package com.arondor.common.reflection.xstream.catalog;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;

import com.arondor.common.reflection.api.catalog.AccessibleClassCatalog;
import com.google.gwt.xml.client.Document;

public class AccessibleClassCatalagParser
{
    public static AccessibleClassCatalog parse(File path)
    {
        try (InputStream source = new FileInputStream(path))
        {
            Document document = com.arondor.common.w3c2gwt.XMLParser.parse(IOUtils.toString(source));
            return new GWTAccessibleClassCatalogParser().parseCatalog(document.getDocumentElement());
        }
        catch (IOException e)
        {
            throw new IllegalArgumentException("Could not parse file : " + path.getAbsolutePath(), e);
        }
    }

    public static AccessibleClassCatalog parseFromResource(String resource)
    {
        try (InputStream source = AccessibleClassCatalagParser.class.getClassLoader().getResourceAsStream(resource))
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

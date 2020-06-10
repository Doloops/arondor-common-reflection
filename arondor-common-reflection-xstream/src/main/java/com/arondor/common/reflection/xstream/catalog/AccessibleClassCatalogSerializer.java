package com.arondor.common.reflection.xstream.catalog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import com.arondor.common.reflection.api.catalog.AccessibleClassCatalog;
import com.thoughtworks.xstream.XStream;

public class AccessibleClassCatalogSerializer
{
    public static void write(AccessibleClassCatalog catalog, File path)
    {
        try (OutputStream out = new FileOutputStream(path))
        {
            XStream xstream = new XStream();
            xstream.toXML(catalog, out);
        }
        catch (IOException e)
        {
            throw new IllegalStateException("Could not write catalog to " + path, e);
        }
    }
}

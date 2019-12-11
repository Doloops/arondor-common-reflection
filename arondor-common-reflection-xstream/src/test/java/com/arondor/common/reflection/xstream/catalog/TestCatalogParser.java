package com.arondor.common.reflection.xstream.catalog;

import org.junit.Test;

import com.arondor.common.reflection.api.catalog.AccessibleClassCatalog;

import junit.framework.Assert;

public class TestCatalogParser
{

    @Test
    public void testSimple()
    {
        AccessibleClassCatalog catalog = AccessibleClassCatalagParser.parseFromResource("allclasses-mini.xml");
        Assert.assertNotNull(catalog.getAccessibleClass("com.arondor.fast2p8.alfresco.PropertyHelper"));
    }
}

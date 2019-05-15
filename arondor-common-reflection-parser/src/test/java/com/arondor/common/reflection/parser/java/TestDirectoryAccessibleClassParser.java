package com.arondor.common.reflection.parser.java;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.arondor.common.reflection.api.catalog.AccessibleClassCatalog;
import com.arondor.common.reflection.catalog.SimpleAccessibleClassCatalog;

public class TestDirectoryAccessibleClassParser
{
    @Test
    public void testParseLibsFolder() throws ClassNotFoundException
    {
        AccessibleClassCatalog catalog = new SimpleAccessibleClassCatalog();
        DirectoryAccessibleClassProvider provider = new DirectoryAccessibleClassProvider();
        JavaAccessibleClassParser parser = new JavaAccessibleClassParser();
        parser.setTryInstantiateClassForDefaultValue(true);
        provider.setAccessibleClassParser(parser);

        List<String> packagePrefixes = Arrays.asList("org.junit");
        provider.setPackagePrefixes(packagePrefixes);
        List<String> directories = Arrays.asList("src/test/resources/libs");
        provider.setDirectories(directories);
        provider.provideClasses(catalog);

        Assert.assertNotNull(catalog.getAccessibleClass("org.junit.Test"));
        Assert.assertNull(catalog.getAccessibleClass("org.apache.log4j.Logger"));

    }
}

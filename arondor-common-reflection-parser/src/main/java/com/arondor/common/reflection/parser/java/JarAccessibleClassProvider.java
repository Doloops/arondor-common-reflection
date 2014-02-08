package com.arondor.common.reflection.parser.java;

import java.io.File;
import java.util.List;

import org.apache.log4j.Logger;

import com.arondor.common.reflection.api.catalog.AccessibleClassCatalog;

public class JarAccessibleClassProvider extends AbstractJavaAccessibleClassProvider
{
    private static final Logger LOG = Logger.getLogger(JarAccessibleClassProvider.class);

    private List<String> jars;

    public JarAccessibleClassProvider()
    {

    }

    public void provideClasses(AccessibleClassCatalog catalog)
    {
        for (String jar : jars)
        {
            LOG.debug("Scanning jar : " + jar);
            File jarFile = new File(jar);
            scanJar(catalog, jarFile);
        }
    }

    public List<String> getJars()
    {
        return jars;
    }

    public void setJars(List<String> jars)
    {
        this.jars = jars;
    }

}
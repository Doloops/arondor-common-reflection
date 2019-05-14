package com.arondor.common.reflection.gwt.client;

import org.junit.Test;

import com.arondor.common.reflection.xstream.GWTObjectConfigurationParser;
import com.google.gwt.junit.client.GWTTestCase;

public class TestGWTReflectionParser extends GWTTestCase
{

    @Override
    public String getModuleName()
    {
        return "com.arondor.common.reflection.gwt.ArondorReflectionGWTStandalone";
    }

    @Test
    public void testSimple()
    {
        GWTObjectConfigurationParser parser = new GWTObjectConfigurationParser();
    }
}

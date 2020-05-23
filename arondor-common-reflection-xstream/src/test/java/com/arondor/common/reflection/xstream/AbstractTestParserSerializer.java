package com.arondor.common.reflection.xstream;

import org.junit.Before;

public class AbstractTestParserSerializer
{
    protected ObjectConfigurationReader parser;

    protected ObjectConfigurationSerializer serializer;

    @Before
    public void init()
    {
        parser = new GWTObjectConfigurationParserJava();
        serializer = new GWTObjectConfigurationSerializerJava();
    }
}

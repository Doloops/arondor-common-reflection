package com.arondor.common.w3c2gwt;

import org.junit.Assert;
import org.junit.Test;

public class SimpleTest
{
    @Test
    public void parseAndSerialize_Simple()
    {
        String xml = "<someRoot attr=\"value1\">someContent</someRoot>";

        Document doc = XMLParser.parse(xml);

        String result = doc.toString();

        Assert.assertEquals(xml, result);
    }
}

package com.arondor.common.w3c2gwt;

import org.junit.Assert;
import org.junit.Test;

public class SimpleTest
{
    private void testXml(String xml)
    {
        Document doc = XMLParser.parse(xml);
        String result = doc.toString();
        Assert.assertEquals(xml, result);
    }

    @Test
    public void parseAndSerialize_Simple()
    {
        String xml = "<someRoot attr=\"value1\">someContent</someRoot>";
        testXml(xml);
    }

    @Test
    public void test_singleElement()
    {
        testXml("<myElement/>");
    }

    @Test
    public void test_singleElementSingleAttribute()
    {
        testXml("<myElement myAttribute=\"myValue\"/>");
    }

    @Test
    public void test_singleElementTextValue()
    {
        testXml("<myElement>my text value</myElement>");
    }
}

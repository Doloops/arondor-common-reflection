package com.arondor.common.reflection.xstream.catalog;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.arondor.common.reflection.api.catalog.AccessibleClassCatalog;
import com.arondor.common.reflection.catalog.SimpleAccessibleClassCatalog;
import com.arondor.common.reflection.model.java.AccessibleClass;
import com.arondor.common.reflection.model.java.AccessibleField;
import com.arondor.common.w3c2gwt.XMLParser;
import com.google.gwt.xml.client.Document;

public class TestXStreamCatalogParser // extends GWTTestCase
{
    // @Override
    public String getModuleName()
    {
        return "com.arondor.common.reflection.ReflectionXStream";
    }

    @Test
    public void testSimple()
    {
        String xml = "<com.arondor.common.reflection.catalog.SimpleAccessibleClassCatalog>" + "  <accessibleClassMap>"
                + "    <entry>"
                + "      <string>com.arondor.fast2p8.xstream.factory.taskflow.campaign.XMLCampaignFactory</string>"
                + "      <com.arondor.common.reflection.bean.java.AccessibleClassBean>" + "        <accessibleFields>"
                + "          <entry>" + "            <string>campaignIncantations</string>"
                + "            <com.arondor.common.reflection.bean.java.AccessibleFieldBean>"
                + "              <name>campaignIncantations</name>"
                + "              <className>java.util.List</className>" + "              <readable>true</readable>"
                + "              <writable>false</writable>" + "              <mandatory>false</mandatory>"
                + "              <enumProperty>false</enumProperty>" + "              <is>false</is>"
                + "              <declaredInClass>com.arondor.fast2p8.xstream.factory.taskflow.campaign.XMLCampaignFactory</declaredInClass>"
                + "            </com.arondor.common.reflection.bean.java.AccessibleFieldBean>" + "          </entry>"
                + "          <entry>" + "            <string>campaignFile</string>"
                + "            <com.arondor.common.reflection.bean.java.AccessibleFieldBean>"
                + "              <description>Some Valuable Text</description>"
                + "              <name>campaignFile</name>" + "              <className>java.lang.String</className>"
                + "              <readable>true</readable>" + "              <writable>true</writable>"
                + "              <mandatory>false</mandatory>" + "              <enumProperty>false</enumProperty>"
                + "              <is>false</is>" + "              <genericParameterClassList/>"
                + "              <declaredInClass>com.arondor.fast2p8.xstream.factory.taskflow.campaign.XMLCampaignFactory</declaredInClass>"
                + "            </com.arondor.common.reflection.bean.java.AccessibleFieldBean>" + "          </entry>"
                + "        </accessibleFields>"
                + "        <name>com.arondor.fast2p8.xstream.factory.taskflow.campaign.XMLCampaignFactory</name>"
                + "        <allInterfaces>" + "          <string>java.lang.Object</string>"
                + "          <string>com.arondor.fast2p8.model.factory.taskflow.CampaignFactory</string>"
                + "        </allInterfaces>" + "        <interfaces>" + "          <string>java.lang.Object</string>"
                + "          <string>com.arondor.fast2p8.model.factory.taskflow.CampaignFactory</string>"
                + "        </interfaces>" + "        <superclass>java.lang.Object</superclass>"
                + "        <constructors>"
                + "          <com.arondor.common.reflection.bean.java.AccessibleConstructorBean>"
                + "            <argumentTypes/>"
                + "          </com.arondor.common.reflection.bean.java.AccessibleConstructorBean>"
                + "        </constructors>" + "        <methods>"
                + "          <com.arondor.common.reflection.bean.java.AccessibleMethodBean>"
                + "            <name>getCampaign</name>"
                + "          </com.arondor.common.reflection.bean.java.AccessibleMethodBean>"
                + "          <com.arondor.common.reflection.bean.java.AccessibleMethodBean>"
                + "            <name>deleteCampaign</name>"
                + "          </com.arondor.common.reflection.bean.java.AccessibleMethodBean>"
                + "          <com.arondor.common.reflection.bean.java.AccessibleMethodBean>"
                + "            <name>finishCampaign</name>"
                + "          </com.arondor.common.reflection.bean.java.AccessibleMethodBean>"
                + "          <com.arondor.common.reflection.bean.java.AccessibleMethodBean>"
                + "            <name>getCampaigns</name>"
                + "          </com.arondor.common.reflection.bean.java.AccessibleMethodBean>"
                + "          <com.arondor.common.reflection.bean.java.AccessibleMethodBean>"
                + "            <name>startCampaign</name>"
                + "          </com.arondor.common.reflection.bean.java.AccessibleMethodBean>"
                + "          <com.arondor.common.reflection.bean.java.AccessibleMethodBean>"
                + "            <name>resetCampaignIncantation</name>"
                + "          </com.arondor.common.reflection.bean.java.AccessibleMethodBean>"
                + "          <com.arondor.common.reflection.bean.java.AccessibleMethodBean>"
                + "            <name>getCampaignIncantation</name>"
                + "          </com.arondor.common.reflection.bean.java.AccessibleMethodBean>" + "        </methods>"
                + "        <abstactClass>false</abstactClass>"
                + "      </com.arondor.common.reflection.bean.java.AccessibleClassBean>" + "    </entry>"
                + "    <entry>"
                + "      <string>com.arondor.fast2p8.pojo.taskflow.design.TaskFlowDesignGraphic</string>"
                + "      <com.arondor.common.reflection.bean.java.AccessibleClassBean>" + "        <accessibleFields>"
                + "          <entry>" + "            <string>image</string>"
                + "            <com.arondor.common.reflection.bean.java.AccessibleFieldBean>"
                + "              <name>image</name>" + "              <className>java.lang.String</className>"
                + "              <readable>true</readable>" + "              <writable>true</writable>"
                + "              <mandatory>false</mandatory>" + "              <enumProperty>false</enumProperty>"
                + "              <is>false</is>" + "              <genericParameterClassList/>"
                + "              <declaredInClass>com.arondor.fast2p8.pojo.taskflow.design.TaskFlowDesignGraphic</declaredInClass>"
                + "            </com.arondor.common.reflection.bean.java.AccessibleFieldBean>" + "          </entry>"
                + "          <entry>" + "            <string>x</string>"
                + "            <com.arondor.common.reflection.bean.java.AccessibleFieldBean>"
                + "              <name>x</name>" + "              <className>int</className>"
                + "              <readable>true</readable>" + "              <writable>true</writable>"
                + "              <mandatory>false</mandatory>" + "              <enumProperty>false</enumProperty>"
                + "              <is>false</is>" + "              <genericParameterClassList/>"
                + "              <declaredInClass>com.arondor.fast2p8.pojo.taskflow.design.TaskFlowDesignGraphic</declaredInClass>"
                + "            </com.arondor.common.reflection.bean.java.AccessibleFieldBean>" + "          </entry>"
                + "          <entry>" + "            <string>y</string>"
                + "            <com.arondor.common.reflection.bean.java.AccessibleFieldBean>"
                + "              <name>y</name>" + "              <className>int</className>"
                + "              <readable>true</readable>" + "              <writable>true</writable>"
                + "              <mandatory>false</mandatory>" + "              <enumProperty>false</enumProperty>"
                + "              <is>false</is>" + "              <genericParameterClassList/>"
                + "              <declaredInClass>com.arondor.fast2p8.pojo.taskflow.design.TaskFlowDesignGraphic</declaredInClass>"
                + "            </com.arondor.common.reflection.bean.java.AccessibleFieldBean>" + "          </entry>"
                + "        </accessibleFields>"
                + "        <name>com.arondor.fast2p8.pojo.taskflow.design.TaskFlowDesignGraphic</name>"
                + "        <allInterfaces>" + "          <string>java.lang.Object</string>"
                + "          <string>com.arondor.fast2p8.model.taskflow.design.TaskFlowDesignGraphic</string>"
                + "        </allInterfaces>" + "        <interfaces>" + "          <string>java.lang.Object</string>"
                + "          <string>com.arondor.fast2p8.model.taskflow.design.TaskFlowDesignGraphic</string>"
                + "        </interfaces>" + "        <superclass>java.lang.Object</superclass>"
                + "        <constructors>"
                + "          <com.arondor.common.reflection.bean.java.AccessibleConstructorBean>"
                + "            <argumentTypes/>"
                + "          </com.arondor.common.reflection.bean.java.AccessibleConstructorBean>"
                + "        </constructors>" + "        <methods/>" + "        <abstactClass>false</abstactClass>"
                + "      </com.arondor.common.reflection.bean.java.AccessibleClassBean>" + "    </entry>"
                + "  </accessibleClassMap>"
                + "  <allowSuperclassesInImplementingClases>true</allowSuperclassesInImplementingClases>"
                + "</com.arondor.common.reflection.catalog.SimpleAccessibleClassCatalog>";

        GWTAccessibleClassCatalogParser parser = new GWTAccessibleClassCatalogParser();
        // AccessibleClassCatalog catalog = parser.parse(xml);
        Document doc = XMLParser.parse(xml);
        AccessibleClassCatalog catalog = parser.parseCatalog(doc.getDocumentElement());

        assertTrue(catalog instanceof SimpleAccessibleClassCatalog);
        assertTrue(((SimpleAccessibleClassCatalog) catalog).isAllowSuperclassesInImplementingClases());

        AccessibleClass clazz = catalog
                .getAccessibleClass("com.arondor.fast2p8.xstream.factory.taskflow.campaign.XMLCampaignFactory");
        assertNotNull(clazz);

        assertEquals("java.lang.Object", clazz.getSuperclass());

        assertNotNull(clazz.getInterfaces());
        assertEquals(2, clazz.getInterfaces().size());
        assertEquals("java.lang.Object", clazz.getInterfaces().get(0));
        assertEquals("com.arondor.fast2p8.model.factory.taskflow.CampaignFactory", clazz.getInterfaces().get(1));

        assertNotNull(clazz.getAllInterfaces());
        assertEquals(2, clazz.getAllInterfaces().size());
        assertEquals("java.lang.Object", clazz.getAllInterfaces().get(0));
        assertEquals("com.arondor.fast2p8.model.factory.taskflow.CampaignFactory", clazz.getAllInterfaces().get(1));

        AccessibleField field0 = clazz.getAccessibleFields().get("campaignIncantations");
        assertNotNull(field0);

        AccessibleField field1 = clazz.getAccessibleFields().get("campaignFile");
        assertNotNull(field0);
        assertEquals("campaignFile", field1.getName());
        assertEquals("Some Valuable Text", field1.getDescription());
        assertTrue(field1.getReadable());
        assertTrue(field1.getWritable());
        assertEquals("java.lang.String", field1.getClassName());
        assertEquals("com.arondor.fast2p8.xstream.factory.taskflow.campaign.XMLCampaignFactory",
                field1.getDeclaredInClass());
    }

    @Test
    public void testGeneric()
    {
        String xml = "<com.arondor.common.reflection.catalog.SimpleAccessibleClassCatalog>" + "  <accessibleClassMap>"
                + "<entry>\r\n" + "      <string>com.arondor.fast2p8.punnetlist.filesystem.MultiPunnetList</string>\r\n"
                + "      <com.arondor.common.reflection.bean.java.AccessibleClassBean>\r\n"
                + "        <accessibleFields>\r\n" + "          <entry>\r\n"
                + "            <string>manager</string>\r\n"
                + "            <com.arondor.common.reflection.bean.java.AccessibleFieldBean>\r\n"
                + "              <name>manager</name>\r\n"
                + "              <className>com.arondor.fast2p8.model.manager.Manager</className>\r\n"
                + "              <readable>true</readable>\r\n" + "              <writable>true</writable>\r\n"
                + "              <mandatory>false</mandatory>\r\n"
                + "              <enumProperty>false</enumProperty>\r\n" + "              <is>false</is>\r\n"
                + "              <genericParameterClassList/>\r\n"
                + "              <declaredInClass>com.arondor.fast2p8.punnetlist.filesystem.MultiPunnetList</declaredInClass>\r\n"
                + "            </com.arondor.common.reflection.bean.java.AccessibleFieldBean>\r\n"
                + "          </entry>\r\n" + "          <entry>\r\n" + "            <string>allowAnyFile</string>\r\n"
                + "            <com.arondor.common.reflection.bean.java.AccessibleFieldBean>\r\n"
                + "              <name>allowAnyFile</name>\r\n"
                + "              <description>Allow any kind of file, not only XML-based Punnet description</description>\r\n"
                + "              <className>boolean</className>\r\n" + "              <readable>true</readable>\r\n"
                + "              <writable>true</writable>\r\n" + "              <mandatory>false</mandatory>\r\n"
                + "              <enumProperty>false</enumProperty>\r\n" + "              <is>true</is>\r\n"
                + "              <genericParameterClassList/>\r\n"
                + "              <declaredInClass>com.arondor.fast2p8.punnetlist.filesystem.MultiPunnetList</declaredInClass>\r\n"
                + "            </com.arondor.common.reflection.bean.java.AccessibleFieldBean>\r\n"
                + "          </entry>\r\n" + "          <entry>\r\n" + "            <string>xmlSkipParse</string>\r\n"
                + "            <com.arondor.common.reflection.bean.java.AccessibleFieldBean>\r\n"
                + "              <name>xmlSkipParse</name>\r\n"
                + "              <description>Skip parsing of XML files as if they were Punnet description</description>\r\n"
                + "              <className>boolean</className>\r\n" + "              <readable>true</readable>\r\n"
                + "              <writable>true</writable>\r\n" + "              <mandatory>false</mandatory>\r\n"
                + "              <enumProperty>false</enumProperty>\r\n" + "              <is>true</is>\r\n"
                + "              <genericParameterClassList/>\r\n"
                + "              <declaredInClass>com.arondor.fast2p8.punnetlist.filesystem.MultiPunnetList</declaredInClass>\r\n"
                + "            </com.arondor.common.reflection.bean.java.AccessibleFieldBean>\r\n"
                + "          </entry>\r\n" + "          <entry>\r\n" + "            <string>pathList</string>\r\n"
                + "            <com.arondor.common.reflection.bean.java.AccessibleFieldBean>\r\n"
                + "              <name>pathList</name>\r\n" + "              <description>pathList</description>\r\n"
                + "              <className>java.util.List</className>\r\n"
                + "              <readable>true</readable>\r\n" + "              <writable>true</writable>\r\n"
                + "              <mandatory>false</mandatory>\r\n"
                + "              <enumProperty>false</enumProperty>\r\n" + "              <is>false</is>\r\n"
                + "              <genericParameterClassList>\r\n"
                + "                <string>java.lang.String</string>\r\n"
                + "              </genericParameterClassList>\r\n"
                + "              <declaredInClass>com.arondor.fast2p8.punnetlist.filesystem.MultiPunnetList</declaredInClass>\r\n"
                + "            </com.arondor.common.reflection.bean.java.AccessibleFieldBean>\r\n"
                + "          </entry>\r\n" + "          <entry>\r\n"
                + "            <string>xmlParseFallback</string>\r\n"
                + "            <com.arondor.common.reflection.bean.java.AccessibleFieldBean>\r\n"
                + "              <name>xmlParseFallback</name>\r\n"
                + "              <description>When XML parsing fails, consider adding this file as a regular file (not an XML)</description>\r\n"
                + "              <className>boolean</className>\r\n" + "              <readable>true</readable>\r\n"
                + "              <writable>true</writable>\r\n" + "              <mandatory>false</mandatory>\r\n"
                + "              <enumProperty>false</enumProperty>\r\n" + "              <is>true</is>\r\n"
                + "              <genericParameterClassList/>\r\n"
                + "              <declaredInClass>com.arondor.fast2p8.punnetlist.filesystem.MultiPunnetList</declaredInClass>\r\n"
                + "            </com.arondor.common.reflection.bean.java.AccessibleFieldBean>\r\n"
                + "          </entry>\r\n" + "          <entry>\r\n" + "            <string>fileScanner</string>\r\n"
                + "            <com.arondor.common.reflection.bean.java.AccessibleFieldBean>\r\n"
                + "              <name>fileScanner</name>\r\n"
                + "              <description>The file scanner implementation to use</description>\r\n"
                + "              <className>com.arondor.common.io.scan.FileScanner</className>\r\n"
                + "              <readable>true</readable>\r\n" + "              <writable>true</writable>\r\n"
                + "              <mandatory>false</mandatory>\r\n"
                + "              <enumProperty>false</enumProperty>\r\n" + "              <is>false</is>\r\n"
                + "              <genericParameterClassList/>\r\n"
                + "              <declaredInClass>com.arondor.fast2p8.punnetlist.filesystem.MultiPunnetList</declaredInClass>\r\n"
                + "            </com.arondor.common.reflection.bean.java.AccessibleFieldBean>\r\n"
                + "          </entry>\r\n" + "          <entry>\r\n" + "            <string>async</string>\r\n"
                + "            <com.arondor.common.reflection.bean.java.AccessibleFieldBean>\r\n"
                + "              <name>async</name>\r\n" + "              <className>boolean</className>\r\n"
                + "              <readable>true</readable>\r\n" + "              <writable>true</writable>\r\n"
                + "              <mandatory>false</mandatory>\r\n"
                + "              <enumProperty>false</enumProperty>\r\n" + "              <is>true</is>\r\n"
                + "              <genericParameterClassList/>\r\n"
                + "              <declaredInClass>com.arondor.common.io.AsyncIterator</declaredInClass>\r\n"
                + "            </com.arondor.common.reflection.bean.java.AccessibleFieldBean>\r\n"
                + "          </entry>\r\n" + "          <entry>\r\n" + "            <string>queueLimit</string>\r\n"
                + "            <com.arondor.common.reflection.bean.java.AccessibleFieldBean>\r\n"
                + "              <name>queueLimit</name>\r\n" + "              <className>int</className>\r\n"
                + "              <readable>true</readable>\r\n" + "              <writable>true</writable>\r\n"
                + "              <mandatory>false</mandatory>\r\n"
                + "              <enumProperty>false</enumProperty>\r\n" + "              <is>false</is>\r\n"
                + "              <genericParameterClassList/>\r\n"
                + "              <declaredInClass>com.arondor.common.io.AsyncIterator</declaredInClass>\r\n"
                + "            </com.arondor.common.reflection.bean.java.AccessibleFieldBean>\r\n"
                + "          </entry>\r\n" + "          <entry>\r\n"
                + "            <string>xmlParseSkipExceptions</string>\r\n"
                + "            <com.arondor.common.reflection.bean.java.AccessibleFieldBean>\r\n"
                + "              <name>xmlParseSkipExceptions</name>\r\n"
                + "              <description>When a parsing exception occurs, do not stop parsing of PunnetList and resume to next candidate</description>\r\n"
                + "              <className>boolean</className>\r\n" + "              <readable>true</readable>\r\n"
                + "              <writable>true</writable>\r\n" + "              <mandatory>false</mandatory>\r\n"
                + "              <enumProperty>false</enumProperty>\r\n" + "              <is>true</is>\r\n"
                + "              <genericParameterClassList/>\r\n"
                + "              <declaredInClass>com.arondor.fast2p8.punnetlist.filesystem.MultiPunnetList</declaredInClass>\r\n"
                + "            </com.arondor.common.reflection.bean.java.AccessibleFieldBean>\r\n"
                + "          </entry>\r\n" + "          <entry>\r\n" + "            <string>punnetFactory</string>\r\n"
                + "            <com.arondor.common.reflection.bean.java.AccessibleFieldBean>\r\n"
                + "              <name>punnetFactory</name>\r\n"
                + "              <description>punnetFactory</description>\r\n"
                + "              <className>com.arondor.fast2p8.model.factory.PunnetFactory</className>\r\n"
                + "              <readable>true</readable>\r\n" + "              <writable>false</writable>\r\n"
                + "              <mandatory>false</mandatory>\r\n"
                + "              <enumProperty>false</enumProperty>\r\n" + "              <is>false</is>\r\n"
                + "              <declaredInClass>com.arondor.fast2p8.punnetlist.filesystem.MultiPunnetList</declaredInClass>\r\n"
                + "            </com.arondor.common.reflection.bean.java.AccessibleFieldBean>\r\n"
                + "          </entry>\r\n" + "          <entry>\r\n" + "            <string>xslPath</string>\r\n"
                + "            <com.arondor.common.reflection.bean.java.AccessibleFieldBean>\r\n"
                + "              <name>xslPath</name>\r\n"
                + "              <description>The XSL stylesheet file to use when parsing XML files</description>\r\n"
                + "              <className>java.lang.String</className>\r\n"
                + "              <readable>true</readable>\r\n" + "              <writable>true</writable>\r\n"
                + "              <mandatory>false</mandatory>\r\n"
                + "              <enumProperty>false</enumProperty>\r\n" + "              <is>false</is>\r\n"
                + "              <genericParameterClassList/>\r\n"
                + "              <declaredInClass>com.arondor.fast2p8.punnetlist.filesystem.MultiPunnetList</declaredInClass>\r\n"
                + "            </com.arondor.common.reflection.bean.java.AccessibleFieldBean>\r\n"
                + "          </entry>\r\n" + "          <entry>\r\n" + "            <string>filesPerPunnet</string>\r\n"
                + "            <com.arondor.common.reflection.bean.java.AccessibleFieldBean>\r\n"
                + "              <name>filesPerPunnet</name>\r\n"
                + "              <description>Number of files to group when fetch multiple documents from 1 Punnet</description>\r\n"
                + "              <className>int</className>\r\n" + "              <readable>true</readable>\r\n"
                + "              <writable>true</writable>\r\n" + "              <mandatory>false</mandatory>\r\n"
                + "              <enumProperty>false</enumProperty>\r\n" + "              <is>false</is>\r\n"
                + "              <genericParameterClassList/>\r\n"
                + "              <declaredInClass>com.arondor.fast2p8.punnetlist.filesystem.MultiPunnetList</declaredInClass>\r\n"
                + "            </com.arondor.common.reflection.bean.java.AccessibleFieldBean>\r\n"
                + "          </entry>\r\n" + "          <entry>\r\n"
                + "            <string>queueLimitDelay</string>\r\n"
                + "            <com.arondor.common.reflection.bean.java.AccessibleFieldBean>\r\n"
                + "              <name>queueLimitDelay</name>\r\n" + "              <className>int</className>\r\n"
                + "              <readable>true</readable>\r\n" + "              <writable>true</writable>\r\n"
                + "              <mandatory>false</mandatory>\r\n"
                + "              <enumProperty>false</enumProperty>\r\n" + "              <is>false</is>\r\n"
                + "              <genericParameterClassList/>\r\n"
                + "              <declaredInClass>com.arondor.common.io.AsyncIterator</declaredInClass>\r\n"
                + "            </com.arondor.common.reflection.bean.java.AccessibleFieldBean>\r\n"
                + "          </entry>\r\n" + "          <entry>\r\n" + "            <string>asyncThreads</string>\r\n"
                + "            <com.arondor.common.reflection.bean.java.AccessibleFieldBean>\r\n"
                + "              <name>asyncThreads</name>\r\n" + "              <className>int</className>\r\n"
                + "              <readable>true</readable>\r\n" + "              <writable>true</writable>\r\n"
                + "              <mandatory>false</mandatory>\r\n"
                + "              <enumProperty>false</enumProperty>\r\n" + "              <is>false</is>\r\n"
                + "              <genericParameterClassList/>\r\n"
                + "              <declaredInClass>com.arondor.common.io.AsyncIterator</declaredInClass>\r\n"
                + "            </com.arondor.common.reflection.bean.java.AccessibleFieldBean>\r\n"
                + "          </entry>\r\n" + "        </accessibleFields>\r\n"
                + "        <name>com.arondor.fast2p8.punnetlist.filesystem.MultiPunnetList</name>\r\n"
                + "        <description>A generic broker for wildcarded punnet lists</description>\r\n"
                + "        <allInterfaces>\r\n" + "          <string>java.lang.Object</string>\r\n"
                + "          <string>com.arondor.fast2p8.model.punnetlist.PunnetList</string>\r\n"
                + "          <string>java.util.Iterator</string>\r\n"
                + "          <string>java.lang.Iterable</string>\r\n" + "        </allInterfaces>\r\n"
                + "        <interfaces>\r\n" + "          <string>java.lang.Object</string>\r\n"
                + "          <string>com.arondor.fast2p8.model.punnetlist.PunnetList</string>\r\n"
                + "        </interfaces>\r\n"
                + "        <superclass>com.arondor.common.io.AsyncIterator</superclass>\r\n"
                + "        <constructors>\r\n"
                + "          <com.arondor.common.reflection.bean.java.AccessibleConstructorBean>\r\n"
                + "            <argumentTypes/>\r\n"
                + "          </com.arondor.common.reflection.bean.java.AccessibleConstructorBean>\r\n"
                + "        </constructors>\r\n" + "        <methods>\r\n"
                + "          <com.arondor.common.reflection.bean.java.AccessibleMethodBean>\r\n"
                + "            <name>remove</name>\r\n"
                + "          </com.arondor.common.reflection.bean.java.AccessibleMethodBean>\r\n"
                + "          <com.arondor.common.reflection.bean.java.AccessibleMethodBean>\r\n"
                + "            <name>hasNext</name>\r\n"
                + "          </com.arondor.common.reflection.bean.java.AccessibleMethodBean>\r\n"
                + "          <com.arondor.common.reflection.bean.java.AccessibleMethodBean>\r\n"
                + "            <name>iterator</name>\r\n"
                + "          </com.arondor.common.reflection.bean.java.AccessibleMethodBean>\r\n"
                + "          <com.arondor.common.reflection.bean.java.AccessibleMethodBean>\r\n"
                + "            <name>next</name>\r\n"
                + "          </com.arondor.common.reflection.bean.java.AccessibleMethodBean>\r\n"
                + "          <com.arondor.common.reflection.bean.java.AccessibleMethodBean>\r\n"
                + "            <name>interruptParsing</name>\r\n"
                + "          </com.arondor.common.reflection.bean.java.AccessibleMethodBean>\r\n"
                + "          <com.arondor.common.reflection.bean.java.AccessibleMethodBean>\r\n"
                + "            <name>forEachRemaining</name>\r\n"
                + "          </com.arondor.common.reflection.bean.java.AccessibleMethodBean>\r\n"
                + "          <com.arondor.common.reflection.bean.java.AccessibleMethodBean>\r\n"
                + "            <name>spliterator</name>\r\n"
                + "          </com.arondor.common.reflection.bean.java.AccessibleMethodBean>\r\n"
                + "          <com.arondor.common.reflection.bean.java.AccessibleMethodBean>\r\n"
                + "            <name>forEach</name>\r\n"
                + "          </com.arondor.common.reflection.bean.java.AccessibleMethodBean>\r\n"
                + "        </methods>\r\n" + "        <abstactClass>false</abstactClass>\r\n"
                + "      </com.arondor.common.reflection.bean.java.AccessibleClassBean>\r\n" + "    </entry>"
                + "  </accessibleClassMap>"
                + "  <allowSuperclassesInImplementingClases>true</allowSuperclassesInImplementingClases>"
                + "</com.arondor.common.reflection.catalog.SimpleAccessibleClassCatalog>";

        GWTAccessibleClassCatalogParser parser = new GWTAccessibleClassCatalogParser();

        Document doc = XMLParser.parse(xml);
        AccessibleClassCatalog catalog = parser.parseCatalog(doc.getDocumentElement());

        assertTrue(catalog instanceof SimpleAccessibleClassCatalog);
        assertTrue(((SimpleAccessibleClassCatalog) catalog).isAllowSuperclassesInImplementingClases());

        AccessibleClass clazz = catalog.getAccessibleClass("com.arondor.fast2p8.punnetlist.filesystem.MultiPunnetList");
        assertNotNull(clazz);

        assertEquals("com.arondor.common.io.AsyncIterator", clazz.getSuperclass());

        assertNotNull(clazz.getInterfaces());
        assertEquals(2, clazz.getInterfaces().size());
        assertEquals("java.lang.Object", clazz.getInterfaces().get(0));
        assertEquals("com.arondor.fast2p8.model.punnetlist.PunnetList", clazz.getInterfaces().get(1));

        AccessibleField pathList = clazz.getAccessibleFields().get("pathList");
        assertEquals("java.util.List", pathList.getClassName());

        assertNotNull(pathList.getGenericParameterClassList());
        assertEquals(1, pathList.getGenericParameterClassList().size());
        assertEquals("java.lang.String", pathList.getGenericParameterClassList().get(0));
    }
}

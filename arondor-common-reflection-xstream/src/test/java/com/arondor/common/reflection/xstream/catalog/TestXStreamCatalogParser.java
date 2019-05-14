package com.arondor.common.reflection.xstream.catalog;

import com.arondor.common.reflection.api.catalog.AccessibleClassCatalog;
import com.arondor.common.reflection.catalog.SimpleAccessibleClassCatalog;
import com.arondor.common.reflection.model.java.AccessibleClass;
import com.arondor.common.reflection.model.java.AccessibleField;
import com.google.gwt.junit.client.GWTTestCase;

public class TestXStreamCatalogParser extends GWTTestCase
{
    @Override
    public String getModuleName()
    {
        return "com.arondor.common.reflection.ReflectionXStreamTest";
    }

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
        AccessibleClassCatalog catalog = parser.parse(xml);

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
        assertTrue(field1.getReadable());
        assertTrue(field1.getWritable());
        assertEquals("java.lang.String", field1.getClassName());
        assertEquals("com.arondor.fast2p8.xstream.factory.taskflow.campaign.XMLCampaignFactory",
                field1.getDeclaredInClass());
    }

}

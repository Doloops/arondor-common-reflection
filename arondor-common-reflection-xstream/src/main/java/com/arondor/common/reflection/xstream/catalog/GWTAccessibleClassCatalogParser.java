package com.arondor.common.reflection.xstream.catalog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import com.arondor.common.reflection.api.catalog.AccessibleClassCatalog;
import com.arondor.common.reflection.bean.java.AccessibleClassBean;
import com.arondor.common.reflection.bean.java.AccessibleFieldBean;
import com.arondor.common.reflection.catalog.SimpleAccessibleClassCatalog;
import com.arondor.common.reflection.model.java.AccessibleField;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;

public class GWTAccessibleClassCatalogParser
{
    private static final Logger LOG = Logger.getLogger(GWTAccessibleClassCatalogParser.class.getName());

    public AccessibleClassCatalog parse(String xmlString)
    {
        Document document = XMLParser.parse(xmlString);
        return parseCatalog(document.getDocumentElement());
    }

    private String getChildValue(Element element, String childName)
    {
        NodeList childNodes = element.getElementsByTagName(childName);
        for (int idx = 0; idx < childNodes.getLength(); idx++)
        {
            Node child = childNodes.item(idx);
            if (child instanceof Element && child.getChildNodes().getLength() == 1)
            {
                String content = child.getFirstChild().toString();
                return content;
            }
        }
        return "";
    }

    private Element getSingleChild(Element element, String childName)
    {
        NodeList childNodes = element.getChildNodes();
        for (int idx = 0; idx < childNodes.getLength(); idx++)
        {
            Node child = childNodes.item(idx);
            if (child instanceof Element && child.getNodeName().equals(childName))
            {
                return (Element) child;
            }
        }
        return null;
    }

    private List<Element> getChildrenByTagName(Element element, String name)
    {
        List<Element> finalList = new ArrayList<Element>();
        NodeList childNodes = element.getChildNodes();
        for (int idx = 0; idx < childNodes.getLength(); idx++)
        {
            Node child = childNodes.item(idx);
            if (child instanceof Element && child.getNodeName().equals(name))
            {
                finalList.add((Element) child);
            }
        }
        return finalList;
    }

    private AccessibleClassCatalog parseCatalog(Element element)
    {
        SimpleAccessibleClassCatalog catalog = new SimpleAccessibleClassCatalog();
        catalog.setAllowSuperclassesInImplementingClases(
                "true".equals(getChildValue(element, "allowSuperclassesInImplementingClases")));
        Element accessibleClassMap = getSingleChild(element, "accessibleClassMap");
        if (accessibleClassMap != null)
        {
            for (Element child : getChildrenByTagName(accessibleClassMap, "entry"))
            {
                parseClassEntry(catalog, child);
            }
        }
        return catalog;
    }

    private void parseClassEntry(SimpleAccessibleClassCatalog catalog, Element entry)
    {
        String key = getChildValue(entry, "string");
        LOG.info("At key : " + key);

        AccessibleClassBean ac = new AccessibleClassBean();
        ac.setName(key);
        ac.setAccessibleFields(new HashMap<String, AccessibleField>());

        ac.setSuperclass(getChildValue(entry, "superclass"));

        Element classElement = getSingleChild(entry, "com.arondor.common.reflection.bean.java.AccessibleClassBean");
        ac.setInterfaces(new ArrayList<String>());
        Element interfacesElement = getSingleChild(classElement, "interfaces");
        if (interfacesElement != null)
        {
            for (Element interfaceElement : getChildrenByTagName(interfacesElement, "string"))
            {
                ac.getInterfaces().add(interfaceElement.getFirstChild().toString());
            }
        }

        ac.setAllInterfaces(new ArrayList<String>());
        Element allInterfacesElement = getSingleChild(classElement, "allInterfaces");
        if (allInterfacesElement != null)
        {
            for (Element interfaceElement : getChildrenByTagName(allInterfacesElement, "string"))
            {
                ac.getAllInterfaces().add(interfaceElement.getFirstChild().toString());
            }
        }

        Element accessibleFields = getSingleChild(classElement, "accessibleFields");
        if (accessibleFields != null)
        {
            for (Element child : getChildrenByTagName(accessibleFields, "entry"))
            {
                parseFieldEntry(ac, child);
            }
        }

        catalog.addAccessibleClass(ac);
    }

    private void parseFieldEntry(AccessibleClassBean ac, Element fieldEntry)
    {
        String key = getChildValue(fieldEntry, "string");
        Element fieldElement = getSingleChild(fieldEntry,
                "com.arondor.common.reflection.bean.java.AccessibleFieldBean");

        AccessibleFieldBean fieldBean = new AccessibleFieldBean();
        fieldBean.setName(getChildValue(fieldElement, "name"));
        fieldBean.setDescription(getChildValue(fieldElement, "description"));
        fieldBean.setClassName(getChildValue(fieldElement, "className"));
        if ("true".equals(getChildValue(fieldElement, "readable")))
        {
            fieldBean.setReadable();
        }
        if ("true".equals(getChildValue(fieldElement, "writable")))
        {
            fieldBean.setWritable();
        }
        fieldBean.setDeclaredInClass(getChildValue(fieldElement, "declaredInClass"));
        ac.getAccessibleFields().put(key, fieldBean);
    }
}

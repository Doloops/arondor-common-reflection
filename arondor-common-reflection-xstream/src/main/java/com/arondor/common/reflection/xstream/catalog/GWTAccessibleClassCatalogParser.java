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
        Element singleChild = getSingleChild(element, childName);
        if (singleChild != null && singleChild instanceof Element && singleChild.getChildNodes().getLength() == 1)
        {
            String content = singleChild.getFirstChild().toString();
            return content;
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

    protected AccessibleClassCatalog parseCatalog(Element element)
    {
        SimpleAccessibleClassCatalog catalog = new SimpleAccessibleClassCatalog();
        String value = getChildValue(element, "allowSuperclassesInImplementingClases");
        catalog.setAllowSuperclassesInImplementingClases("true".equals(value));
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

        AccessibleClassBean ac = new AccessibleClassBean();
        ac.setName(key);

        Element classElement = getSingleChild(entry, "com.arondor.common.reflection.bean.java.AccessibleClassBean");

        String description = getChildValue(classElement, "description");
        if (description != null && !description.equals(""))
        {
            ac.setDescription(description);
        }

        String longDescription = getChildValue(classElement, "longDescription");
        if (longDescription != null && !longDescription.equals(""))
        {
            ac.setLongDescription(longDescription);
        }

        String defaultBehavior = getChildValue(classElement, "defaultBehavior");
        if (defaultBehavior != null && !defaultBehavior.equals(""))
        {
            ac.setDefaultBehavior(defaultBehavior);
        }
        ac.setAccessibleFields(new HashMap<String, AccessibleField>());

        ac.setSuperclass(getChildValue(classElement, "superclass"));

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
        String description = getChildValue(fieldElement, "description");
        if (description != null && !description.equals(""))
        {
            fieldBean.setDescription(description);
        }
        String longDescription = getChildValue(fieldElement, "longDescription");
        if (longDescription != null && !longDescription.equals(""))
        {
            fieldBean.setLongDescription(longDescription);
        }
        String placeholder = getChildValue(fieldElement, "placeholder");
        if (placeholder != null && !placeholder.equals(""))
        {
            fieldBean.setPlaceholder(placeholder);
        }
        String defaultBehavior = getChildValue(fieldElement, "defaultBehavior");
        if (defaultBehavior != null && !defaultBehavior.equals(""))
        {
            fieldBean.setDefaultBehavior(defaultBehavior);
        }
        String defaultValue = getChildValue(fieldElement, "defaultValue");
        if (defaultValue != null && !defaultValue.equals(""))
        {
            fieldBean.setDefaultValue(defaultValue);
        }
        fieldBean.setClassName(getChildValue(fieldElement, "className"));
        if ("true".equals(getChildValue(fieldElement, "readable")))
        {
            fieldBean.setReadable();
        }
        if ("true".equals(getChildValue(fieldElement, "writable")))
        {
            fieldBean.setWritable();
        }
        fieldBean.setMandatory("true".equals(getChildValue(fieldElement, "mandatory")));
        fieldBean.setPassword("true".equals(getChildValue(fieldElement, "password")));
        fieldBean.setEnumProperty("true".equals(getChildValue(fieldElement, "enumProperty")));
        fieldBean.setDeclaredInClass(getChildValue(fieldElement, "declaredInClass"));
        fieldBean.setIs("true".equals(getChildValue(fieldElement, "is")));

        Element generics = getSingleChild(fieldElement, "genericParameterClassList");
        if (generics != null && generics.hasChildNodes())
        {
            fieldBean.setGenericParameterClassList(new ArrayList<String>());
            for (int child = 0; child < generics.getChildNodes().getLength(); child++)
            {
                Node childNode = generics.getChildNodes().item(child);
                if (childNode.getNodeType() == Node.ELEMENT_NODE && childNode.getNodeName() == "string"
                        && childNode.hasChildNodes())
                {
                    fieldBean.getGenericParameterClassList().add(childNode.getFirstChild().toString());
                }
            }
        }
        ac.getAccessibleFields().put(key, fieldBean);
    }
}

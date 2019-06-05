package com.arondor.common.reflection.parser.spring;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import com.arondor.common.reflection.bean.config.ListConfigurationBean;
import com.arondor.common.reflection.bean.config.MapConfigurationBean;
import com.arondor.common.reflection.bean.config.ObjectConfigurationBean;
import com.arondor.common.reflection.bean.config.ObjectConfigurationMapBean;
import com.arondor.common.reflection.bean.config.PrimitiveConfigurationBean;
import com.arondor.common.reflection.bean.config.ReferenceConfigurationBean;
import com.arondor.common.reflection.model.config.ElementConfiguration;
import com.arondor.common.reflection.model.config.ListConfiguration;
import com.arondor.common.reflection.model.config.MapConfiguration;
import com.arondor.common.reflection.model.config.ObjectConfiguration;
import com.arondor.common.reflection.model.config.ObjectConfigurationMap;
import com.arondor.common.reflection.model.config.ReferenceConfiguration;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;

public class XMLBeanDefinitionReader
{
    private static final Logger LOG = Logger.getLogger(XMLBeanDefinitionReader.class.getName());

    private void assertEquals(String left, String right)
    {
        if (!left.equals(right))
        {
            throw new RuntimeException("Not equals ! left=" + left + ", right=" + right);
        }
    }

    private List<Element> getChildElements(Element father)
    {
        return getChildElements(father, null);
    }

    private List<Element> getChildElements(Element father, String childName)
    {
        List<Element> children = new ArrayList<Element>();
        for (int childIdx = 0; childIdx < father.getChildNodes().getLength(); childIdx++)
        {
            Node childNode = father.getChildNodes().item(childIdx);
            if (childNode.getNodeType() == Node.ELEMENT_NODE
                    && (childName == null || childNode.getNodeName().equals(childName)))
            {
                children.add((Element) childNode);
            }
        }
        return children;
    }

    private Element getSingleChild(Element father, String name)
    {
        for (int childIdx = 0; childIdx < father.getChildNodes().getLength(); childIdx++)
        {
            Node childNode = father.getChildNodes().item(childIdx);
            if (childNode.getNodeType() == Node.ELEMENT_NODE && childNode.getNodeName().equals(name))
            {
                return (Element) childNode;
            }
        }
        return null;
    }

    public ObjectConfigurationMap read(Document document)
    {
        ObjectConfigurationMap objectConfigurationMap = new ObjectConfigurationMapBean();

        Element root = document.getDocumentElement();
        assertEquals("beans", root.getNodeName());

        for (Element bean : getChildElements(root))
        {
            if (bean.getNodeName().equals("bean"))
            {
                LOG.finest("At bean :" + bean.getNodeName() + ", id=" + bean.getAttribute("id"));
                ObjectConfiguration objectConfiguration = parseObjectConfiguration(bean);
                if (objectConfiguration.getObjectName() == null)
                {
                    throw new IllegalArgumentException("Bean has no valid id : " + bean.toString());
                }
                objectConfigurationMap.put(objectConfiguration.getObjectName(), objectConfiguration);
            }
        }
        return objectConfigurationMap;
    }

    private ObjectConfiguration parseObjectConfiguration(Element bean)
    {
        ObjectConfigurationBean objectConfiguration = new ObjectConfigurationBean();
        objectConfiguration.setConstructorArguments(new ArrayList<ElementConfiguration>());
        if (bean.hasAttribute("id"))
        {
            objectConfiguration.setObjectName(bean.getAttribute("id"));
        }
        if (bean.hasAttribute("class"))
        {
            objectConfiguration.setClassName(bean.getAttribute("class"));
        }
        if (bean.hasAttribute("scope"))
        {
            objectConfiguration.setSingleton(bean.getAttribute("scope").equals("singleton"));
        }
        Element constructorArgsElt = getSingleChild(bean, "constructor-arg");
        if (constructorArgsElt != null)
        {
            ListConfiguration args = parseList(constructorArgsElt);
            objectConfiguration.getConstructorArguments().addAll(args.getListConfiguration());
        }

        objectConfiguration.setFields(new HashMap<String, ElementConfiguration>());

        for (Element field : getChildElements(bean))
        {
            if (field.getNodeName().equals("property"))
            {
                ElementConfiguration fieldConfiguration = parseProperty(field);
                objectConfiguration.getFields().put(field.getAttribute("name"), fieldConfiguration);
            }
        }
        return objectConfiguration;
    }

    private ElementConfiguration parseProperty(Element field)
    {
        if (field.hasAttribute("value"))
        {
            PrimitiveConfigurationBean primitive = new PrimitiveConfigurationBean();
            primitive.setValue(field.getAttribute("value"));
            return primitive;
        }
        List<Element> propertyContents = getChildElements(field);
        if (propertyContents.size() == 1)
        {
            Element child = propertyContents.get(0);
            try
            {
                return parseElement(child);
            }
            catch (IllegalArgumentException e)
            {
                throw new IllegalArgumentException("At field name : " + field.getAttribute("name"), e);
            }

        }
        throw new IllegalArgumentException("Not supported :" + field.getNodeName().toString() + " (field name : "
                + field.getAttribute("name") + ")");
    }

    private ElementConfiguration parseElement(Element element)
    {
        if (element.getNodeName().equals("bean"))
        {
            return parseObjectConfiguration(element);
        }
        if (element.getNodeName().equals("ref"))
        {
            return parseObjectRef(element);
        }
        if (element.getNodeName().equals("list"))
        {
            return parseList(element);
        }
        if (element.getNodeName().equals("value"))
        {
            String content = element.getChildNodes().item(0).toString();
            PrimitiveConfigurationBean primitive = new PrimitiveConfigurationBean();
            primitive.setValue(content);
            return primitive;
        }
        if (element.getNodeName().equals("map"))
        {
            return parseMap(element);
        }
        throw new IllegalArgumentException("Not supported :" + element.getNodeName());
    }

    private MapConfiguration parseMap(Element element)
    {
        MapConfigurationBean map = new MapConfigurationBean();
        map.setMapConfiguration(new HashMap<ElementConfiguration, ElementConfiguration>());
        for (Element childEntry : getChildElements(element, "entry"))
        {
            List<Element> children = getChildElements(childEntry);
            Element keyElement = children.get(0);
            ElementConfiguration key = parseElement(getChildElements(keyElement).get(0));
            ElementConfiguration value = parseElement(children.get(1));
            map.getMapConfiguration().put(key, value);
        }
        return map;
    }

    private ListConfiguration parseList(Element listElement)
    {
        ListConfigurationBean list = new ListConfigurationBean();
        list.setListConfiguration(new ArrayList<ElementConfiguration>());
        for (Element child : getChildElements(listElement))
        {
            ElementConfiguration elt = parseElement(child);
            list.getListConfiguration().add(elt);
        }
        return list;
    }

    private ReferenceConfiguration parseObjectRef(Element refElement)
    {
        ReferenceConfigurationBean ref = new ReferenceConfigurationBean();
        ref.setReferenceName(refElement.getAttribute("bean"));
        return ref;
    }
}

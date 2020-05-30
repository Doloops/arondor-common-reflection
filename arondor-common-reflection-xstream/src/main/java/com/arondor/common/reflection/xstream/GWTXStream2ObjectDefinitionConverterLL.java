package com.arondor.common.reflection.xstream;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.arondor.common.reflection.bean.config.ListConfigurationBean;
import com.arondor.common.reflection.bean.config.MapConfigurationBean;
import com.arondor.common.reflection.bean.config.ObjectConfigurationBean;
import com.arondor.common.reflection.bean.config.PrimitiveConfigurationBean;
import com.arondor.common.reflection.model.config.ElementConfiguration;
import com.arondor.common.reflection.model.config.ListConfiguration;
import com.arondor.common.reflection.model.config.MapConfiguration;
import com.arondor.common.reflection.model.config.ObjectConfiguration;
import com.arondor.common.reflection.model.config.PrimitiveConfiguration;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;

public class GWTXStream2ObjectDefinitionConverterLL
{
    private static final Logger LOG = Logger.getLogger(GWTXStream2ObjectDefinitionConverterLL.class.getName());

    private static final boolean DEBUG = LOG.isLoggable(Level.FINEST);

    private String classAttributeName = "class";

    public String getClassAttributeName()
    {
        return classAttributeName;
    }

    public void setClassAttributeName(String classAttributeName)
    {
        this.classAttributeName = classAttributeName;
    }

    private TypeOracle typeOracle;

    public TypeOracle getTypeOracle()
    {
        return typeOracle;
    }

    public void setTypeOracle(TypeOracle typeOracle)
    {
        this.typeOracle = typeOracle;
    }

    private String guessClassName(String className, String fieldName)
    {
        if (getTypeOracle() == null)
        {
            return null;
        }
        return getTypeOracle().guessType(className, fieldName);
    }

    protected ObjectConfiguration parseObject(Element objectElement, String guessedParentClass)
    {
        if (DEBUG)
            LOG.finest("parseObject() objectElement=" + objectElement.getNodeName());
        ObjectConfiguration oc = new ObjectConfigurationBean();
        if (guessedParentClass == null)
        {
            guessedParentClass = objectElement.getNodeName();
        }
        oc.setClassName(guessedParentClass);
        oc.setFields(new HashMap<String, ElementConfiguration>());
        for (Node childNode = objectElement.getFirstChild(); childNode != null; childNode = childNode.getNextSibling())
        {
            if (DEBUG)
                LOG.finest("* parseObject() childNode=" + childNode.getNodeType() + " - " + childNode.getNodeName());
            switch (childNode.getNodeType())
            {
            case Node.TEXT_NODE:
                break;
            case Node.ELEMENT_NODE:
            {
                String fieldName = childNode.getNodeName();
                Element childElement = (Element) childNode;
                String guessedChildClass = null;
                if (childElement.hasAttribute("class"))
                {
                    guessedChildClass = childElement.getAttribute("class");
                }
                if (guessedChildClass == null)
                {
                    guessedChildClass = guessClassName(guessedParentClass, fieldName);
                }
                ElementConfiguration ec = parseElement(childElement, guessedChildClass);
                oc.getFields().put(fieldName, ec);
                break;
            }
            default:
                if (DEBUG)
                    LOG.finest("Not handled : type " + childNode.getNodeType());
            }
        }

        return oc;
    }

    private List<Element> elementsList(NodeList nodeList)
    {
        List<Element> elementsList = new ArrayList<Element>();
        for (int nodeIndex = 0; nodeIndex < nodeList.getLength(); nodeIndex++)
        {
            Node node = nodeList.item(nodeIndex);
            if (node.getNodeType() == Node.ELEMENT_NODE)
            {
                elementsList.add((Element) node);
            }
        }
        return elementsList;
    }

    private ElementConfiguration parseElement(Element element, String guessedClass)
    {
        if (guessedClass == null && element.hasAttribute("class"))
        {
            guessedClass = element.getAttribute("class");
        }
        if (DEBUG)
            LOG.finest("* parseElement() element=" + element.getNodeName() + ", guessedClass=" + guessedClass);
        if (element.getChildNodes().getLength() == 1 && element.getFirstChild().getNodeType() == Node.TEXT_NODE)
        {
            String nodeValue = element.getFirstChild().getNodeValue();
            if (nodeValue != null && nodeValue.equals("#null"))
            {
                return null;
            }
            if (DEBUG)
                LOG.finest("Primitive type : " + element.getNodeName() + "=" + nodeValue);
            PrimitiveConfiguration pc = new PrimitiveConfigurationBean();
            pc.setValue(nodeValue);
            return pc;
        }
        if (guessedClass != null && !isList(guessedClass))
        {
            if (DEBUG)
                LOG.finest("* parseElement() this is an explicit object : '" + guessedClass + "'");
            return parseObject(element, guessedClass);
        }
        if (element.getChildNodes().getLength() == 1 && element.getFirstChild() instanceof Element
                && element.getFirstChild().getNodeName().equals("map"))
        {
            MapConfiguration mapConfiguration = parseMap((Element) element.getFirstChild());
            if (DEBUG)
                LOG.finest("* parseElement(" + element.getNodeName() + ") : map with "
                        + mapConfiguration.getMapConfiguration().size() + " items.");
            return mapConfiguration;
        }
        List<Element> elements = elementsList(element.getChildNodes());
        if (elements.size() > 1 || (elements.size() == 1 && isPrimitive(elements.get(0))) || isList(guessedClass))
        {
            if (DEBUG)
                LOG.finest("* parseElement() this is a list !");
            return parseList(elements);
        }
        if (DEBUG)
            LOG.finest("* parseElement() this is a ... I don't know, object ?");
        return parseObject(element, guessedClass);
    }

    private boolean isList(String guessedClass)
    {
        return "java.util.List".equals(guessedClass);
    }

    private boolean isPrimitive(Element element)
    {
        String nodeName = element.getNodeName();
        if (DEBUG)
            LOG.finest("isPrimitive(" + nodeName + ")");
        if (nodeName.equals("float") || nodeName.equals("int") || nodeName.equals("string"))
        {
            if (DEBUG)
                LOG.finest("isPrimitive(" + nodeName + ") => true");
            return true;
        }
        if (DEBUG)
            LOG.finest("isPrimitive(" + nodeName + ") => false");
        return false;
    }

    private ElementConfiguration parseList(List<Element> elements)
    {
        List<ElementConfiguration> lec = new ArrayList<ElementConfiguration>();
        for (Element childElement : elements)
        {
            lec.add(parseElement(childElement, null));
        }
        ListConfiguration lc = new ListConfigurationBean();
        lc.setListConfiguration(lec);
        return lc;
    }

    private MapConfiguration parseMap(Element element)
    {
        MapConfiguration mc = new MapConfigurationBean();
        mc.setMapConfiguration(new HashMap<ElementConfiguration, ElementConfiguration>());
        List<Element> elements = elementsList(element.getChildNodes());
        for (int idx = 0; idx < elements.size(); idx += 2)
        {
            ElementConfiguration key = parseElement(elements.get(idx), null);
            ElementConfiguration value = parseElement(elements.get(idx + 1), null);
            mc.getMapConfiguration().put(key, value);
        }
        return mc;
    }

}

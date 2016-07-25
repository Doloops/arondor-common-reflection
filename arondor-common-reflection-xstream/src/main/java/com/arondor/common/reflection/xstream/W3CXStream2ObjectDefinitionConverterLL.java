package com.arondor.common.reflection.xstream;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.arondor.common.reflection.bean.config.ListConfigurationBean;
import com.arondor.common.reflection.bean.config.ObjectConfigurationBean;
import com.arondor.common.reflection.bean.config.PrimitiveConfigurationBean;
import com.arondor.common.reflection.model.config.ElementConfiguration;
import com.arondor.common.reflection.model.config.ListConfiguration;
import com.arondor.common.reflection.model.config.ObjectConfiguration;
import com.arondor.common.reflection.model.config.PrimitiveConfiguration;

public class W3CXStream2ObjectDefinitionConverterLL
{
    private static final Logger LOG = Logger.getLogger(W3CXStream2ObjectDefinitionConverterLL.class);

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
        LOG.debug("parseObject() objectElement=" + objectElement.getNodeName());
        ObjectConfiguration oc = new ObjectConfigurationBean();
        if (guessedParentClass == null)
        {
            guessedParentClass = objectElement.getNodeName();
        }
        oc.setClassName(guessedParentClass);
        oc.setFields(new HashMap<String, ElementConfiguration>());
        for (Node childNode = objectElement.getFirstChild(); childNode != null; childNode = childNode.getNextSibling())
        {
            LOG.debug("* parseObject() childNode=" + childNode.getNodeType() + " - " + childNode.getNodeName());
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
                LOG.debug("Not handled : typpe " + childNode.getNodeType());
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
        LOG.debug("* parseElement() element=" + element.getNodeName() + ", guessedClass=" + guessedClass);
        if (element.getChildNodes().getLength() == 1 && element.getFirstChild().getNodeType() == Node.TEXT_NODE)
        {
            String nodeValue = element.getFirstChild().getNodeValue();
            LOG.debug("Primitive type : " + element.getNodeName() + "=" + nodeValue);
            PrimitiveConfiguration pc = new PrimitiveConfigurationBean();
            pc.setValue(nodeValue);
            return pc;
        }
        if (guessedClass != null && !isList(guessedClass))
        {
            LOG.debug("* parseElement() this is an explicit object : '" + guessedClass + "'");
            return parseObject(element, guessedClass);
        }

        List<Element> elements = elementsList(element.getChildNodes());
        if (elements.size() > 1 || (elements.size() == 1 && isPrimitive(elements.get(0))) || isList(guessedClass))
        {
            LOG.debug("* parseElement() this is a list !");
            return parseList(elements);
        }
        LOG.debug("* parseElement() this is a ... I don't know, object ?");
        return parseObject(element, guessedClass);
    }

    private boolean isList(String guessedClass)
    {
        return java.util.List.class.getName().equals(guessedClass);
    }

    private boolean isPrimitive(Element element)
    {
        String nodeName = element.getNodeName();
        LOG.debug("isPrimitive(" + nodeName + ")");
        if (nodeName.equals("float") || nodeName.equals("int") || nodeName.equals("string"))
        {
            LOG.debug("isPrimitive(" + nodeName + ") => true");
            return true;
        }
        LOG.debug("isPrimitive(" + nodeName + ") => false");
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

}

package com.arondor.common.reflection.parser.spring;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import com.arondor.common.reflection.model.config.ElementConfiguration;
import com.arondor.common.reflection.model.config.ListConfiguration;
import com.arondor.common.reflection.model.config.MapConfiguration;
import com.arondor.common.reflection.model.config.ObjectConfiguration;
import com.arondor.common.reflection.model.config.ObjectConfigurationMap;
import com.arondor.common.reflection.model.config.PrimitiveConfiguration;
import com.arondor.common.reflection.model.config.ReferenceConfiguration;
import com.arondor.common.reflection.parser.spring.XMLBeanTagsConstant.Namespace;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;

/**
 * Class to serialize {@link ObjectConfigurationMap} and write it to xml file
 * 
 * @author Caroline Gaudin
 */

public class XMLBeanDefinitionWriter
{

    private static final Logger LOGGER = Logger.getLogger(XMLBeanDefinitionWriter.class);

    private Element newElement(Document document, String name, Namespace ns)
    {
        if (ns.getPrefix() != null)
        {
            name = ns.getPrefix() + ":" + name;
        }
        return document.createElement(name);
    }

    private void setAttribute(Element element, String name, String value, Namespace ns)
    {
        if (ns.getPrefix() != null)
        {
            name = ns.getPrefix() + ":" + name;
        }
        element.setAttribute(name, value);
    }

    private void addNamespaceDeclaration(Element element, Namespace ns)
    {
        String name = "xmlns";
        if (ns.getPrefix() != null)
        {
            name += ":" + ns.getPrefix();
        }
        element.setAttribute(name, ns.getUri());
    }

    /**
     * Serialize objectConfigurationMap to a JDOM object
     * 
     * @param objectConfigurationMap
     *            object configuration to serialize
     * @return a JDOM Document
     */
    public void write(Document document, ObjectConfigurationMap objectConfigurationMap)
    {

        if (objectConfigurationMap == null || objectConfigurationMap.isEmpty())
        {
            LOGGER.warn("ObjectConfigurationMap is empty, skipping objectConfiguration writting");
        }

        Element rootElement = initRootElement(document);
        /**
         * For each objectConfiguration, serialize it
         */
        for (Entry<String, ObjectConfiguration> objectConfEntry : objectConfigurationMap.entrySet())
        {
            LOGGER.debug("Bean defintion name : " + objectConfEntry.getKey());
            Element objectConfElement = newElement(document, XMLBeanTagsConstant.BEAN_TAG,
                    XMLBeanTagsConstant.NAMESPACE_BEANS);
            rootElement.appendChild(objectConfElement);
            serializeBeanConfiguration(objectConfElement, objectConfEntry.getKey(), objectConfEntry.getValue(), true);
        }
    }

    /**
     * Serialize objectConfigurationMap and save it on target xml file
     * 
     * @param objectConfigurationMap
     *            {@link ObjectConfigurationMap} : to serialize and save to
     *            target file
     * @param path
     * @throws IOException
     */
    // public void write(ObjectConfigurationMap objectConfigurationMap, String
    // path) throws IOException
    // {
    // Document document = write(objectConfigurationMap);
    // if (document != null)
    // {
    // XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());
    // xmlOutputter.output(document, new FileOutputStream(path));
    // }
    // }

    /**
     * Write generic header informations for xml file
     * 
     * @param document
     * 
     * @return
     */
    private Element initRootElement(Document document)
    {
        Element rootElement = newElement(document, XMLBeanTagsConstant.ALL_BEANS_TAG,
                XMLBeanTagsConstant.NAMESPACE_BEANS);
        document.appendChild(rootElement);

        addNamespaceDeclaration(rootElement, XMLBeanTagsConstant.NAMESPACE_BEANS);
        addNamespaceDeclaration(rootElement, XMLBeanTagsConstant.NAMESPACE_XSI);
        addNamespaceDeclaration(rootElement, XMLBeanTagsConstant.NAMESPACE_CONTEXT);
        setAttribute(rootElement, XMLBeanTagsConstant.SCHEMA_LOCATION_NAME, XMLBeanTagsConstant.SCHEMA_LOCATION,
                XMLBeanTagsConstant.NAMESPACE_XSI);

        for (Entry<String, String> entry : XMLBeanTagsConstant.HEADER_ATTRIBUTE_MAP.entrySet())
        {
            rootElement.setAttribute(entry.getKey(), entry.getValue());
        }

        return rootElement;
    }

    /**
     * Serialize an element configuration, in function of its
     * ElementConfigurationType
     * 
     * @param objectConfElement
     * @param elementConfiguration
     * @param isProperty
     *            : no new JDOM element created because it is a property (only
     *            used for PrimitiveConfiguration)
     */
    private void serializeElementConfiguration(Element objectConfElement, ElementConfiguration elementConfiguration,
            boolean isProperty)
    {

        switch (elementConfiguration.getFieldConfigurationType())
        {
        case Map:
            serializeMapConfiguration(objectConfElement, (MapConfiguration) elementConfiguration);
            break;
        case List:
            serializeListConfiguration(objectConfElement, (ListConfiguration) elementConfiguration);
            break;
        case Object:
            serializeBeanConfiguration(objectConfElement, null, (ObjectConfiguration) elementConfiguration, false);
            break;
        case Primitive:
            serializePrimitiveConfiguration(objectConfElement, (PrimitiveConfiguration) elementConfiguration,
                    isProperty);
            break;
        case Reference:
            serializeReferenceConfiguration(objectConfElement, (ReferenceConfiguration) elementConfiguration);
            break;
        }

    }

    /**
     * Serialize bean definition
     * 
     * @param objectConfElement
     * @param beanName
     * @param objectConfiguration
     * @param isParent
     *            : create a new JDOM element
     * @return
     */
    private void serializeBeanConfiguration(Element objectConfElement, String beanName,
            ObjectConfiguration objectConfiguration, boolean isParent)
    {
        Element beanDefinitionElement = objectConfElement;
        if (!isParent)
        {
            beanDefinitionElement = newElement(objectConfElement.getOwnerDocument(), XMLBeanTagsConstant.BEAN_TAG,
                    XMLBeanTagsConstant.NAMESPACE_BEANS);
            objectConfElement.appendChild(beanDefinitionElement);
        }

        String objectName = objectConfiguration.getObjectName();
        String className = objectConfiguration.getClassName();
        boolean singleton = objectConfiguration.isSingleton();
        if (objectName != null)
        {
            LOGGER.debug("Bean definition name : " + objectName);
            beanDefinitionElement.setAttribute(XMLBeanTagsConstant.BEAN_ID_TAG, objectName);
        }
        else
        {
            // Just use for log message to have contextual value
            objectName = beanName;
        }
        LOGGER.debug("Bean definition name = " + objectName + ", class name = " + className + ", isSingleton = "
                + singleton);
        if (className == null)
        {
            LOGGER.error("No class name defined for bean definition=" + objectName);
            throw new RuntimeException("Bean definition=" + objectName + " has no class name defined");
        }

        beanDefinitionElement.setAttribute(XMLBeanTagsConstant.BEAN_CLASS_TAG, className);
        if (singleton)
        {
            beanDefinitionElement.setAttribute(XMLBeanTagsConstant.BEAN_SCOPE_TAG, XMLBeanTagsConstant.SINGLETON);
        }
        else
        {
            beanDefinitionElement.setAttribute(XMLBeanTagsConstant.BEAN_SCOPE_TAG, XMLBeanTagsConstant.PROTOTYPE);
        }

        serializeConstructorArg(beanDefinitionElement, objectName, objectConfiguration.getConstructorArguments());

        serializeBeanDefinitionProperties(beanDefinitionElement, objectName, objectConfiguration.getFields());

    }

    /**
     * Serialize constructor arguments list of bean definition
     * 
     * @param beanDefinitionElement
     * @param beanName
     * @param constructorArguments
     */
    private void serializeConstructorArg(Element beanDefinitionElement, String beanName,
            List<ElementConfiguration> constructorArguments)
    {
        if (constructorArguments == null || constructorArguments.isEmpty())
        {
            LOGGER.debug("No contructor arguments defined for bean definition=" + beanName);
            return;
        }

        Element constructorArg = newElement(beanDefinitionElement.getOwnerDocument(),
                XMLBeanTagsConstant.CONSTRUCTOR_ARG_TAG, XMLBeanTagsConstant.NAMESPACE_BEANS);
        beanDefinitionElement.appendChild(constructorArg);

        for (ElementConfiguration elementConfiguration : constructorArguments)
        {
            serializeElementConfiguration(constructorArg, elementConfiguration, false);
        }

    }

    /**
     * Serialize properties list of bean definition
     * 
     * @param beanDefinitionElement
     * @param beanName
     * @param fields
     */
    private void serializeBeanDefinitionProperties(Element beanDefinitionElement, String beanName,
            Map<String, ElementConfiguration> fields)
    {
        if (fields == null || fields.isEmpty())
        {
            LOGGER.debug("No properties associated with bean definition=" + beanName);
            return;
        }

        for (Entry<String, ElementConfiguration> fieldEntry : fields.entrySet())
        {
            LOGGER.debug("Add property " + fieldEntry.getKey() + " for bean definition " + beanName);
            Element propertyElement = newElement(beanDefinitionElement.getOwnerDocument(),
                    XMLBeanTagsConstant.PROPERTY_TAG, XMLBeanTagsConstant.NAMESPACE_BEANS);
            propertyElement.setAttribute(XMLBeanTagsConstant.PROPERTY_NAME_TAG, fieldEntry.getKey());
            beanDefinitionElement.appendChild(propertyElement);
            serializeElementConfiguration(propertyElement, fieldEntry.getValue(), true);
        }

    }

    /**
     * Serialize reference configuration
     * 
     * @param parentElement
     * @param elementConfiguration
     * @return
     */
    private void serializeReferenceConfiguration(Element parentElement, ReferenceConfiguration refConfiguration)
    {
        Element refElement = newElement(parentElement.getOwnerDocument(), XMLBeanTagsConstant.REF_TAG,
                XMLBeanTagsConstant.NAMESPACE_BEANS);
        parentElement.appendChild(refElement);
        String reference = refConfiguration.getReferenceName();
        if (reference == null)
        {
            LOGGER.error("No reference defined");
            throw new BeanSerializeException("No reference defined");
        }
        refElement.setAttribute(XMLBeanTagsConstant.BEAN_TAG, reference);

    }

    /**
     * Serialize primitive configuration
     * 
     * @param parentElement
     * @param elementConfiguration
     * @return
     */
    private void serializePrimitiveConfiguration(Element parentElement, PrimitiveConfiguration primitiveConfiguration,
            boolean isProperty)
    {

        String value = primitiveConfiguration.getValue();
        if (value == null)
        {
            LOGGER.error("No value defined");
            throw new BeanSerializeException("No value defined");
        }
        if (!isProperty)
        {
            Element valueElement = newElement(parentElement.getOwnerDocument(), XMLBeanTagsConstant.PROPERTY_VALUE_TAG,
                    XMLBeanTagsConstant.NAMESPACE_BEANS);
            parentElement.appendChild(valueElement);
            valueElement.appendChild(parentElement.getOwnerDocument().createTextNode(value));
        }
        else
        {
            parentElement.setAttribute(XMLBeanTagsConstant.PROPERTY_VALUE_TAG, value);
        }

    }

    /**
     * Serialize list of element configuration
     * 
     * @param parentElement
     * @param listConfiguration
     */
    private void serializeListConfiguration(Element parentElement, ListConfiguration listConfiguration)
    {

        Element listElement = newElement(parentElement.getOwnerDocument(), XMLBeanTagsConstant.LIST_TAG,
                XMLBeanTagsConstant.NAMESPACE_BEANS);
        parentElement.appendChild(listElement);

        List<ElementConfiguration> elementConfigurationList = listConfiguration.getListConfiguration();
        if (elementConfigurationList == null)
        {
            LOGGER.error("No list defined");
            throw new BeanSerializeException("No list defined");
        }
        for (ElementConfiguration elementConfiguration : elementConfigurationList)
        {
            serializeElementConfiguration(listElement, elementConfiguration, false);
        }

    }

    /**
     * Serialize map of element configuration
     * 
     * @param parentElement
     * @param elementConfiguration
     */
    private void serializeMapConfiguration(Element parentElement, MapConfiguration mapConfiguration)
    {
        Element mapElement = newElement(parentElement.getOwnerDocument(), XMLBeanTagsConstant.MAP_TAG,
                XMLBeanTagsConstant.NAMESPACE_BEANS);
        parentElement.appendChild(mapElement);

        Map<ElementConfiguration, ElementConfiguration> elementConfMap = mapConfiguration.getMapConfiguration();
        if (elementConfMap == null)
        {
            LOGGER.error("No map defined");
            throw new BeanSerializeException("No map defined");
        }

        for (Entry<ElementConfiguration, ElementConfiguration> elementConfEntry : elementConfMap.entrySet())
        {

            Element entryElement = newElement(parentElement.getOwnerDocument(), XMLBeanTagsConstant.ENTRY_TAG,
                    XMLBeanTagsConstant.NAMESPACE_BEANS);
            mapElement.appendChild(entryElement);
            Element keyElement = newElement(parentElement.getOwnerDocument(), XMLBeanTagsConstant.KEY_TAG,
                    XMLBeanTagsConstant.NAMESPACE_BEANS);
            entryElement.appendChild(keyElement);
            serializeElementConfiguration(keyElement, elementConfEntry.getKey(), false);

            ElementConfiguration value = elementConfEntry.getValue();
            serializeElementConfiguration(entryElement, value, true);

        }

    }
}

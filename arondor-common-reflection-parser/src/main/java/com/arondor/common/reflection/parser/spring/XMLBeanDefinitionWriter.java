package com.arondor.common.reflection.parser.spring;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import com.arondor.common.reflection.model.config.ElementConfiguration;
import com.arondor.common.reflection.model.config.ListConfiguration;
import com.arondor.common.reflection.model.config.MapConfiguration;
import com.arondor.common.reflection.model.config.ObjectConfiguration;
import com.arondor.common.reflection.model.config.ObjectConfigurationMap;
import com.arondor.common.reflection.model.config.PrimitiveConfiguration;
import com.arondor.common.reflection.model.config.ReferenceConfiguration;

/**
 * Class to serialize {@link ObjectConfigurationMap} and write it to xml file
 * 
 * @author Caroline Gaudin
 */

public class XMLBeanDefinitionWriter
{

    private static final Logger LOGGER = Logger.getLogger(XMLBeanDefinitionWriter.class);

    /**
     * Serialize objectConfigurationMap to a JDOM object
     * 
     * @param objectConfigurationMap
     *            object configuration to serialize
     * @return a JDOM Document
     */
    public Document write(ObjectConfigurationMap objectConfigurationMap)
    {

        if (objectConfigurationMap == null || objectConfigurationMap.isEmpty())
        {
            LOGGER.warn("ObjectConfigurationMap is empty, skipping objectConfiguration writting");
            return null;
        }

        Element rootElement = initConfigurationSerializing();

        /**
         * For each objectConfiguration, serialize it
         */
        for (Entry<String, ObjectConfiguration> objectConfEntry : objectConfigurationMap.entrySet())
        {
            LOGGER.debug("Bean defintion name : " + objectConfEntry.getKey());
            Element objectConfElement = new Element(XMLBeanTagsConstant.BEAN_TAG, XMLBeanTagsConstant.NAMESPACE);
            rootElement.addContent(objectConfElement);
            serializeBeanConfiguration(objectConfElement, objectConfEntry.getKey(), objectConfEntry.getValue(), true);
        }

        /**
         * Xml document construction and save it as target file
         */
        Document document = new Document(rootElement);
        return document;
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
    public void write(ObjectConfigurationMap objectConfigurationMap, String path) throws IOException
    {
        Document document = write(objectConfigurationMap);
        if (document != null)
        {
            XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());
            xmlOutputter.output(document, new FileOutputStream(path));
        }
    }

    /**
     * Write generic header informations for xml file
     * 
     * @return
     */
    private Element initConfigurationSerializing()
    {
        Element rootElement = new Element(XMLBeanTagsConstant.ALL_BEANS_TAG, XMLBeanTagsConstant.NAMESPACE);

        rootElement.addNamespaceDeclaration(XMLBeanTagsConstant.NAMESPACE_XSI);
        rootElement.setAttribute(XMLBeanTagsConstant.SCHEMA_LOCATION_NAME, XMLBeanTagsConstant.SCHEMA_LOCATION,
                XMLBeanTagsConstant.NAMESPACE_XSI);
        rootElement.addNamespaceDeclaration(XMLBeanTagsConstant.NAMESPACE_CONTEXT);

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
            beanDefinitionElement = new Element(XMLBeanTagsConstant.BEAN_TAG, XMLBeanTagsConstant.NAMESPACE);
            objectConfElement.addContent(beanDefinitionElement);
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

        Element constructorArg = new Element(XMLBeanTagsConstant.CONSTRUCTOR_ARG_TAG, XMLBeanTagsConstant.NAMESPACE);
        beanDefinitionElement.addContent(constructorArg);

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
            Element propertyElement = new Element(XMLBeanTagsConstant.PROPERTY_TAG, XMLBeanTagsConstant.NAMESPACE);
            propertyElement.setAttribute(XMLBeanTagsConstant.PROPERTY_NAME_TAG, fieldEntry.getKey());
            beanDefinitionElement.addContent(propertyElement);
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

        Element refElement = new Element(XMLBeanTagsConstant.REF_TAG, XMLBeanTagsConstant.NAMESPACE);
        parentElement.addContent(refElement);
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
            Element valueElement = new Element(XMLBeanTagsConstant.PROPERTY_VALUE_TAG, XMLBeanTagsConstant.NAMESPACE);
            parentElement.addContent(valueElement);
            valueElement.addContent(value);
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

        Element listElement = new Element(XMLBeanTagsConstant.LIST_TAG, XMLBeanTagsConstant.NAMESPACE);
        parentElement.addContent(listElement);

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
        Element mapElement = new Element(XMLBeanTagsConstant.MAP_TAG, XMLBeanTagsConstant.NAMESPACE);
        parentElement.addContent(mapElement);

        Map<ElementConfiguration, ElementConfiguration> elementConfMap = mapConfiguration.getMapConfiguration();
        if (elementConfMap == null)
        {
            LOGGER.error("No map defined");
            throw new BeanSerializeException("No map defined");
        }

        for (Entry<ElementConfiguration, ElementConfiguration> elementConfEntry : elementConfMap.entrySet())
        {

            Element entryElement = new Element(XMLBeanTagsConstant.ENTRY_TAG, XMLBeanTagsConstant.NAMESPACE);
            mapElement.addContent(entryElement);
            Element keyElement = new Element(XMLBeanTagsConstant.KEY_TAG, XMLBeanTagsConstant.NAMESPACE);
            entryElement.addContent(keyElement);
            serializeElementConfiguration(keyElement, elementConfEntry.getKey(), false);

            ElementConfiguration value = elementConfEntry.getValue();
            serializeElementConfiguration(entryElement, value, true);

        }

    }
}

package com.arondor.common.reflection.parser.spring;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

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
     * Serialize objectConfigurationMap and save it on target xml file
     * 
     * @param objectConfigurationMap {@link ObjectConfigurationMap} : to serialize and save to target file
     * @param path
     */
    public void write(ObjectConfigurationMap objectConfigurationMap, String path)
    {

        if (objectConfigurationMap == null || objectConfigurationMap.isEmpty())
        {
            LOGGER.warn("ObjectConfigurationMap is empty, skipping objectConfiguration writting");
            return;
        }

        Element rootElement = initConfigurationSerializing();

        /**
         * For each objectConfiguration, serialize it
         */
        for (String beanDefinitionName : objectConfigurationMap.keySet())
        {
            LOGGER.debug("Bean defintion name : " + beanDefinitionName);
            Element objectConfElement = new Element(XMLBeanTagsConstant.BEAN_TAG, XMLBeanTagsConstant.NAMESPACE);
            rootElement.addContent(objectConfElement);
            serializeBeanConfiguration(objectConfElement, beanDefinitionName,
                    objectConfigurationMap.get(beanDefinitionName), false);
        }

        /**
         * Xml document construction and save it as target file
         */
        Document document = new Document(rootElement);
        writeConfigurationFile(path, document);

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
     * @param newElement
     *            : create a new JDOM element
     */
    private void serializeElementConfiguration(Element objectConfElement, ElementConfiguration elementConfiguration,
            boolean newElement)
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
            serializeBeanConfiguration(objectConfElement, null, (ObjectConfiguration) elementConfiguration, newElement);
            break;
        case Primitive:
            serializePrimitiveConfiguration(objectConfElement, (PrimitiveConfiguration) elementConfiguration,
                    newElement);
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
     * @param newElement
     *            : create a new JDOM element
     * @return
     */
    private void serializeBeanConfiguration(Element objectConfElement, String beanName,
            ObjectConfiguration objectConfiguration, boolean newElement)
    {

        Element beanDefinitionElement = objectConfElement;
        if (newElement)
        {
            beanDefinitionElement = new Element(XMLBeanTagsConstant.BEAN_TAG, XMLBeanTagsConstant.NAMESPACE);
            objectConfElement.addContent(beanDefinitionElement);
        }

        String objectName = objectConfiguration.getObjectName();
        String className = objectConfiguration.getClassName();
        boolean singleton = objectConfiguration.isSingleton();
        if (objectName != null)
        {
            // TODO it must be an error if objectName is null ?
            LOGGER.debug("Bean definition name : " + objectName);
            beanName = objectName;
            beanDefinitionElement.setAttribute(XMLBeanTagsConstant.BEAN_ID_TAG, objectName);
        }
        LOGGER.debug("Bean definition name = " + beanName + ", class name = " + className + ", isSingleton = " + singleton);
        if (className == null)
        {
            LOGGER.error("No class name defined for bean definition=" + beanName);
            throw new RuntimeException("Bean definition=" + beanName + " has no class name defined");
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

        serializeConstructorArg(beanDefinitionElement, beanName, objectConfiguration.getConstructorArguments());

        serializeBeanDefinitionProperties(beanDefinitionElement, beanName, objectConfiguration.getFields());

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
            serializeElementConfiguration(constructorArg, elementConfiguration, true);
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

        for (String fieldName : fields.keySet())
        {
            LOGGER.debug("Add property " + fieldName + " for bean definition " + beanName);
            Element propertyElement = new Element(XMLBeanTagsConstant.PROPERTY_TAG, XMLBeanTagsConstant.NAMESPACE);
            propertyElement.setAttribute(XMLBeanTagsConstant.PROPERTY_NAME_TAG, fieldName);
            beanDefinitionElement.addContent(propertyElement);
            serializeElementConfiguration(propertyElement, fields.get(fieldName), true);
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
            // TODO check if it must be an error ?
            LOGGER.error("No reference defined");
            throw new RuntimeException("No reference defined");
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
            boolean newElement)
    {

        String value = primitiveConfiguration.getValue();
        if (value == null)
        {
            // TODO check if it must be an error ?
            LOGGER.error("No value defined");
            throw new RuntimeException("No value defined");
        }
        if (newElement)
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
        if (elementConfigurationList == null || elementConfigurationList.isEmpty())
        {
            // TODO check if it must be an error ?
            LOGGER.error("No list defined");
            throw new RuntimeException("No list defined");
        }
        for (ElementConfiguration elementConfiguration : elementConfigurationList)
        {
            serializeElementConfiguration(listElement, elementConfiguration, true);
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
        if (elementConfMap == null || elementConfMap.isEmpty())
        {
            // TODO check if it must be an error ?
            LOGGER.error("No map defined");
            throw new RuntimeException("No map defined");
        }

        for (ElementConfiguration key : elementConfMap.keySet())
        {

            Element entryElement = new Element(XMLBeanTagsConstant.ENTRY_TAG, XMLBeanTagsConstant.NAMESPACE);
            mapElement.addContent(entryElement);
            Element keyElement = new Element(XMLBeanTagsConstant.KEY_TAG, XMLBeanTagsConstant.NAMESPACE);
            entryElement.addContent(keyElement);
            serializeElementConfiguration(keyElement, key, true);

            ElementConfiguration value = elementConfMap.get(key);
            serializeElementConfiguration(entryElement, value, true);

        }

    }

    /**
     * Write objectConfigurationMap serialized to the target file
     * 
     * @param path
     * @param document
     */
    private void writeConfigurationFile(String path, Document document)
    {
        XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());
        try
        {
            xmlOutputter.output(document, new FileOutputStream(path));
        }
        catch (FileNotFoundException e)
        {
            LOGGER.error("Error during xml configuration file saving", e);
            // TODO which exception ?
            throw new RuntimeException(e);
        }
        catch (IOException e)
        {
            LOGGER.error("Error during xml configuration file saving", e);
            // TODO which exception ?
            throw new RuntimeException(e);
        }
    }
}

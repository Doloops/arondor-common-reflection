package com.arondor.common.reflection.parser.spring;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.jdom.DocType;
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
 * Class to write bean definition in xml file
 * 
 * @author Caroline Gaudin
 * 
 */

public class XMLBeanDefinitionWriter
{

    private static final Logger LOGGER = Logger.getLogger(XMLBeanDefinitionParser.class);

    /**
     * Method to write objectConfiguration map to xml file
     * 
     * @param objectConfigurationMap
     * @param path
     */
    public void write(ObjectConfigurationMap objectConfigurationMap, String path)
    {

        if (objectConfigurationMap == null || objectConfigurationMap.isEmpty())
        {
            LOGGER.warn("ObjectConfigurationMap is empty, skipping objectConfiguration writting");
        }

        Element rootElement = initConfigurationSerializing();

        for (String beanDefinitionName : objectConfigurationMap.keySet())
        {
            LOGGER.debug("Bean defintion name : " + beanDefinitionName);
            Element objectConfElement = new Element(XMLBeanTagsConstant.BEAN_TAG);
            rootElement.addContent(objectConfElement);
            serializeBeanConfiguration(objectConfElement, objectConfigurationMap.get(beanDefinitionName), false);
        }

        Document document = new Document(rootElement);
        // TODO a supprimer lorsque le probleme de ns est regle
        DocType docType = new DocType("beans", "-//SPRING//DTD BEAN//EN",
                "http://www.springframework.org/dtd/spring-beans-2.0.dtd");
        document.setDocType(docType);
        writeConfigurationFile(path, document);

    }

    /**
     * Method to write the target file
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
            // TODO FBA créer une exception ?
        }
        catch (IOException e)
        {
            LOGGER.error("Error during xml configuration file saving", e);
            // TODO FBA créer une exception ?
        }
    }

    /**
     * Method to write generic header informations
     * 
     * @return
     */
    private Element initConfigurationSerializing()
    {
        Element rootElement = new Element(XMLBeanTagsConstant.ALL_BEANS_TAG);
        // TODO remettre en place le namespace
        // Element rootElement = new Element(XMLBeanTagsConstant.ALL_BEANS_TAG,
        // XMLBeanTagsConstant.NAMESPACE);
        // rootElement.addNamespaceDeclaration(XMLBeanTagsConstant.NAMESPACE_XSI);
        // rootElement.addNamespaceDeclaration(XMLBeanTagsConstant.NAMESPACE_CONTEXT);
        // rootElement.addNamespaceDeclaration(XMLBeanTagsConstant.NAMESPACE_SCHEME_LOCATION);
        for (Entry<String, String> entry : XMLBeanTagsConstant.HEADER_ATTRIBUTE_MAP.entrySet())
        {
            rootElement.setAttribute(entry.getKey(), entry.getValue());
        }
        return rootElement;
    }

    /**
     * Serialize constructor arguments of bean definition
     * 
     * @param beanDefinition
     * @param constructorArguments
     */
    private void serializeConstructorArg(Element beanDefinition, List<ElementConfiguration> constructorArguments)
    {
        if (constructorArguments == null || constructorArguments.isEmpty())
        {
            LOGGER.debug("No contructor arguments defined");
            return;
        }
        
        Element constructorArg = new Element(XMLBeanTagsConstant.CONSTRUCTOR_ARG_TAG);
        beanDefinition.addContent(constructorArg);

        for (ElementConfiguration elementConfiguration : constructorArguments)
        {
            serializeElementConfiguration(constructorArg, elementConfiguration, true);
        }

    }

    /**
     * List to serialize an element configuration
     * 
     * @param objectConfElement
     * @param elementConfiguration
     * @param newElement
     *            : create a new element in xml file
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
            serializeBeanConfiguration(objectConfElement, (ObjectConfiguration) elementConfiguration, newElement);
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
     * Method to serialize properties list of bean definition
     * 
     * @param objectConfElement
     * @param fields
     */
    private void serializeBeanDefinitionProperties(Element objectConfElement, Map<String, ElementConfiguration> fields)
    {
        if (fields == null || fields.isEmpty())
        {
            LOGGER.info("No properties associated with bean");
            return;
        }

        for (String fieldName : fields.keySet())
        {
            LOGGER.debug("Add property " + fieldName + " ...");
            Element propertyElement = new Element(XMLBeanTagsConstant.PROPERTY_TAG);
            propertyElement.setAttribute(XMLBeanTagsConstant.PROPERTY_NAME_TAG, fieldName);
            objectConfElement.addContent(propertyElement);
            serializeElementConfiguration(propertyElement, fields.get(fieldName), true);
        }

    }

    /**
     * Method to serialize bean configuration
     * 
     * @param objectConfiguration
     * @return
     */
    private void serializeBeanConfiguration(Element objectConfElement, ObjectConfiguration objectConfiguration,
            boolean newElement)
    {

        Element beanElement = objectConfElement;
        if (newElement)
        {
            beanElement = new Element(XMLBeanTagsConstant.BEAN_TAG);
            objectConfElement.addContent(beanElement);
        }

        String objectName = objectConfiguration.getObjectName();
        String className = objectConfiguration.getClassName();
        if (objectName != null)
        {
            beanElement.setAttribute(XMLBeanTagsConstant.BEAN_ID_TAG, objectName);
        }

        beanElement.setAttribute(XMLBeanTagsConstant.BEAN_CLASS_TAG, className);
        if (objectConfiguration.isSingleton())
        {
            beanElement.setAttribute(XMLBeanTagsConstant.BEAN_SCOPE_TAG, XMLBeanTagsConstant.SINGLETON);
        }
        else
        {
            beanElement.setAttribute(XMLBeanTagsConstant.BEAN_SCOPE_TAG, XMLBeanTagsConstant.PROTOTYPE);
        }
        serializeConstructorArg(beanElement, objectConfiguration.getConstructorArguments());
        serializeBeanDefinitionProperties(beanElement, objectConfiguration.getFields());

    }

    /**
     * Method to serialize reference configuration
     * 
     * @param parentElement
     * @param elementConfiguration
     * @return
     */
    private void serializeReferenceConfiguration(Element parentElement, ReferenceConfiguration refConfiguration)
    {

        Element refElement = new Element(XMLBeanTagsConstant.REF_TAG);
        parentElement.addContent(refElement);
        String reference = refConfiguration.getReferenceName();
        refElement.setAttribute(XMLBeanTagsConstant.BEAN_TAG,reference);
        
    }

    /**
     * Method to serialize primitive configuration
     * 
     * @param parentElement
     * @param elementConfiguration
     * @return
     */
    private void serializePrimitiveConfiguration(Element parentElement, PrimitiveConfiguration primitiveConfiguration,
            boolean newElement)
    {

        String value = primitiveConfiguration.getValue();
        if (newElement)
        {
            Element valueElement = new Element(XMLBeanTagsConstant.PROPERTY_VALUE_TAG);
            parentElement.addContent(valueElement);
            valueElement.addContent(value);
        }
        else
        {
            parentElement.setAttribute(XMLBeanTagsConstant.PROPERTY_VALUE_TAG, value);
        }

    }

    /**
     * Method to serialize list of element configuration
     * 
     * @param parentElement
     * @param listConfiguration
     */
    private void serializeListConfiguration(Element parentElement, ListConfiguration listConfiguration)
    {

        Element listElement = new Element(XMLBeanTagsConstant.LIST_TAG);
        parentElement.addContent(listElement);

        for (ElementConfiguration elementConfiguration : listConfiguration.getListConfiguration())
        {
            serializeElementConfiguration(listElement, elementConfiguration, true);
        }

    }

    /**
     * Method to serialize map of element configuration
     * 
     * @param parentElement
     * @param elementConfiguration
     */
    private void serializeMapConfiguration(Element parentElement, MapConfiguration mapConfiguration)
    {
        Element mapElement = new Element(XMLBeanTagsConstant.MAP_TAG);
        parentElement.addContent(mapElement);

        Map<ElementConfiguration, ElementConfiguration> elementConfMap = mapConfiguration.getMapConfiguration();

        for (ElementConfiguration key : elementConfMap.keySet())
        {

            Element entryElement = new Element(XMLBeanTagsConstant.ENTRY_TAG);
            mapElement.addContent(entryElement);
            Element keyElement = new Element(XMLBeanTagsConstant.KEY_TAG);
            entryElement.addContent(keyElement);
            serializeElementConfiguration(keyElement, key, true);

            ElementConfiguration value = elementConfMap.get(key);
            serializeElementConfiguration(entryElement, value, true);

        }

    }
}

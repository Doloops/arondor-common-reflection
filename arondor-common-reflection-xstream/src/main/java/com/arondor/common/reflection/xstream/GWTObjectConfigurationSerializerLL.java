package com.arondor.common.reflection.xstream;

import java.util.Map;
import java.util.logging.Logger;

import com.arondor.common.reflection.model.config.ElementConfiguration;
import com.arondor.common.reflection.model.config.ListConfiguration;
import com.arondor.common.reflection.model.config.MapConfiguration;
import com.arondor.common.reflection.model.config.ObjectConfiguration;
import com.arondor.common.reflection.model.config.PrimitiveConfiguration;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;

public class GWTObjectConfigurationSerializerLL
{
    private static final Logger LOG = Logger.getLogger(GWTObjectConfigurationSerializerLL.class.getName());

    private String classAttributeName = "class";

    protected String serialize(Document document, ElementConfiguration ec)
    {
        Element firstChild = doSerialize(document, ec, null);
        document.appendChild(firstChild);
        return document.toString();
    }

    protected Element doSerialize(Document document, ElementConfiguration ec, String name)
    {
        if (ec == null)
        {
            LOG.severe("Null element configuration here ! name=" + name);
            throw new IllegalArgumentException("Null value ! name=" + name);
        }
        String tagName = name, className = null;
        if (ec instanceof ObjectConfiguration)
        {
            ObjectConfiguration oc = (ObjectConfiguration) ec;
            if (oc.getObjectName() != null)
            {
                tagName = oc.getObjectName();
            }
            if (oc.getClassName() != null)
            {
                if (tagName == null)
                {
                    tagName = oc.getClassName();
                }
                else
                {
                    className = oc.getClassName();
                }
            }
        }
        if (tagName == null)
        {
            if (ec instanceof PrimitiveConfiguration)
            {
                tagName = "string";
            }
            else
            {
                // tagName = "{unknown}";
                throw new RuntimeException("Could not get a valid tag Name !");
            }
        }
        Element child = document.createElement(tagName);
        if (className != null)
        {
            child.setAttribute(classAttributeName, className);
        }
        if (ec instanceof ObjectConfiguration)
        {
            ObjectConfiguration oc = (ObjectConfiguration) ec;
            for (Map.Entry<String, ElementConfiguration> entry : oc.getFields().entrySet())
            {
                Element grandChild = doSerialize(document, entry.getValue(), entry.getKey());
                child.appendChild(grandChild);
            }
        }
        else if (ec instanceof PrimitiveConfiguration)
        {
            PrimitiveConfiguration pc = (PrimitiveConfiguration) ec;
            child.appendChild(document.createTextNode(pc.getValue()));
        }
        else if (ec instanceof ListConfiguration)
        {
            ListConfiguration lc = (ListConfiguration) ec;
            for (ElementConfiguration childEc : lc.getListConfiguration())
            {
                Element childElement = doSerialize(document, childEc, null);
                child.appendChild(childElement);
            }
        }
        else if (ec instanceof MapConfiguration)
        {
            MapConfiguration mc = (MapConfiguration) ec;
            Element mapElement = document.createElement("map");
            child.appendChild(mapElement);
            for (Map.Entry<ElementConfiguration, ElementConfiguration> entry : mc.getMapConfiguration().entrySet())
            {
                mapElement.appendChild(doSerialize(document, entry.getKey(), "key"));
                mapElement.appendChild(doSerialize(document, entry.getValue(), "value"));
            }
        }
        else
        {
            LOG.severe("Not supported ! " + ec.getClass().getName());
        }
        return child;
    }

}

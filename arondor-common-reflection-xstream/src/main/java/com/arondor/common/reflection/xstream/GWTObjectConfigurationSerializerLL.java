package com.arondor.common.reflection.xstream;

import java.util.Map;
import java.util.logging.Logger;

import com.arondor.common.reflection.model.config.ElementConfiguration;
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
            tagName = "{unknown}";
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
        return child;
    }

}

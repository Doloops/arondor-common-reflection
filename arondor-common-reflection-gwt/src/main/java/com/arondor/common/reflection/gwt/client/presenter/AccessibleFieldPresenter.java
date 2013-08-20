package com.arondor.common.reflection.gwt.client.presenter;

import java.util.logging.Logger;

import com.arondor.common.reflection.model.config.ElementConfiguration;
import com.arondor.common.reflection.model.config.ObjectConfigurationFactory;
import com.arondor.common.reflection.model.config.PrimitiveConfiguration;
import com.arondor.common.reflection.model.java.AccessibleField;
import com.arondor.common.reflection.util.PrimitiveTypeUtil;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.IsWidget;

public class AccessibleFieldPresenter
{
    private static final Logger LOG = Logger.getLogger(AccessibleFieldPresenter.class.getName());

    private final AccessibleField accessibleField;

    public interface Display extends IsWidget
    {
        void setName(String name);

        void setClassName(String className);

        void setDescription(String description);

        HasValue<String> getInputValue();

        void setInputValue(String value);
    }

    private final Display display;

    public AccessibleFieldPresenter(AccessibleField accessibleField, Display view)
    {
        this.accessibleField = accessibleField;
        this.display = view;
        bind();

        display.setName(accessibleField.getName());
        display.setClassName(accessibleField.getClassName());
        display.setDescription(accessibleField.getDescription());
    }

    public Display getDisplay()
    {
        return display;
    }

    public void bind()
    {
    }

    public void setElementConfiguration(ElementConfiguration elementConfiguration)
    {
        switch (elementConfiguration.getFieldConfigurationType())
        {
        case Primitive:
            display.setInputValue(((PrimitiveConfiguration) elementConfiguration).getValue());
            break;
        default:
            display.setInputValue("Not supported : " + elementConfiguration.getFieldConfigurationType());
        }

    }

    public ElementConfiguration getElementConfiguration(ObjectConfigurationFactory objectConfigurationFactory)
    {
        if (PrimitiveTypeUtil.isPrimitiveType(accessibleField.getClassName()))
        {
            String input = display.getInputValue().getValue();
            LOG.finest("field=" + accessibleField.getName() + ", class=" + accessibleField.getClassName() + ", value="
                    + input);
            if (!input.trim().isEmpty())
            {
                return objectConfigurationFactory.createPrimitiveConfiguration(input);
            }
        }
        return null;
    }
}

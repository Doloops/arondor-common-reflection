package com.arondor.common.reflection.gwt.client.presenter;

import java.util.ArrayList;
import java.util.logging.Logger;

import com.arondor.common.reflection.model.config.ElementConfiguration;
import com.arondor.common.reflection.model.config.ListConfiguration;
import com.arondor.common.reflection.model.config.ObjectConfigurationFactory;
import com.arondor.common.reflection.model.config.PrimitiveConfiguration;
import com.arondor.common.reflection.model.java.AccessibleField;
import com.arondor.common.reflection.util.PrimitiveTypeUtil;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.IsWidget;

public class AccessibleFieldPresenter
{
    @SuppressWarnings("unused")
    private static final Logger LOG = Logger.getLogger(AccessibleFieldPresenter.class.getName());

    public interface Display extends IsWidget
    {
        void setName(String name);

        void setClassName(String className);

        void setDescription(String description);

        HasValue<Object> getInputValue();

        void setInputValue(Object value);

        boolean getToConfig();
    }

    private final AccessibleField accessibleField;

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

    public void bind()
    {
    }

    public ElementConfiguration getElementConfiguration(ObjectConfigurationFactory objectConfigurationFactory)
    {
        if (display.getToConfig())
        {
            if (PrimitiveTypeUtil.isPrimitiveType(accessibleField.getClassName()))
            {
                Object input = display.getInputValue().getValue();

                if (input != null)
                {
                    if (!input.toString().trim().isEmpty())
                    {

                        return objectConfigurationFactory.createPrimitiveConfiguration(input.toString());
                    }
                }
            }
            if (accessibleField.getClassName().equals("java.util.List"))
            // && accessibleField.getGenericParameterClassList().size() == 1
            // &&
            // accessibleField.getGenericParameterClassList().get(0).equals("java.lang.String")
            {
                Object input = display.getInputValue().getValue();

                if (input != null)
                {
                    if (!input.toString().trim().isEmpty())
                    {
                        ListConfiguration listConf = objectConfigurationFactory.createListConfiguration();
                        listConf.setListConfiguration(new ArrayList<ElementConfiguration>());

                        String[] split = input.toString().split("\n");
                        for (int i = 0; i < split.length; i++)
                        {
                            listConf.getListConfiguration().add(
                                    objectConfigurationFactory.createPrimitiveConfiguration(split[i]));
                        }
                        LOG.finest(listConf.toString());
                        return listConf;
                    }
                }
            }
        }
        return null;
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

    public Display getDisplay()
    {
        return display;
    }
}

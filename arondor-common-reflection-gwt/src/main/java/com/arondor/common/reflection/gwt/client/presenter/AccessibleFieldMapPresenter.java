package com.arondor.common.reflection.gwt.client.presenter;

import java.util.HashMap;
import java.util.Map;

import com.arondor.common.reflection.model.config.ElementConfiguration;
import com.arondor.common.reflection.model.config.ObjectConfiguration;
import com.arondor.common.reflection.model.config.ObjectConfigurationFactory;
import com.arondor.common.reflection.model.java.AccessibleField;
import com.google.gwt.user.client.ui.IsWidget;

public class AccessibleFieldMapPresenter
{
    private Map<String, AccessibleFieldPresenter> accessibleFieldPresenters = new HashMap<String, AccessibleFieldPresenter>();

    public interface Display extends IsWidget
    {
        AccessibleFieldPresenter.Display createAccessibleFieldDisplay();
    }

    private final Display display;

    public AccessibleFieldMapPresenter(Display view)
    {
        this.display = view;
        bind();
    }

    public Display getDisplay()
    {
        return display;
    }

    public void bind()
    {

    }

    public void setAccessibleFields(Map<String, AccessibleField> accessibleFields)
    {
        for (AccessibleField accessibleField : accessibleFields.values())
        {
            AccessibleFieldPresenter.Display fieldDisplay = getDisplay().createAccessibleFieldDisplay();
            AccessibleFieldPresenter fieldPresenter = new AccessibleFieldPresenter(accessibleField, fieldDisplay);
            accessibleFieldPresenters.put(accessibleField.getName(), fieldPresenter);
        }
    }

    public void setObjectConfiguration(ObjectConfiguration objectConfiguration)
    {
        for (Map.Entry<String, ElementConfiguration> fieldEntry : objectConfiguration.getFields().entrySet())
        {
            if (accessibleFieldPresenters.containsKey(fieldEntry.getKey()))
            {
                accessibleFieldPresenters.get(fieldEntry.getKey()).setElementConfiguration(fieldEntry.getValue());
            }
        }
    }

    public void updateObjectConfiguration(ObjectConfigurationFactory objectConfigurationFactory,
            ObjectConfiguration objectConfiguration)
    {
        for (Map.Entry<String, AccessibleFieldPresenter> fieldEntry : accessibleFieldPresenters.entrySet())
        {
            ElementConfiguration elementConfiguration = fieldEntry.getValue().getElementConfiguration(
                    objectConfigurationFactory);
            if (elementConfiguration != null)
            {
                objectConfiguration.getFields().put(fieldEntry.getKey(), elementConfiguration);
            }
        }
    }
}

package com.arondor.common.reflection.gwt.client.presenter;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import com.arondor.common.reflection.model.config.ElementConfiguration;
import com.arondor.common.reflection.model.config.ElementConfiguration.ElementConfigurationType;
import com.arondor.common.reflection.model.config.ObjectConfiguration;
import com.arondor.common.reflection.model.config.ObjectConfigurationFactory;
import com.arondor.common.reflection.model.java.AccessibleField;
import com.arondor.common.reflection.util.PrimitiveTypeUtil;
import com.google.gwt.user.client.ui.IsWidget;

public class AccessibleFieldMapPresenter
{
    private static final Logger LOG = Logger.getLogger(AccessibleFieldMapPresenter.class.getName());

    public interface Display extends IsWidget
    {
        AccessibleFieldPresenter.Display createAccessibleFieldDisplay();

        void clearList();
    }

    private final Display display;

    private final Map<String, AccessibleFieldPresenter> accessibleFieldPresenters = new HashMap<String, AccessibleFieldPresenter>();

    public AccessibleFieldMapPresenter(Display view)
    {
        this.display = view;
        bind();
    }

    public void bind()
    {

    }

    public void setAccessibleFields(Map<String, AccessibleField> accessibleFields)
    {
        LOG.finest("setAccessibleFields : " + accessibleFields.size() + " fields");
        for (AccessibleField accessibleField : accessibleFields.values())
        {
            if (PrimitiveTypeUtil.isPrimitiveType(accessibleField.getClassName()))
            {
                AccessibleFieldPresenter.Display fieldDisplay = getDisplay().createAccessibleFieldDisplay();
                AccessibleFieldPresenter fieldPresenter = new AccessibleFieldPresenter(accessibleField, fieldDisplay);
                accessibleFieldPresenters.put(accessibleField.getName(), fieldPresenter);
            }
        }
    }

    public void setObjectConfiguration(ObjectConfiguration objectConfiguration)
    {
        for (Map.Entry<String, ElementConfiguration> fieldEntry : objectConfiguration.getFields().entrySet())
        {
            if (accessibleFieldPresenters.containsKey(fieldEntry.getKey()))
            {
                ElementConfiguration elementConfiguration = fieldEntry.getValue();
                if (elementConfiguration.getFieldConfigurationType() == ElementConfigurationType.Primitive)
                {
                    accessibleFieldPresenters.get(fieldEntry.getKey()).setElementConfiguration(elementConfiguration);
                }
            }
        }
    }

    public void updateFieldsConfiguration(ObjectConfigurationFactory objectConfigurationFactory,
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

    public Map<String, AccessibleFieldPresenter> getAccessibleFieldPresenters()
    {
        return accessibleFieldPresenters;
    }

    public Display getDisplay()
    {
        return display;
    }

    public void clear()
    {
        accessibleFieldPresenters.clear();
        display.clearList();
    }
}

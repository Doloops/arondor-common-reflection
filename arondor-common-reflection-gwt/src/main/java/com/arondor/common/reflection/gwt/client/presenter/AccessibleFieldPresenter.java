package com.arondor.common.reflection.gwt.client.presenter;

import java.util.logging.Logger;

import com.arondor.common.reflection.model.java.AccessibleField;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.IsWidget;

public class AccessibleFieldPresenter
{
    private static final Logger LOG = Logger.getLogger(AccessibleFieldPresenter.class.getName());

    private AccessibleField accessibleField;

    public interface Display extends IsWidget
    {
        void setName(String name);

        void setClassName(String className);

        void setDescription(String description);

        HasValue<String> getInputValue();

        void setInputValue(String value);
    }

    private final Display display;

    public AccessibleFieldPresenter(Display view)
    {
        this.display = view;
        bind();
        // RootPanel.get().add(display.asWidget());
    }

    public Display getDisplay()
    {
        return display;
    }

    public void bind()
    {
    }

    public void setAccessibleField(AccessibleField accessibleField)
    {
        this.accessibleField = accessibleField;
        display.setName(accessibleField.getName());
        display.setClassName(accessibleField.getClassName());
        display.setDescription(accessibleField.getDescription());
    }

}

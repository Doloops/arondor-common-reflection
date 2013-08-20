package com.arondor.common.reflection.gwt.client.presenter;

import java.util.logging.Logger;

import com.arondor.common.reflection.model.java.AccessibleField;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.RootPanel;

public class AccessibleFieldPresenter implements Presenter
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

    private final HandlerManager eventBus;

    private final Display display;

    public AccessibleFieldPresenter(HandlerManager eventBus, Display view)
    {
        this.eventBus = eventBus;
        this.display = view;
        bind();
        RootPanel.get().add(display.asWidget());
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

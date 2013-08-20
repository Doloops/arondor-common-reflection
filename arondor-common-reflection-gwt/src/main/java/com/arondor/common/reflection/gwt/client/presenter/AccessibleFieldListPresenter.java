package com.arondor.common.reflection.gwt.client.presenter;

import java.util.Map;

import com.arondor.common.reflection.gwt.client.view.AccessibleFieldView;
import com.arondor.common.reflection.model.java.AccessibleField;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.RootPanel;

public class AccessibleFieldListPresenter implements Presenter
{

    private Map<String, AccessibleField> accessibleFieldList;

    private Map<String, AccessibleFieldPresenter> accessibleFieldPresenterList;

    public interface Display extends IsWidget
    {

        Map<AccessibleField, AccessibleFieldView> getAccessibleFieldViewList();

        void addAccessibleFieldView(AccessibleField accessibleField, AccessibleFieldView accessibleFieldView);

        FlexTable getFields();
    }

    private final HandlerManager eventBus;

    private final Display display;

    public AccessibleFieldListPresenter(HandlerManager eventBus, Display view)
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

    public void setAccessibleFieldList(Map<String, AccessibleField> accessibleFields)
    {
        this.accessibleFieldList = accessibleFields;
        for (AccessibleField accessibleField : accessibleFields.values())
        {
            AccessibleFieldPresenter fieldPresenter = new AccessibleFieldPresenter(eventBus, new AccessibleFieldView(
                    display.getFields()));
            display.addAccessibleFieldView(accessibleField, (AccessibleFieldView) fieldPresenter.getDisplay());
            display.getAccessibleFieldViewList().get(accessibleField).setName(accessibleField.getName());
            display.getAccessibleFieldViewList().get(accessibleField).setClassName(accessibleField.getClassName());
            display.getAccessibleFieldViewList().get(accessibleField).setDescription(accessibleField.getDescription());
        }
    }

}

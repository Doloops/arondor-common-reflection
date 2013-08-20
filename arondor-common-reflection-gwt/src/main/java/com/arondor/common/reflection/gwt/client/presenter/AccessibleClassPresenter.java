package com.arondor.common.reflection.gwt.client.presenter;

import java.util.HashMap;
import java.util.logging.Logger;

import com.arondor.common.reflection.bean.config.ObjectConfigurationFactoryBean;
import com.arondor.common.reflection.gwt.client.AccessibleClassServiceAsync;
import com.arondor.common.reflection.gwt.client.view.AccessibleFieldListView;
import com.arondor.common.reflection.gwt.client.view.AccessibleFieldView;
import com.arondor.common.reflection.model.config.ElementConfiguration;
import com.arondor.common.reflection.model.config.ObjectConfiguration;
import com.arondor.common.reflection.model.config.ObjectConfigurationFactory;
import com.arondor.common.reflection.model.java.AccessibleClass;
import com.arondor.common.reflection.model.java.AccessibleField;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.RootPanel;

public class AccessibleClassPresenter implements Presenter
{
    private static final Logger LOG = Logger.getLogger(AccessibleClassPresenter.class.getName());

    private AccessibleClass accessibleClass;

    public interface Display extends IsWidget
    {
        void setName(String name);

        void setClassname(String classname);

        HasClickHandlers getSaveButton();

        AccessibleFieldListView getAccessibleFieldListView();

        void setAccessibleFieldListView(AccessibleFieldListView accessibleFieldView);
    }

    private final AccessibleClassServiceAsync rpcService;

    private final HandlerManager eventBus;

    private final Display display;

    private AccessibleFieldListPresenter fieldListPresenter;

    public AccessibleClassPresenter(AccessibleClassServiceAsync rpcService, HandlerManager eventBus, Display view)
    {
        this.rpcService = rpcService;
        this.eventBus = eventBus;
        this.display = view;

        bind();
        RootPanel.get().clear();
        RootPanel.get().add(display.asWidget());
        fetchAccessibleClass();
        fieldListPresenter = new AccessibleFieldListPresenter(eventBus, new AccessibleFieldListView());
        display.setAccessibleFieldListView((AccessibleFieldListView) fieldListPresenter.getDisplay());
    }

    public void bind()
    {
        display.getSaveButton().addClickHandler(new ClickHandler()
        {
            public void onClick(ClickEvent event)
            {
                doSave();
            }
        });
    }

    public void setAccessibleClass(AccessibleClass accessibleClass)
    {
        this.accessibleClass = accessibleClass;
    }

    private void fetchAccessibleClass()
    {
        rpcService.getAccessibleClass("com.arondor.common.reflection.gwt.server.samples.TestClass",
                new AsyncCallback<AccessibleClass>()
                {

                    public void onSuccess(AccessibleClass result)
                    {
                        setAccessibleClass(result);
                        accessibleClass = result;
                        display.setName(result.getName());
                        display.setClassname(result.getClassBaseName());
                        fieldListPresenter.setAccessibleFieldList(result.getAccessibleFields());
                    }

                    public void onFailure(Throwable caught)
                    {
                        Window.alert("Error fetching Accessible Classes");
                    }
                });
    }

    private void doSave()
    {

        ObjectConfigurationFactory objectConfigurationFactory = new ObjectConfigurationFactoryBean();

        ObjectConfiguration configuration = objectConfigurationFactory.createObjectConfiguration();
        configuration.setClassName(accessibleClass.getClassBaseName());

        configuration.setFields(new HashMap<String, ElementConfiguration>());

        for (AccessibleField accessibleField : accessibleClass.getAccessibleFields().values())
        {
            AccessibleFieldView accessibleFieldView = display.getAccessibleFieldListView().getAccessibleFieldViewList()
                    .get(accessibleField);
            String value = accessibleFieldView.getInputValue().getValue();
            configuration.getFields().put(accessibleField.getName(),
                    objectConfigurationFactory.createPrimitiveConfiguration(value));
            LOG.finest(accessibleField.getName() + " : " + value);
        }

    }
}

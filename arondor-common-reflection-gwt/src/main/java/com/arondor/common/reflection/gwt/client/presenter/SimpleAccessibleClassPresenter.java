package com.arondor.common.reflection.gwt.client.presenter;

import java.util.HashMap;
import java.util.logging.Logger;

import com.arondor.common.reflection.gwt.client.api.AccessibleClassPresenter;
import com.arondor.common.reflection.gwt.client.service.GWTReflectionServiceAsync;
import com.arondor.common.reflection.model.config.ElementConfiguration;
import com.arondor.common.reflection.model.config.ObjectConfiguration;
import com.arondor.common.reflection.model.config.ObjectConfigurationFactory;
import com.arondor.common.reflection.model.java.AccessibleClass;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.TreeItem;

public class SimpleAccessibleClassPresenter implements AccessibleClassPresenter
{
    private static final Logger LOG = Logger.getLogger(SimpleAccessibleClassPresenter.class.getName());

    public interface Display extends IsWidget
    {
        void setName(String name);

        void setClassname(String classname);

        AccessibleFieldMapPresenter.Display getFieldMapDisplay();

        ClassTreePresenter.Display getClassTreeDisplay();

        void addImplementation(String implementationClassName);
    }

    private String baseClassName;

    public String getBaseClassName()
    {
        return baseClassName;
    }

    private final GWTReflectionServiceAsync rpcService;

    private final Display display;

    private AccessibleFieldMapPresenter fieldListPresenter;

    private ClassTreePresenter classTreePresenter;

    public SimpleAccessibleClassPresenter(GWTReflectionServiceAsync rpcService, Display view)
    {
        this.rpcService = rpcService;
        this.display = view;

        fieldListPresenter = new AccessibleFieldMapPresenter(display.getFieldMapDisplay());

        bind();
    }

    public void bind()
    {
        if (classTreePresenter != null)
        {
            classTreePresenter.getDisplay().getTree().addSelectionHandler(new SelectionHandler<TreeItem>()
            {

                public void onSelection(SelectionEvent<TreeItem> event)
                {
                    fetchAccessibleClassField(event.getSelectedItem().getText());
                }
            });
        }
    }

    public Display getDisplay()
    {
        return display;
    }

    public void setBaseClassName(String className)
    {
        this.baseClassName = className;
        classTreePresenter = new ClassTreePresenter(baseClassName, rpcService, display.getClassTreeDisplay());
        bind();
        fetchAccessibleClass(className);
    }

    private void fetchAccessibleClass(String className)
    {
        rpcService.getAccessibleClass(className, new AsyncCallback<AccessibleClass>()
        {

            public void onSuccess(AccessibleClass result)
            {
                buildInfo(result);
                fieldListPresenter.setAccessibleFields(result.getAccessibleFields());
                classTreePresenter.getParentNodePresenter().buildTree(result);
            }

            public void onFailure(Throwable caught)
            {
                Window.alert("Error fetching Accessible Class");
            }
        });
    }

    private void fetchAccessibleClassField(String className)
    {
        rpcService.getAccessibleClass(className, new AsyncCallback<AccessibleClass>()
        {

            public void onSuccess(AccessibleClass result)
            {
                buildInfo(result);
                fieldListPresenter.getDisplay().clearList();
                fieldListPresenter.setAccessibleFields(result.getAccessibleFields());
            }

            public void onFailure(Throwable caught)
            {
                Window.alert("Error fetching Accessible Class");
            }
        });
    }

    private void buildInfo(AccessibleClass result)
    {
        display.setName(result.getName());
        display.setClassname(result.getClassBaseName());
    }

    public ObjectConfiguration getObjectConfiguration(ObjectConfigurationFactory objectConfigurationFactory)
    {
        ObjectConfiguration configuration = objectConfigurationFactory.createObjectConfiguration();
        configuration.setClassName(baseClassName);
        configuration.setFields(new HashMap<String, ElementConfiguration>());
        fieldListPresenter.updateObjectConfiguration(objectConfigurationFactory, configuration);
        return configuration;
    }

    public void setObjectConfiguration(ObjectConfiguration objectConfiguration)
    {
        fieldListPresenter.setObjectConfiguration(objectConfiguration);
    }

    public IsWidget getDisplayWidget()
    {
        return getDisplay();
    }

}

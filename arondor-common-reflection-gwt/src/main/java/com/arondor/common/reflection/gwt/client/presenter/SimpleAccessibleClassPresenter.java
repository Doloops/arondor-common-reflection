package com.arondor.common.reflection.gwt.client.presenter;

import java.util.HashMap;

import com.arondor.common.reflection.gwt.client.api.AccessibleClassPresenter;
import com.arondor.common.reflection.gwt.client.service.GWTReflectionServiceAsync;
import com.arondor.common.reflection.model.config.ElementConfiguration;
import com.arondor.common.reflection.model.config.ObjectConfiguration;
import com.arondor.common.reflection.model.config.ObjectConfigurationFactory;
import com.arondor.common.reflection.model.java.AccessibleClass;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;

public class SimpleAccessibleClassPresenter implements AccessibleClassPresenter
{
    public interface Display extends IsWidget
    {
        void setName(String name);

        void setClassname(String classname);

        AccessibleFieldMapPresenter.Display getFieldMapDisplay();
    }

    private String baseClassName;

    public String getBaseClassName()
    {
        return baseClassName;
    }

    private String objectClassName;

    private final GWTReflectionServiceAsync rpcService;

    private final Display display;

    private final AccessibleFieldMapPresenter fieldListPresenter;

    public SimpleAccessibleClassPresenter(GWTReflectionServiceAsync rpcService, Display view)
    {
        this.rpcService = rpcService;
        this.display = view;

        fieldListPresenter = new AccessibleFieldMapPresenter(display.getFieldMapDisplay());
        bind();
    }

    public void bind()
    {
    }

    public Display getDisplay()
    {
        return display;
    }

    public void setBaseClassName(String className)
    {
        this.baseClassName = className;
        this.objectClassName = className;
        fetchAccessibleClass(className);
    }

    private void fetchAccessibleClass(String className)
    {
        rpcService.getAccessibleClass(className, new AsyncCallback<AccessibleClass>()
        {

            public void onSuccess(AccessibleClass result)
            {
                display.setName(result.getName());
                display.setClassname(result.getClassBaseName());
                fieldListPresenter.setAccessibleFields(result.getAccessibleFields());
            }

            public void onFailure(Throwable caught)
            {
                Window.alert("Error fetching Accessible Classes");
            }
        });
    }

    public ObjectConfiguration getObjectConfiguration(ObjectConfigurationFactory objectConfigurationFactory)
    {
        ObjectConfiguration configuration = objectConfigurationFactory.createObjectConfiguration();
        configuration.setClassName(objectClassName);
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

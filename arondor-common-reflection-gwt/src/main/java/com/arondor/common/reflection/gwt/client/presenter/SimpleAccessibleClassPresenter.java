package com.arondor.common.reflection.gwt.client.presenter;

import java.util.Collection;
import java.util.HashMap;
import java.util.logging.Logger;

import com.arondor.common.reflection.bean.config.ObjectConfigurationFactoryBean;
import com.arondor.common.reflection.gwt.client.api.AccessibleClassPresenter;
import com.arondor.common.reflection.gwt.client.service.GWTReflectionServiceAsync;
import com.arondor.common.reflection.model.config.ElementConfiguration;
import com.arondor.common.reflection.model.config.ObjectConfiguration;
import com.arondor.common.reflection.model.config.ObjectConfigurationFactory;
import com.arondor.common.reflection.model.java.AccessibleClass;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;

public class SimpleAccessibleClassPresenter implements AccessibleClassPresenter
{
    private static final Logger LOG = Logger.getLogger(SimpleAccessibleClassPresenter.class.getName());

    private final ObjectConfigurationFactory objectConfigurationFactory = new ObjectConfigurationFactoryBean();

    public interface Display extends IsWidget
    {
        HasClickHandlers getImplListInput();

        String getImplListInputValue();

        void setName(String name);

        void setClassname(String classname);

        AccessibleFieldMapPresenter.Display getFieldMapDisplay();

        ClassTreePresenter.Display getClassTreeDisplay();

        void addImplementation(String implementationClassName);

        void clearImpl();

        HasClickHandlers getSetConfigButton();

        HasClickHandlers getSaveConfigButton();

        void setSelectedImplementation(String implementation);
    }

    private String fieldClassName;

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
            classTreePresenter.addSelectionHandler(new SelectionHandler<ClassTreeNodePresenter>()
            {
                public void onSelection(SelectionEvent<ClassTreeNodePresenter> event)
                {
                    String baseClass = event.getSelectedItem().getBaseClassName();
                    String implClass = event.getSelectedItem().getImplClassName();

                    fetchInfo(baseClass);
                    fetchImplementation(baseClass, implClass);
                    if (implClass != null)
                    {
                        fetchField(implClass);
                    }
                    else
                    {
                        fetchField(baseClass);
                    }
                }
            });
        }
        display.getImplListInput().addClickHandler(new ClickHandler()
        {
            public void onClick(ClickEvent event)
            {
                String inputValue = display.getImplListInputValue();

                if (!inputValue.equals("null"))
                {
                    fetchField(inputValue);

                    ClassTreeNodePresenter findNode = classTreePresenter.getRootNodePresenter()
                            .findNode(fieldClassName);
                    if (findNode != null)
                    {
                        findNode.setImplClassName(inputValue);
                    }
                    else
                    {
                        LOG.warning(inputValue + "Not Found");
                    }
                }
            }
        });

        display.getSetConfigButton().addClickHandler(new ClickHandler()
        {
            public void onClick(ClickEvent event)
            {

                // ObjectConfigurationFactory objectConfigurationFactory = new
                // ObjectConfigurationFactoryBean();
                //
                // ObjectConfiguration objectConfiguration =
                // objectConfigurationFactory.createObjectConfiguration();
                // objectConfiguration.setClassName("com.arondor.common.reflection.gwt.server.samples.TestClass");
                //
                // objectConfiguration.setFields(new HashMap<String,
                // ElementConfiguration>());
                //
                // objectConfiguration.getFields().put("aStringProperty",
                // objectConfigurationFactory.createPrimitiveConfiguration("test"));
                // objectConfiguration.getFields().put("aLongProperty",
                // objectConfigurationFactory.createPrimitiveConfiguration("123"));
                //
                // setObjectConfiguration(objectConfiguration);
            }
        });

        display.getSaveConfigButton().addClickHandler(new ClickHandler()
        {
            public void onClick(ClickEvent event)
            {
                ObjectConfiguration objectConfiguration = getObjectConfiguration(objectConfigurationFactory);
                LOG.info("objectConfiguration=> " + objectConfiguration);
                setObjectConfiguration(objectConfiguration);
            }
        });
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
        fetchInfo(className);
        fetchTree(className);
        fetchField(className);
    }

    private void fetchInfo(String className)
    {
        rpcService.getAccessibleClass(className, new AsyncCallback<AccessibleClass>()
        {

            public void onSuccess(AccessibleClass result)
            {
                baseClassName = result.getName();
                fieldClassName = result.getClassBaseName();
                display.setName(baseClassName);
                display.setClassname(fieldClassName);
            }

            public void onFailure(Throwable caught)
            {
                Window.alert("Error fetching Accessible Class");
            }
        });
    }

    private void fetchTree(String className)
    {
        rpcService.getAccessibleClass(className, new AsyncCallback<AccessibleClass>()
        {

            public void onSuccess(AccessibleClass result)
            {
                classTreePresenter.getRootNodePresenter().buildTree(result);
            }

            public void onFailure(Throwable caught)
            {
                Window.alert("Error fetching Accessible Class");
            }
        });
    }

    private void fetchField(String className)
    {
        rpcService.getAccessibleClass(className, new AsyncCallback<AccessibleClass>()
        {

            public void onSuccess(AccessibleClass result)
            {
                fieldListPresenter.getDisplay().clearList();
                fieldListPresenter.setAccessibleFields(result.getAccessibleFields());
            }

            public void onFailure(Throwable caught)
            {
                // Window.alert("Error fetching Accessible Class");
            }
        });
    }

    private void fetchImplementation(final String name, final String implClass)
    {
        rpcService.getImplementingAccessibleClasses(name, new AsyncCallback<Collection<AccessibleClass>>()
        {

            @Override
            public void onSuccess(Collection<AccessibleClass> result)
            {
                display.clearImpl();
                display.addImplementation("null");
                for (AccessibleClass accessibleClass : result)
                {
                    display.addImplementation(accessibleClass.getName());
                }
                display.setSelectedImplementation(implClass);
            }

            @Override
            public void onFailure(Throwable caught)
            {
                LOG.warning("Failed to fetch Implementing Class");
            }
        });
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

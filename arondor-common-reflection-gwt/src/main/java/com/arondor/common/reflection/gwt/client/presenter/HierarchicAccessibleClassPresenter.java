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

public class HierarchicAccessibleClassPresenter implements AccessibleClassPresenter
{
    private static final Logger LOG = Logger.getLogger(HierarchicAccessibleClassPresenter.class.getName());

    public interface Display extends IsWidget
    {

        ClassTreePresenter.Display getClassTreeDisplay();

        void setName(String name);

        void setClassname(String classname);

        HasClickHandlers getSetConfigButton();

        HasClickHandlers getSaveConfigButton();

        HasClickHandlers getImplListInput();

        String getImplListInputValue();

        void addImplementation(String implementationClassName);

        void setSelectedImplementation(String implementation);

        void clearImpl();

        void setFieldMapDisplay(AccessibleFieldMapPresenter.Display fieldMapDisplay);
    }

    private String fieldClassName;

    private String baseClassName;

    private final GWTReflectionServiceAsync rpcService;

    private final Display display;

    private AccessibleFieldMapPresenter fieldsPresenter;

    private ClassTreePresenter classTreePresenter;

    public HierarchicAccessibleClassPresenter(GWTReflectionServiceAsync rpcService, Display view)
    {
        this.rpcService = rpcService;
        this.display = view;

        // bind();
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
                    if (implClass == null)
                    {
                        implClass = baseClass;
                    }

                    fetchInfo(baseClass);
                    fetchImplementation(baseClass, implClass);
                    fetchField(baseClass);
                }
            });
        }

        display.getImplListInput().addClickHandler(new ClickHandler()
        {
            public void onClick(ClickEvent event)
            {
                String inputValue = display.getImplListInputValue();

                ClassTreeNodePresenter findNode = classTreePresenter.findNode(baseClassName);
                if (findNode != null)
                {
                    if (findNode == classTreePresenter.getRootNodePresenter())
                    {
                        fetchField(baseClassName);
                    }
                    else if (!inputValue.equals("null"))
                    {
                        fetchField(inputValue);
                        findNode.setImplClassName(inputValue);
                    }
                    else
                    {
                        fetchField(baseClassName);
                        findNode.setImplClassName(null);
                    }
                }
                else
                {
                    LOG.warning(baseClassName + " Not Found in the tree");
                }
            }
        });

        display.getSetConfigButton().addClickHandler(new ClickHandler()
        {
            public void onClick(ClickEvent event)
            {

                ObjectConfigurationFactory objectConfigurationFactory = new ObjectConfigurationFactoryBean();

                ObjectConfiguration objectConfiguration = objectConfigurationFactory.createObjectConfiguration();
                objectConfiguration.setClassName("com.arondor.common.reflection.gwt.server.samples.TestClass");

                objectConfiguration.setFields(new HashMap<String, ElementConfiguration>());

                objectConfiguration.getFields().put("aStringProperty",
                        objectConfigurationFactory.createPrimitiveConfiguration("test"));
                objectConfiguration.getFields().put("aLongProperty",
                        objectConfigurationFactory.createPrimitiveConfiguration("123"));

                setObjectConfiguration(objectConfiguration);
            }
        });

        display.getSaveConfigButton().addClickHandler(new ClickHandler()
        {
            public void onClick(ClickEvent event)
            {
                ObjectConfiguration objectConfiguration = getObjectConfiguration();
                LOG.info("" + objectConfiguration);
                if (classTreePresenter != null)
                {
                    classTreePresenter.findNode(objectConfiguration.getClassName()).setObjectConfiguration(
                            objectConfiguration);
                }
                // setObjectConfiguration(objectConfiguration);
            }
        });
    }

    public ObjectConfiguration getObjectConfiguration()
    {
        ObjectConfigurationFactory objectConfigurationFactory = new ObjectConfigurationFactoryBean();
        ObjectConfiguration configuration = objectConfigurationFactory.createObjectConfiguration();
        ClassTreeNodePresenter nodePresenter = classTreePresenter.getRootNodePresenter().findNode(baseClassName);

        String className = baseClassName;
        if (nodePresenter != null)
        {
            if (nodePresenter.getImplClassName() != null)
            {
                className = nodePresenter.getImplClassName();
            }
            configuration.setClassName(className);
        }
        configuration.setClassName(className);
        configuration.setFields(new HashMap<String, ElementConfiguration>());
        fieldsPresenter.saveObjectConfiguration(objectConfigurationFactory, configuration);
        return configuration;
    }

    public void setObjectConfiguration(ObjectConfiguration objectConfiguration)
    {
        fieldsPresenter.setObjectConfiguration(objectConfiguration);
    }

    public String getBaseClassName()
    {
        return baseClassName;
    }

    public void setBaseClassName(String className)
    {
        this.baseClassName = className;
        classTreePresenter = new ClassTreePresenter(rpcService, baseClassName, display.getClassTreeDisplay());
        bind();
        fetchInfo(className);
        fetchImplementation(className, null);
        fetchTree(className);
        fetchField(className);
    }

    public Display getDisplay()
    {
        return display;
    }

    public IsWidget getDisplayWidget()
    {
        return getDisplay();
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

    private void fetchImplementation(final String name, final String implClass)
    {
        display.clearImpl();
        if (classTreePresenter.getRootNodePresenter().getBaseClassName().equals(name))
        {
            display.addImplementation(name);
            return;
        }
        rpcService.getImplementingAccessibleClasses(name, new AsyncCallback<Collection<AccessibleClass>>()
        {
            public void onSuccess(Collection<AccessibleClass> result)
            {

                display.addImplementation("null");
                for (AccessibleClass accessibleClass : result)
                {
                    display.addImplementation(accessibleClass.getName());
                }
                display.setSelectedImplementation(implClass);
            }

            public void onFailure(Throwable caught)
            {
                LOG.warning("Failed to fetch Implementing Class");
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

    private void fetchField(final String className)
    {
        LOG.finest("fetch " + className);
        rpcService.getAccessibleClass(className, new AsyncCallback<AccessibleClass>()
        {

            public void onSuccess(AccessibleClass result)
            {
                ClassTreeNodePresenter nodePresenter = classTreePresenter.findNode(className);
                if (nodePresenter != null)
                {
                    fieldsPresenter = classTreePresenter.findNode(className).getFieldsPresenter();
                    fieldsPresenter.setAccessibleFields(result.getAccessibleFields());
                    fieldsPresenter.setClassName(className);
                    display.setFieldMapDisplay(fieldsPresenter.getDisplay());
                }
                else
                    LOG.finest("null " + className);
            }

            public void onFailure(Throwable caught)
            {
                Window.alert("Error fetching Accessible Class");
            }
        });
    }

}

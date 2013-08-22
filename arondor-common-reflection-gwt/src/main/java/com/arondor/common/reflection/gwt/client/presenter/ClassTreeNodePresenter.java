package com.arondor.common.reflection.gwt.client.presenter;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import com.arondor.common.reflection.gwt.client.service.GWTReflectionServiceAsync;
import com.arondor.common.reflection.model.config.ElementConfiguration;
import com.arondor.common.reflection.model.config.ObjectConfiguration;
import com.arondor.common.reflection.model.config.ObjectConfigurationFactory;
import com.arondor.common.reflection.model.java.AccessibleClass;
import com.arondor.common.reflection.model.java.AccessibleField;
import com.arondor.common.reflection.util.PrimitiveTypeUtil;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class ClassTreeNodePresenter
{
    private static final Logger LOG = Logger.getLogger(ClassTreeNodePresenter.class.getName());

    public interface Display
    {
        AccessibleFieldMapPresenter.Display getFieldMapDisplay();

        ImplementingClassPresenter.Display getImplementingClassDisplay();

        Display createChild();

        void setNodeName(String name);

        ClassTreeNodePresenter getClassTreeNodePresenter();

        void setClassTreeNodePresenter(ClassTreeNodePresenter classTreeNodePresenter);

        void clear();
    }

    private Map<String, ClassTreeNodePresenter> classTreeNodePresenterMap;

    private final Display display;

    private final String fieldName;

    private AccessibleFieldMapPresenter fieldsPresenter;

    private final ImplementingClassPresenter implementingClassPresenter;

    private final GWTReflectionServiceAsync rpcService;

    public ClassTreeNodePresenter(GWTReflectionServiceAsync rpcService, String baseClassName, Display view)
    {
        this(rpcService, null, baseClassName, view);
    }

    private ClassTreeNodePresenter(GWTReflectionServiceAsync rpcService, String fieldName, String baseClassName,
            Display view)
    {
        this.fieldName = fieldName;
        this.rpcService = rpcService;
        this.display = view;
        this.display.setClassTreeNodePresenter(this);

        LOG.finest("At node : fieldName=" + fieldName);

        classTreeNodePresenterMap = new HashMap<String, ClassTreeNodePresenter>();

        implementingClassPresenter = new ImplementingClassPresenter(rpcService, baseClassName,
                display.getImplementingClassDisplay());
        fieldsPresenter = new AccessibleFieldMapPresenter(display.getFieldMapDisplay());

        bind();

        if (fieldName != null)
        {
            display.setNodeName(fieldName);
        }
        else
        {
            /**
             * We are supposed to be root here
             */
            display.setNodeName("Root :" + baseClassName);
        }
    }

    private void bind()
    {
        implementingClassPresenter.addValueChangeHandler(new ValueChangeHandler<AccessibleClass>()
        {
            public void onValueChange(ValueChangeEvent<AccessibleClass> event)
            {
                updateAccessibleClass(event.getValue());
            }

        });
    }

    private void updateAccessibleClass(AccessibleClass accessibleClass)
    {
        updateAccessibleClass(accessibleClass, null);
    }

    private void updateAccessibleClass(AccessibleClass accessibleClass, ObjectConfiguration objectConfiguration)
    {
        LOG.finest("updateAccessibleClass(class=" + accessibleClass + ", objectConfiguration=" + objectConfiguration
                + ")");
        classTreeNodePresenterMap.clear();
        fieldsPresenter.clear();
        display.clear();

        if (accessibleClass != null)
        {
            buildTree(accessibleClass);
            fieldsPresenter.setAccessibleFields(accessibleClass.getAccessibleFields());

            if (objectConfiguration != null)
            {
                for (Map.Entry<String, ElementConfiguration> fieldEntry : objectConfiguration.getFields().entrySet())
                {
                    ClassTreeNodePresenter nodePresenter = classTreeNodePresenterMap.get(fieldEntry.getKey());
                    if (nodePresenter != null)
                    {
                        if (fieldEntry.getValue() instanceof ObjectConfiguration)
                        {
                            nodePresenter.setObjectConfiguration((ObjectConfiguration) fieldEntry.getValue());
                        }
                        else
                        {
                        }
                    }
                    fieldsPresenter.setObjectConfiguration(objectConfiguration);
                }
            }
        }
    }

    private void buildTree(AccessibleClass accessibleClass)
    {
        for (AccessibleField accessibleField : accessibleClass.getAccessibleFields().values())
        {
            String fieldName = accessibleField.getName();
            String fieldClassName = accessibleField.getClassName();
            if (!PrimitiveTypeUtil.isPrimitiveType(fieldClassName))
            {
                ClassTreeNodePresenter childPresenter = new ClassTreeNodePresenter(rpcService, fieldName,
                        fieldClassName, display.createChild());
                classTreeNodePresenterMap.put(fieldName, childPresenter);
            }
        }
    }

    public ClassTreeNodePresenter findNode(String classType)
    {
        for (ClassTreeNodePresenter classTreeNodePresenter : classTreeNodePresenterMap.values())
        {
            if (classTreeNodePresenter.getBaseClassName().equals(classType))
            {
                return classTreeNodePresenter;
            }
            else
            {
                ClassTreeNodePresenter result = classTreeNodePresenter.findNode(classType);
                if (result != null)
                {
                    return result;
                }
            }
        }
        return null;

    }

    public String getFieldName()
    {
        return fieldName;
    }

    public String getBaseClassName()
    {
        return implementingClassPresenter.getBaseClassName();
    }

    public String getImplementClassName()
    {
        return implementingClassPresenter.getImplementClassName();
    }

    public Display getDisplay()
    {
        return display;
    }

    public ObjectConfiguration getObjectConfiguration(ObjectConfigurationFactory objectConfigurationFactory)
    {
        if (implementingClassPresenter.getImplementClassName() == null)
        {
            return null;
        }
        ObjectConfiguration objectConfiguration = objectConfigurationFactory.createObjectConfiguration();
        objectConfiguration.setFields(new HashMap<String, ElementConfiguration>());
        objectConfiguration.setClassName(implementingClassPresenter.getImplementClassName());

        updateChildObjectConfigurations(objectConfigurationFactory, objectConfiguration);

        fieldsPresenter.updateFieldsConfiguration(objectConfigurationFactory, objectConfiguration);

        return objectConfiguration;
    }

    private void updateChildObjectConfigurations(ObjectConfigurationFactory objectConfigurationFactory,
            ObjectConfiguration objectConfiguration)
    {
        for (Map.Entry<String, ClassTreeNodePresenter> presentersEntry : classTreeNodePresenterMap.entrySet())
        {
            ObjectConfiguration childConfiguration = presentersEntry.getValue().getObjectConfiguration(
                    objectConfigurationFactory);
            if (childConfiguration != null)
            {
                objectConfiguration.getFields().put(presentersEntry.getKey(), childConfiguration);
            }
        }
    }

    public void setObjectConfiguration(final ObjectConfiguration objectConfiguration)
    {
        LOG.finest("setObjectConfiguration :" + objectConfiguration);
        implementingClassPresenter.setImplementClassName(objectConfiguration.getClassName(),
                new AsyncCallback<AccessibleClass>()
                {
                    public void onFailure(Throwable caught)
                    {
                    }

                    public void onSuccess(AccessibleClass result)
                    {
                        updateAccessibleClass(result, objectConfiguration);
                    }
                });
    }

    public AccessibleFieldMapPresenter getFieldsPresenter()
    {
        return fieldsPresenter;
    }

    public void setAccessibleFieldMapPresenter(AccessibleFieldMapPresenter fieldsPresenter)
    {
        this.fieldsPresenter = fieldsPresenter;
    }
}

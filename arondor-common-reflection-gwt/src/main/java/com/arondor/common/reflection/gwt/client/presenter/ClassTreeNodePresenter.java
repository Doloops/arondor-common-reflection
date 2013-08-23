package com.arondor.common.reflection.gwt.client.presenter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
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

public class ClassTreeNodePresenter implements TreeNodePresenter
{
    private static final Logger LOG = Logger.getLogger(ClassTreeNodePresenter.class.getName());

    public interface ClassDisplay extends TreeNodePresenter.Display
    {
        ImplementingClassPresenter.Display getImplementingClassDisplay();

        ClassDisplay createClassChild();

        PrimitiveTreeNodePresenter.PrimitiveDisplay createPrimitiveChild(String fieldClassName);

        StringListTreeNodePresenter.StringListDisplay createStringListChild();
    }

    private final Map<String, TreeNodePresenter> classTreeNodePresenterMap = new HashMap<String, TreeNodePresenter>();

    private final ClassDisplay display;

    private final String fieldName;

    private final ImplementingClassPresenter implementingClassPresenter;

    private final GWTReflectionServiceAsync rpcService;

    public ClassTreeNodePresenter(GWTReflectionServiceAsync rpcService, String baseClassName, ClassDisplay view)
    {
        this(rpcService, null, baseClassName, view);
    }

    private ClassTreeNodePresenter(GWTReflectionServiceAsync rpcService, String fieldName, String baseClassName,
            ClassDisplay view)
    {
        this.fieldName = fieldName;
        this.rpcService = rpcService;
        this.display = view;

        LOG.finest("At node : fieldName=" + fieldName);

        implementingClassPresenter = new ImplementingClassPresenter(rpcService, baseClassName,
                display.getImplementingClassDisplay());
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
            display.setNodeName(baseClassName);
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
        display.clear();

        if (accessibleClass != null)
        {
            buildTree(accessibleClass);

            if (objectConfiguration != null)
            {
                for (Map.Entry<String, ElementConfiguration> fieldEntry : objectConfiguration.getFields().entrySet())
                {
                    TreeNodePresenter nodePresenter = classTreeNodePresenterMap.get(fieldEntry.getKey());
                    if (nodePresenter != null)
                    {
                        nodePresenter.setElementConfiguration(fieldEntry.getValue());
                    }
                }
            }
        }
    }

    private static class AccessibleFieldComparator implements Comparator<AccessibleField>
    {
        public int compare(AccessibleField o1, AccessibleField o2)
        {
            boolean prim1 = PrimitiveTypeUtil.isPrimitiveType(o1.getClassName())
                    || o1.getClassName().equals("java.util.List");
            boolean prim2 = PrimitiveTypeUtil.isPrimitiveType(o2.getClassName())
                    || o2.getClassName().equals("java.util.List");
            if (prim1 && !prim2)
            {
                return 1;
            }
            if (!prim1 && prim2)
            {
                return -1;
            }
            return o1.getName().compareTo(o2.getName());
        }

    }

    private void buildTree(AccessibleClass accessibleClass)
    {
        List<AccessibleField> sortedAccessibleFields = new ArrayList<AccessibleField>();
        sortedAccessibleFields.addAll(accessibleClass.getAccessibleFields().values());
        Collections.sort(sortedAccessibleFields, new AccessibleFieldComparator());

        for (AccessibleField accessibleField : sortedAccessibleFields)
        {
            String fieldName = accessibleField.getName();
            String fieldClassName = accessibleField.getClassName();

            TreeNodePresenter childPresenter = null;
            if (PrimitiveTypeUtil.isPrimitiveType(fieldClassName))
            {
                childPresenter = new PrimitiveTreeNodePresenter(fieldName, display.createPrimitiveChild(fieldClassName));
            }
            else if (fieldClassName.equals("java.util.List"))
            {
                childPresenter = new StringListTreeNodePresenter(fieldName, display.createStringListChild());
            }
            else
            {
                childPresenter = new ClassTreeNodePresenter(rpcService, fieldName, fieldClassName,
                        display.createClassChild());
            }
            childPresenter.getDisplay().setNodeDescription(accessibleField.getDescription());
            classTreeNodePresenterMap.put(fieldName, childPresenter);
        }
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

    public ClassDisplay getDisplay()
    {
        return display;
    }

    public ElementConfiguration getElementConfiguration(ObjectConfigurationFactory objectConfigurationFactory)
    {
        if (implementingClassPresenter.getImplementClassName() == null)
        {
            return null;
        }
        if (PrimitiveTypeUtil.isPrimitiveType(implementingClassPresenter.getImplementClassName()))
        {
            throw new RuntimeException("NOT IMPLEMENTED YET");
        }
        else
        {
            ObjectConfiguration objectConfiguration = objectConfigurationFactory.createObjectConfiguration();
            objectConfiguration.setFields(new HashMap<String, ElementConfiguration>());
            objectConfiguration.setClassName(implementingClassPresenter.getImplementClassName());

            updateChildObjectConfigurations(objectConfigurationFactory, objectConfiguration);
            return objectConfiguration;
        }

        // fieldsPresenter.updateFieldsConfiguration(objectConfigurationFactory,
        // objectConfiguration);

    }

    private void updateChildObjectConfigurations(ObjectConfigurationFactory objectConfigurationFactory,
            ObjectConfiguration objectConfiguration)
    {
        for (Map.Entry<String, TreeNodePresenter> presentersEntry : classTreeNodePresenterMap.entrySet())
        {
            ElementConfiguration childConfiguration = presentersEntry.getValue().getElementConfiguration(
                    objectConfigurationFactory);
            if (childConfiguration != null)
            {
                objectConfiguration.getFields().put(presentersEntry.getKey(), childConfiguration);
            }
        }
    }

    public void setElementConfiguration(ElementConfiguration elementConfiguration)
    {
        LOG.finest("setElementConfiguration :" + elementConfiguration);
        if (elementConfiguration instanceof ObjectConfiguration)
        {
            final ObjectConfiguration objectConfiguration = (ObjectConfiguration) elementConfiguration;
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
    }
    //
    // public AccessibleFieldMapPresenter getFieldsPresenter()
    // {
    // return fieldsPresenter;
    // }
    //
    // public void setAccessibleFieldMapPresenter(AccessibleFieldMapPresenter
    // fieldsPresenter)
    // {
    // this.fieldsPresenter = fieldsPresenter;
    // }
}

package com.arondor.common.reflection.gwt.client.presenter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.arondor.common.reflection.gwt.client.event.TreeNodeClearEvent;
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

    public interface ClassDisplay extends TreeNodePresenter.ChildCreatorDisplay
    {
        ImplementingClassPresenter.Display getImplementingClassDisplay();

    }

    private final Map<String, TreeNodePresenter> classTreeNodePresenterMap = new HashMap<String, TreeNodePresenter>();

    private final ClassDisplay display;

    private final String fieldName;

    private final ImplementingClassPresenter implementingClassPresenter;

    private final GWTReflectionServiceAsync rpcService;

    public ClassTreeNodePresenter(GWTReflectionServiceAsync rpcService, String baseClassName, ClassDisplay view)
    {
        this(rpcService, null, baseClassName, view);
        display.setNodeName(baseClassName);
    }

    protected ClassTreeNodePresenter(GWTReflectionServiceAsync rpcService, String fieldName, String baseClassName,
            ClassDisplay view)
    {
        this.fieldName = fieldName;
        this.rpcService = rpcService;
        this.display = view;

        LOG.finest("Create new TreeNodePresenter for fieldName=" + fieldName + ", baseClassName=" + baseClassName);

        implementingClassPresenter = new ImplementingClassPresenter(rpcService, baseClassName,
                display.getImplementingClassDisplay());
        bind();
    }

    private void bind()
    {
        implementingClassPresenter.addValueChangeHandler(new ValueChangeHandler<String>()
        {
            public void onValueChange(ValueChangeEvent<String> event)
            {
                updateAccessibleClass(event.getValue(), null);
            }
        });

        display.addTreeNodeClearHandler(new TreeNodeClearEvent.Handler()
        {
            public void onTreeNodeClearEvent(TreeNodeClearEvent treeNodeClearEvent)
            {
                implementingClassPresenter.setImplementClassName(null);
                classTreeNodePresenterMap.clear();
            }
        });
    }

    private void updateAccessibleClass(String className, final ObjectConfiguration objectConfiguration)
    {
        rpcService.getAccessibleClass(className, new AsyncCallback<AccessibleClass>()
        {
            public void onSuccess(AccessibleClass result)
            {
                updateAccessibleClass(result, objectConfiguration);
            }

            public void onFailure(Throwable caught)
            {
            }
        });
    }

    private void updateAccessibleClass(AccessibleClass accessibleClass, ObjectConfiguration objectConfiguration)
    {
        LOG.finest("field=" + fieldName + ", updateAccessibleClass(class="
                + (accessibleClass != null ? accessibleClass.getName() : null) + ", objectConfiguration="
                + objectConfiguration + ")");

        classTreeNodePresenterMap.clear();
        display.clear();

        if (accessibleClass != null)
        {
            display.setActive(true);
            buildTree(accessibleClass);

            if (objectConfiguration != null)
            {
                implementingClassPresenter.setImplementClassName(objectConfiguration.getClassName());
                if (objectConfiguration.getFields() != null)
                {
                    for (Map.Entry<String, ElementConfiguration> fieldEntry : objectConfiguration.getFields()
                            .entrySet())
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
        else
        {
            display.setActive(false);
        }
    }

    private void buildTree(AccessibleClass accessibleClass)
    {
        List<AccessibleField> sortedAccessibleFields = new ArrayList<AccessibleField>();
        sortedAccessibleFields.addAll(accessibleClass.getAccessibleFields().values());
        Collections.sort(sortedAccessibleFields, new AccessibleFieldComparator());

        LOG.finest("Build tree for accessibleClass=" + accessibleClass.getName());

        for (AccessibleField accessibleField : sortedAccessibleFields)
        {
            TreeNodePresenter childPresenter = TreeNodePresenterFactory.getInstance().createChildNodePresenter(
                    rpcService, display, accessibleField);
            LOG.finest("At field=" + accessibleField.getName() + ", created childPresenter=" + childPresenter);
            classTreeNodePresenterMap.put(accessibleField.getName(), childPresenter);
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
            updateAccessibleClass(objectConfiguration.getClassName(), objectConfiguration);
        }
    }

}

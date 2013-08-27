package com.arondor.common.reflection.gwt.client.presenter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.arondor.common.reflection.gwt.client.presenter.fields.PrimitiveTreeNodePresenter;
import com.arondor.common.reflection.gwt.client.presenter.fields.StringListTreeNodePresenter;
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

    private static final int MAX_DESCRIPTION_LENGTH = 60;

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
        display.setNodeName(baseClassName);
    }

    private ClassTreeNodePresenter(GWTReflectionServiceAsync rpcService, String fieldName, String baseClassName,
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
            String fieldName = accessibleField.getName();
            String fieldClassName = accessibleField.getClassName();

            TreeNodePresenter childPresenter = null;
            if (PrimitiveTypeUtil.isPrimitiveType(fieldClassName))
            {
                childPresenter = new PrimitiveTreeNodePresenter(fieldName, display.createPrimitiveChild(fieldClassName));
            }
            else if (isStringListField(accessibleField))
            {
                childPresenter = new StringListTreeNodePresenter(fieldName, display.createStringListChild());
            }
            else
            {
                childPresenter = new ClassTreeNodePresenter(rpcService, fieldName, fieldClassName,
                        display.createClassChild());
            }

            String fieldDescription = accessibleField.getDescription();
            String nodeName;
            String nodeDescription = "";
            if (fieldDescription != null)
            {
                if (fieldDescription.length() >= MAX_DESCRIPTION_LENGTH)
                {
                    nodeName = fieldDescription.substring(0, MAX_DESCRIPTION_LENGTH) + "...";
                    nodeDescription = fieldDescription;
                }
                else
                {
                    nodeName = fieldDescription;
                }
            }
            else
            {
                nodeName = fieldName;
            }
            nodeDescription += " (" + fieldName + " : " + fieldClassName + ")";
            childPresenter.getDisplay().setNodeDescription(nodeDescription);
            childPresenter.getDisplay().setNodeName(nodeName);
            classTreeNodePresenterMap.put(fieldName, childPresenter);
        }
    }

    private boolean isStringListField(AccessibleField accessibleField)
    {
        String fieldClassName = accessibleField.getClassName();
        return fieldClassName.equals("java.util.List") && accessibleField.getGenericParameterClassList() != null
                && accessibleField.getGenericParameterClassList().size() == 1
                && accessibleField.getGenericParameterClassList().get(0).equals("java.lang.String");
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

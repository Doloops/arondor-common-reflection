package com.arondor.common.reflection.gwt.client.presenter;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import com.arondor.common.reflection.gwt.client.service.GWTReflectionServiceAsync;
import com.arondor.common.reflection.model.config.ObjectConfiguration;
import com.arondor.common.reflection.model.java.AccessibleClass;
import com.arondor.common.reflection.model.java.AccessibleField;
import com.arondor.common.reflection.util.PrimitiveTypeUtil;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class ClassTreeNodePresenter
{
    @SuppressWarnings("unused")
    private static final Logger LOG = Logger.getLogger(ClassTreeNodePresenter.class.getName());

    public interface Display
    {
        AccessibleFieldMapPresenter.Display getFieldMapDisplay();

        Display createChild();

        void setNodeName(String name);

        ClassTreeNodePresenter getClassTreeNodePresenter();

        void setClassTreeNodePresenter(ClassTreeNodePresenter classTreeNodePresenter);
    }

    private Map<String, ClassTreeNodePresenter> classTreeNodePresenterMap;

    private final Display display;

    private final String baseClassName;

    private String implClassName;

    private final String fieldName;

    private ObjectConfiguration objectConfiguration;

    private AccessibleFieldMapPresenter fieldsPresenter;

    private final GWTReflectionServiceAsync rpcService;

    public ClassTreeNodePresenter(GWTReflectionServiceAsync rpcService, String fieldName, String baseClassName,
            Display view)
    {
        this.fieldName = fieldName;
        this.baseClassName = baseClassName;
        this.rpcService = rpcService;
        this.display = view;

        this.display.setClassTreeNodePresenter(this);

        classTreeNodePresenterMap = new HashMap<String, ClassTreeNodePresenter>();

        fieldsPresenter = new AccessibleFieldMapPresenter(display.getFieldMapDisplay());

        bind();

        String title = "";
        if (fieldName != null)
        {
            title += fieldName + " : " + implClassName;
        }
        else
        {
            title += baseClassName;
        }
        display.setNodeName(title);
    }

    private void bind()
    {
    }

    private void fetchAccessibleClass(String className)
    {
        rpcService.getAccessibleClass(className, new AsyncCallback<AccessibleClass>()
        {

            public void onSuccess(AccessibleClass result)
            {
                buildTree(result);
            }

            public void onFailure(Throwable caught)
            {
                Window.alert("Error fetching Accessible Class");
            }
        });
    }

    public void addClassTreePresenter(String accessibleClassName)
    {
        fetchAccessibleClass(accessibleClassName);
    }

    public void buildTree(AccessibleClass accessibleClass)
    {
        for (AccessibleField accessibleField : accessibleClass.getAccessibleFields().values())
        {
            String fieldName = accessibleField.getName();
            String fieldClassName = accessibleField.getClassName();
            if (!PrimitiveTypeUtil.isPrimitiveType(fieldClassName))
            {
                ClassTreeNodePresenter childPresenter = new ClassTreeNodePresenter(rpcService, fieldName,
                        fieldClassName, display.createChild());
                classTreeNodePresenterMap.put(fieldClassName, childPresenter);
                childPresenter.addClassTreePresenter(fieldClassName);
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
        return baseClassName;
    }

    public String getImplClassName()
    {
        return implClassName;
    }

    public void setImplClassName(String implClassName)
    {
        this.implClassName = implClassName;
        display.setNodeName(fieldName + " : " + implClassName);
    }

    public Display getDisplay()
    {
        return display;
    }

    public ObjectConfiguration getObjectConfiguration()
    {
        return objectConfiguration;
    }

    public void setObjectConfiguration(ObjectConfiguration objectConfiguration)
    {
        this.objectConfiguration = objectConfiguration;
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

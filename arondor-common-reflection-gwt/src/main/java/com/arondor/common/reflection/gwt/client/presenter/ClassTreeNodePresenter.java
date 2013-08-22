package com.arondor.common.reflection.gwt.client.presenter;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import com.arondor.common.reflection.gwt.client.service.GWTReflectionServiceAsync;
import com.arondor.common.reflection.model.java.AccessibleClass;
import com.arondor.common.reflection.model.java.AccessibleField;
import com.arondor.common.reflection.util.PrimitiveTypeUtil;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class ClassTreeNodePresenter
{

    private static final Logger LOG = Logger.getLogger(ClassTreeNodePresenter.class.getName());

    public interface Display
    {
        Display createChild();

        void setNodeName(String name);

        void setClassTreeNodePresenter(ClassTreeNodePresenter classTreeNodePresenter);

        ClassTreeNodePresenter getClassTreeNodePresenter();
    }

    private Map<String, ClassTreeNodePresenter> classTreeNodePresenterList;

    private final Display display;

    private final String baseClassName;

    private String implClassName;

    public String getImplClassName()
    {
        return implClassName;
    }

    public void setImplClassName(String implClassName)
    {
        this.implClassName = implClassName;
        display.setNodeName(fieldName + " : " + implClassName);
    }

    private final String fieldName;

    public String getFieldName()
    {
        return fieldName;
    }

    public String getBaseClassName()
    {
        return baseClassName;
    }

    private final GWTReflectionServiceAsync rpcService;

    public ClassTreeNodePresenter(GWTReflectionServiceAsync rpcService, String fieldName, String baseClassName,
            Display view)
    {
        this.fieldName = fieldName;
        this.baseClassName = baseClassName;
        this.rpcService = rpcService;
        this.display = view;

        this.display.setClassTreeNodePresenter(this);

        classTreeNodePresenterList = new HashMap<String, ClassTreeNodePresenter>();

        bind();

        String title = "";
        if (fieldName != null)
        {
            title += fieldName + " : ";
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

    public Display getDisplay()
    {
        return display;
    }

    public void addClassTreePresenter(String accessibleClassName)
    {
        fetchAccessibleClass(accessibleClassName);
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
                classTreeNodePresenterList.put(fieldClassName, childPresenter);
                childPresenter.addClassTreePresenter(fieldClassName);
            }
        }
    }

    public ClassTreeNodePresenter findNode(String classType)
    {
        for (ClassTreeNodePresenter classTreeNodePresenter : classTreeNodePresenterList.values())
        {
            String[] split = classTreeNodePresenter.getBaseClassName().split("\\.");
            String thisClassType = split[split.length - 1];
            if (thisClassType.equals(classType))
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
}

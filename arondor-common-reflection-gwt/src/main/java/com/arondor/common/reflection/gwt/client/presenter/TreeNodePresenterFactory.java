package com.arondor.common.reflection.gwt.client.presenter;

import java.util.List;

import com.arondor.common.reflection.gwt.client.presenter.fields.ListTreeNodePresenter;
import com.arondor.common.reflection.gwt.client.presenter.fields.MapTreeNodePresenter;
import com.arondor.common.reflection.gwt.client.presenter.fields.PrimitiveTreeNodePresenter;
import com.arondor.common.reflection.gwt.client.presenter.fields.StringListTreeNodePresenter;
import com.arondor.common.reflection.gwt.client.service.GWTReflectionServiceAsync;
import com.arondor.common.reflection.model.config.ObjectConfigurationMap;
import com.arondor.common.reflection.model.java.AccessibleField;
import com.arondor.common.reflection.util.PrimitiveTypeUtil;

public class TreeNodePresenterFactory
{
    private static final int MAX_DESCRIPTION_LENGTH = 60;

    public TreeNodePresenter createChildNodePresenter(GWTReflectionServiceAsync rpcService,
            ObjectConfigurationMap objectConfigurationMap, TreeNodePresenter.ChildCreatorDisplay display,
            AccessibleField accessibleField)
    {
        String fieldName = accessibleField.getName();
        String fieldClassName = accessibleField.getClassName();
        return createChildNodePresenter(rpcService, objectConfigurationMap, display, fieldName, fieldClassName,
                accessibleField.getDescription(), accessibleField.getGenericParameterClassList());
    }

    public TreeNodePresenter createChildNodePresenter(GWTReflectionServiceAsync rpcService,
            ObjectConfigurationMap objectConfigurationMap, TreeNodePresenter.ChildCreatorDisplay display,
            String fieldName, String fieldClassName, String fieldDescription, List<String> genericTypes)
    {
        TreeNodePresenter childPresenter = null;
        if (PrimitiveTypeUtil.isPrimitiveType(fieldClassName))
        {
            childPresenter = new PrimitiveTreeNodePresenter(fieldName, display.createPrimitiveChild(fieldClassName));
        }
        else if (isStringListField(fieldClassName, genericTypes))
        {
            childPresenter = new StringListTreeNodePresenter(fieldName, display.createStringListChild());
        }
        else if (fieldClassName.equals("java.util.Map") && genericTypes != null && genericTypes.size() == 2)
        {
            childPresenter = new MapTreeNodePresenter(rpcService, objectConfigurationMap, fieldName, genericTypes,
                    display.createMapChild());
        }
        else if (fieldClassName.equals("java.util.List") && genericTypes != null && genericTypes.size() == 1)
        {
            String genericType = genericTypes.get(0);
            childPresenter = new ListTreeNodePresenter(rpcService, objectConfigurationMap, fieldName, genericType,
                    display.createListChild());
        }
        else
        {
            childPresenter = new ClassTreeNodePresenter(rpcService, objectConfigurationMap, fieldName, fieldClassName,
                    display.createClassChild());
        }

        setNodeNameAndDescription(fieldName, fieldClassName, fieldDescription, childPresenter);
        return childPresenter;
    }

    private void setNodeNameAndDescription(String fieldName, String fieldClassName, String fieldDescription,
            TreeNodePresenter childPresenter)
    {
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
    }

    private boolean isStringListField(String fieldClassName, List<String> genericTypes)
    {
        return fieldClassName.equals("java.util.List") && genericTypes != null && genericTypes.size() == 1
                && genericTypes.get(0).equals("java.lang.String");
    }

    private final static TreeNodePresenterFactory INSTANCE = new TreeNodePresenterFactory();

    public static TreeNodePresenterFactory getInstance()
    {
        return INSTANCE;
    }

}

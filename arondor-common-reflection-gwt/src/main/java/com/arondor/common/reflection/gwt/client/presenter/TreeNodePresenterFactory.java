/*
 *  Copyright 2013, Arondor
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.arondor.common.reflection.gwt.client.presenter;

import java.util.List;

import com.arondor.common.reflection.bean.config.PrimitiveConfigurationBean;
import com.arondor.common.reflection.gwt.client.presenter.fields.ListTreeNodePresenter;
import com.arondor.common.reflection.gwt.client.presenter.fields.MapTreeNodePresenter;
import com.arondor.common.reflection.gwt.client.presenter.fields.PrimitiveTreeNodePresenter;
import com.arondor.common.reflection.gwt.client.presenter.fields.StringListTreeNodePresenter;
import com.arondor.common.reflection.gwt.client.service.GWTReflectionServiceAsync;
import com.arondor.common.reflection.model.config.ObjectConfigurationMap;
import com.arondor.common.reflection.model.config.PrimitiveConfiguration;
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
                accessibleField.getDescription(), accessibleField.isMandatory(), accessibleField.getDefaultValue(), accessibleField.getGenericParameterClassList());
    }

    public TreeNodePresenter createChildNodePresenter(GWTReflectionServiceAsync rpcService,
            ObjectConfigurationMap objectConfigurationMap, TreeNodePresenter.ChildCreatorDisplay display,
            String fieldName, String fieldClassName, String fieldDescription, boolean isMandatory, String defaultValue, List<String> genericTypes)
    {
        TreeNodePresenter childPresenter = null;
        if (PrimitiveTypeUtil.isPrimitiveType(fieldClassName))
        {
            childPresenter = new PrimitiveTreeNodePresenter(fieldName, display.createPrimitiveChild(fieldClassName));
            if (defaultValue != null && !defaultValue.equals("")) {
	            PrimitiveConfiguration primitiveConfiguration = new PrimitiveConfigurationBean();
	            primitiveConfiguration.setValue(defaultValue);
	            childPresenter.setElementConfiguration(primitiveConfiguration);
            }
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

        setNodeNameAndDescription(fieldName, fieldClassName, fieldDescription, isMandatory, childPresenter);
        return childPresenter;
    }

    private void setNodeNameAndDescription(String fieldName, String fieldClassName, String fieldDescription, boolean isMandatory,
            TreeNodePresenter childPresenter)
    {
        String nodeName;
        String nodeDescription = "";
        if (isMandatory) {
        	nodeDescription = "* ";
        }
        if (fieldDescription != null)
        {
            if (fieldDescription.length() >= MAX_DESCRIPTION_LENGTH)
            {
                nodeName = nodeDescription + fieldDescription.substring(0, MAX_DESCRIPTION_LENGTH) + "...";
                nodeDescription += fieldDescription;
            }
            else
            {
                nodeName = nodeDescription + fieldDescription;
                nodeDescription += fieldDescription;
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

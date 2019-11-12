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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.arondor.common.reflection.bean.config.ObjectConfigurationBean;
import com.arondor.common.reflection.bean.config.PrimitiveConfigurationBean;
import com.arondor.common.reflection.gwt.client.presenter.fields.EnumTreeNodePresenter;
import com.arondor.common.reflection.gwt.client.presenter.fields.ListTreeNodePresenter;
import com.arondor.common.reflection.gwt.client.presenter.fields.MapTreeNodePresenter;
import com.arondor.common.reflection.gwt.client.presenter.fields.PrimitiveTreeNodePresenter;
import com.arondor.common.reflection.gwt.client.presenter.fields.StringListTreeNodePresenter;
import com.arondor.common.reflection.gwt.client.service.GWTReflectionServiceAsync;
import com.arondor.common.reflection.model.config.ObjectConfiguration;
import com.arondor.common.reflection.model.config.ObjectConfigurationMap;
import com.arondor.common.reflection.model.config.PrimitiveConfiguration;
import com.arondor.common.reflection.model.java.AccessibleClass;
import com.arondor.common.reflection.model.java.AccessibleField;
import com.arondor.common.reflection.util.PrimitiveTypeUtil;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class TreeNodePresenterFactory
{
    private final static Logger LOG = Logger.getLogger(TreeNodePresenterFactory.class.getName());

    private static final int MAX_DESCRIPTION_LENGTH = 60;

    public TreeNodePresenter createChildNodePresenter(GWTReflectionServiceAsync rpcService,
            ObjectConfigurationMap objectConfigurationMap, TreeNodePresenter.ChildCreatorDisplay display,
            AccessibleField accessibleField)
    {
        String fieldName = accessibleField.getName();
        String fieldClassName = accessibleField.getClassName();
        String fieldDescription = accessibleField.getDescription();
        String fieldLongDescription = accessibleField.getLongDescription();
        String fieldDefaultBehavior = accessibleField.getDefaultBehavior();
        boolean mandatory = accessibleField.isMandatory();
        String defaultValue = accessibleField.getDefaultValue();
        boolean enumProperty = accessibleField.isEnumProperty();
        List<String> genericTypes = accessibleField.getGenericParameterClassList();

        return createChildNodePresenter(rpcService, objectConfigurationMap, display, fieldName, fieldClassName,
                fieldDescription, fieldLongDescription, fieldDefaultBehavior, mandatory, defaultValue, enumProperty,
                accessibleField.getDeclaredInClass(), genericTypes);
    }

    public TreeNodePresenter createChildNodePresenter(GWTReflectionServiceAsync rpcService,
            ObjectConfigurationMap objectConfigurationMap, TreeNodePresenter.ChildCreatorDisplay display,
            String fieldName, final String fieldClassName, String fieldDescription)
    {
        return createChildNodePresenter(rpcService, objectConfigurationMap, display, fieldName, fieldClassName,
                fieldDescription, null, null, false, null, false, null, null);
    }

    public TreeNodePresenter createChildNodePresenter(GWTReflectionServiceAsync rpcService,
            ObjectConfigurationMap objectConfigurationMap, TreeNodePresenter.ChildCreatorDisplay display,
            String fieldName, final String fieldClassName, String fieldDescription, String fieldLongDescription,
            String defaultBehavior, boolean isMandatory, String defaultValue, boolean isEnumProperty,
            final String classDeclaredIn, List<String> genericTypes)
    {
        TreeNodePresenter childPresenter = null;
        if (PrimitiveTypeUtil.isPrimitiveType(fieldClassName))
        {
            LOG.finest("Field " + fieldName + " is primitive type");
            childPresenter = new PrimitiveTreeNodePresenter(fieldName, display.createPrimitiveChild(fieldClassName));
            if (defaultValue != null && !defaultValue.equals(""))
            {
                ((PrimitiveTreeNodePresenter) childPresenter).setDefaultValue(defaultValue);
            }
            PrimitiveConfiguration primitiveConfiguration = new PrimitiveConfigurationBean();
            childPresenter.setElementConfiguration(primitiveConfiguration);
        }
        else if (isEnumProperty)
        {
            LOG.finest("Field " + fieldName + " is associated with enum type");
            childPresenter = createEnumListPresenter(rpcService, display, fieldName, fieldClassName, classDeclaredIn);
        }
        else if (isStringListField(fieldClassName, genericTypes))
        {
            LOG.finest("Field " + fieldName + " is a string list");
            childPresenter = new StringListTreeNodePresenter(fieldName, display.createStringListChild());
        }
        else if (fieldClassName.equals("java.util.Map") && genericTypes != null && genericTypes.size() == 2)
        {
            LOG.severe(
                    "Field " + fieldName + " is an object map of " + genericTypes.get(0) + ", " + genericTypes.get(1));
            childPresenter = new MapTreeNodePresenter(rpcService, objectConfigurationMap, fieldName, genericTypes,
                    display.createMapChild());
        }
        else if (fieldClassName.equals("java.util.List") && genericTypes != null && genericTypes.size() == 1)
        {
            LOG.finest("Field " + fieldName + " is an " + genericTypes.get(0) + " list");
            String genericType = genericTypes.get(0);
            childPresenter = new ListTreeNodePresenter(rpcService, objectConfigurationMap, fieldName, genericType,
                    display.createListChild());
        }
        else
        {
            LOG.finest("Field " + fieldName + " is an object " + fieldClassName);
            childPresenter = new ClassTreeNodePresenter(rpcService, objectConfigurationMap, fieldName, fieldClassName,
                    display.createClassChild());
        }

        setNodeNameAndDescription(fieldName, fieldClassName, fieldDescription, fieldLongDescription, defaultBehavior,
                isMandatory, childPresenter);
        return childPresenter;
    }

    private TreeNodePresenter createEnumListPresenter(GWTReflectionServiceAsync rpcService,
            TreeNodePresenter.ChildCreatorDisplay display, String fieldName, final String fieldClassName,
            final String classDeclaredIn)
    {
        final EnumTreeNodePresenter childPresenter = new EnumTreeNodePresenter(fieldName,
                display.createEnumListChild());

        if (classDeclaredIn != null)
        {
            rpcService.getAccessibleClass(classDeclaredIn, new AsyncCallback<AccessibleClass>()
            {
                @Override
                public void onSuccess(AccessibleClass result)
                {
                    LOG.finest("Retrieve enum class : " + result.getClassBaseName());
                    Map<String, List<String>> enumValuesMap = result.getAccessibleEnums();
                    if (enumValuesMap == null || enumValuesMap.isEmpty())
                    {
                        LOG.warning("No enum type defined for class : " + fieldClassName);
                        return;
                    }
                    List<String> enumVals = enumValuesMap.get(fieldClassName);
                    if (enumVals == null || enumVals.isEmpty())
                    {
                        LOG.warning("Impossible to get enumtype values for " + fieldClassName);
                        return;
                    }
                    final List<String> enumValues = new ArrayList<String>();
                    enumValues.addAll(enumVals);

                    if (enumValues.isEmpty())
                    {
                        LOG.warning("Impossible to get values for enum : " + fieldClassName);
                    }
                    else
                    {
                        childPresenter.setEnumValues(enumValues);
                    }
                }

                @Override
                public void onFailure(Throwable caught)
                {
                }
            });

        }
        ObjectConfiguration objectConfiguration = new ObjectConfigurationBean();
        childPresenter.setElementConfiguration(objectConfiguration);
        return childPresenter;
    }

    private void setNodeNameAndDescription(String fieldName, String fieldClassName, String fieldDescription,
            String fieldLongDescription, String defaultBehavior, boolean isMandatory, TreeNodePresenter childPresenter)
    {
        String nodeName = fieldName;
        String nodeDescription = null;
        if (fieldDescription != null)
        {
            if (fieldDescription.length() < MAX_DESCRIPTION_LENGTH)
            {
                nodeName = fieldDescription;
            }
            else
            {
                nodeName = fieldDescription.substring(0, MAX_DESCRIPTION_LENGTH) + "...";
                nodeDescription = fieldDescription;
            }
        }
        if (fieldLongDescription != null)
        {
            nodeDescription = fieldLongDescription;
        }
        String fieldPrefix = isMandatory ? "* " : "";

        childPresenter.getDisplay().setNodeName(fieldPrefix + nodeName);
        if (nodeDescription != null)
        {
            childPresenter.getDisplay().setNodeDescription(nodeDescription);
        }
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

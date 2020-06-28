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
import java.util.Arrays;
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
import com.arondor.common.reflection.model.config.PrimitiveConfiguration;
import com.arondor.common.reflection.model.java.AccessibleClass;
import com.arondor.common.reflection.model.java.AccessibleField;
import com.arondor.common.reflection.util.PrimitiveTypeUtil;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class TreeNodePresenterFactory
{
    private final static Logger LOG = Logger.getLogger(TreeNodePresenterFactory.class.getName());

    public TreeNodePresenter createChildNodePresenter(GWTReflectionServiceAsync rpcService,
            ObjectReferencesProvider objectReferencesProvider, TreeNodePresenter.ChildCreatorDisplay display,
            AccessibleField accessibleField)
    {
        String fieldName = accessibleField.getName();
        String fieldClassName = accessibleField.getClassName();
        boolean isMandatory = accessibleField.isMandatory();
        String defaultValue = accessibleField.getDefaultValue();
        String placeholder = accessibleField.getPlaceholder();
        String classDeclaredIn = accessibleField.getDeclaredInClass();
        boolean isEnumProperty = accessibleField.isEnumProperty();
        List<String> genericTypes = accessibleField.getGenericParameterClassList();

        TreeNodePresenter childPresenter = null;
        if (PrimitiveTypeUtil.isPrimitiveType(fieldClassName))
        {
            LOG.finest("Field " + fieldName + " is primitive type, class=" + fieldClassName + ", defaultValue="
                    + defaultValue);
            childPresenter = new PrimitiveTreeNodePresenter(display.createPrimitiveChild(fieldClassName, isMandatory));
            if (defaultValue != null && !defaultValue.equals(""))
            {
                ((PrimitiveTreeNodePresenter) childPresenter).setDefaultValue(defaultValue);
            }
            if (placeholder != null && !placeholder.equals(""))
            {
                ((PrimitiveTreeNodePresenter) childPresenter).setPlaceholder(placeholder);
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
            childPresenter = new StringListTreeNodePresenter(fieldName, display.createStringListChild(isMandatory));
            if (defaultValue != null && !defaultValue.equals(""))
            {
                ((StringListTreeNodePresenter) childPresenter)
                        .setDefaultValue(Arrays.asList(defaultValue.trim().split("\n")));
            }
            if (placeholder != null && !placeholder.equals(""))
            {
                ((StringListTreeNodePresenter) childPresenter)
                        .setPlaceholder(Arrays.asList(placeholder.trim().split("\n")));
            }
        }
        else if (fieldClassName.equals("java.util.Map") && genericTypes != null && genericTypes.size() == 2)
        {
            LOG.finest(
                    "Field " + fieldName + " is an object map of " + genericTypes.get(0) + ", " + genericTypes.get(1));
            childPresenter = new MapTreeNodePresenter(rpcService, objectReferencesProvider, genericTypes,
                    display.createMapChild(isMandatory));
        }
        else if (fieldClassName.equals("java.util.List") && genericTypes != null && genericTypes.size() == 1)
        {
            LOG.finest("Field " + fieldName + " is an " + genericTypes.get(0) + " list");
            String genericType = genericTypes.get(0);
            childPresenter = new ListTreeNodePresenter(rpcService, objectReferencesProvider, genericType,
                    display.createListChild(isMandatory));
        }
        else
        {
            LOG.finest("Field " + fieldName + " is an object " + fieldClassName);
            childPresenter = new ClassTreeNodePresenter(rpcService, objectReferencesProvider, fieldClassName,
                    isMandatory, display.createClassChild(isMandatory));
        }

        setNodeNameAndDescription(fieldName, accessibleField, childPresenter);
        return childPresenter;
    }

    private TreeNodePresenter createEnumListPresenter(GWTReflectionServiceAsync rpcService,
            TreeNodePresenter.ChildCreatorDisplay display, String fieldName, final String fieldClassName,
            final String classDeclaredIn)
    {
        final EnumTreeNodePresenter childPresenter = new EnumTreeNodePresenter(fieldName,
                display.createEnumListChild(false));

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

    private void setNodeNameAndDescription(String fieldName, AccessibleField accessibleField,
            TreeNodePresenter childPresenter)
    {
        childPresenter.getDisplay().setNodeName(fieldName);
        if (accessibleField.getDescription() != null)
        {
            childPresenter.getDisplay().setNodeDescription(accessibleField.getDescription());
        }
        else
        {
            childPresenter.getDisplay().setNodeDescription(fieldName);
        }
        if (accessibleField.getLongDescription() != null)
        {
            childPresenter.getDisplay().setNodeLongDescription(accessibleField.getLongDescription());
        }
        if (accessibleField.isPassword())
        {
            childPresenter.getDisplay().setIsPassword();
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

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
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.arondor.common.reflection.gwt.client.event.TreeNodeClearEvent;
import com.arondor.common.reflection.gwt.client.service.GWTReflectionServiceAsync;
import com.arondor.common.reflection.model.config.ElementConfiguration;
import com.arondor.common.reflection.model.config.ObjectConfiguration;
import com.arondor.common.reflection.model.config.ObjectConfigurationFactory;
import com.arondor.common.reflection.model.config.ObjectConfigurationMap;
import com.arondor.common.reflection.model.config.ReferenceConfiguration;
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

    private final ObjectConfigurationMap objectConfigurationMap;

    public ClassTreeNodePresenter(GWTReflectionServiceAsync rpcService, ObjectConfigurationMap objectConfigurationMap,
            String baseClassName, ClassDisplay view)
    {
        this(rpcService, objectConfigurationMap, null, baseClassName, view);
        display.setNodeName(baseClassName);
    }

    protected ClassTreeNodePresenter(GWTReflectionServiceAsync rpcService,
            ObjectConfigurationMap objectConfigurationMap, String fieldName, String baseClassName, ClassDisplay view)
    {
        this.fieldName = fieldName;
        this.rpcService = rpcService;
        this.display = view;
        this.objectConfigurationMap = objectConfigurationMap;

        LOG.finest("Create new TreeNodePresenter for fieldName=" + fieldName + ", baseClassName=" + baseClassName);

        implementingClassPresenter = new ImplementingClassPresenter(rpcService, objectConfigurationMap, baseClassName,
                display.getImplementingClassDisplay());
        bind();
    }

    private void bind()
    {
        implementingClassPresenter.addValueChangeHandler(new ValueChangeHandler<ImplementingClass>()
        {
            public void onValueChange(ValueChangeEvent<ImplementingClass> event)
            {
                if (event.getValue().isReference())
                {
                    classTreeNodePresenterMap.clear();
                }
                else
                {
                    updateAccessibleClass(event.getValue().getName(), null);
                }
            }
        });

        display.addTreeNodeClearHandler(new TreeNodeClearEvent.Handler()
        {
            public void onTreeNodeClearEvent(TreeNodeClearEvent treeNodeClearEvent)
            {
                implementingClassPresenter.setImplementingClass(ImplementingClass.NULL_CLASS);
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
                implementingClassPresenter.setImplementingClass(new ImplementingClass(false, objectConfiguration
                        .getClassName()));

                if (PrimitiveTypeUtil.isPrimitiveType(objectConfiguration.getClassName())
                        && objectConfiguration.getConstructorArguments().size() == 1)
                {
                    TreeNodePresenter valuePresenter = classTreeNodePresenterMap.get("value");
                    if (valuePresenter != null)
                    {
                        valuePresenter.setElementConfiguration(objectConfiguration.getConstructorArguments().get(0));
                    }
                }
                else if (objectConfiguration.getFields() != null)
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
                    rpcService, objectConfigurationMap, display, accessibleField);
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

    public ClassDisplay getDisplay()
    {
        return display;
    }

    public ElementConfiguration getElementConfiguration(ObjectConfigurationFactory objectConfigurationFactory)
    {
        if (implementingClassPresenter.getImplementingClass() == null
                || implementingClassPresenter.getImplementingClass().getName() == null)
        {
            return null;
        }
        if (PrimitiveTypeUtil.isPrimitiveType(implementingClassPresenter.getImplementingClass().getName()))
        {
            String implementingClass = implementingClassPresenter.getImplementingClass().getName();
            if (implementingClass.equals("java.lang.String"))
            {
                /**
                 * This is rather hacky
                 */
                TreeNodePresenter valuePresenter = classTreeNodePresenterMap.get("value");
                if (valuePresenter != null)
                {
                    ElementConfiguration valueConfiguration = valuePresenter
                            .getElementConfiguration(objectConfigurationFactory);
                    ObjectConfiguration objectConfiguration = objectConfigurationFactory.createObjectConfiguration();
                    objectConfiguration.setClassName(implementingClass);
                    objectConfiguration.setFields(new LinkedHashMap<String, ElementConfiguration>());
                    objectConfiguration.setConstructorArguments(new ArrayList<ElementConfiguration>());
                    objectConfiguration.getConstructorArguments().add(valueConfiguration);
                    return objectConfiguration;
                }
            }
            throw new RuntimeException("NOT IMPLEMENTED YET");
        }
        else if (implementingClassPresenter.getImplementingClass().isReference())
        {
            ReferenceConfiguration referenceConfiguration = objectConfigurationFactory.createReferenceConfiguration();
            referenceConfiguration.setReferenceName(implementingClassPresenter.getImplementingClass().getName());
            return referenceConfiguration;
        }
        else
        {
            return getObjectConfiguration(objectConfigurationFactory);
        }

    }

    private ElementConfiguration getObjectConfiguration(ObjectConfigurationFactory objectConfigurationFactory)
    {
        ObjectConfiguration objectConfiguration = objectConfigurationFactory.createObjectConfiguration();
        objectConfiguration.setFields(new LinkedHashMap<String, ElementConfiguration>());
        objectConfiguration.setClassName(implementingClassPresenter.getImplementingClass().getName());

        updateChildObjectConfigurations(objectConfigurationFactory, objectConfiguration);
        return objectConfiguration;
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
            ObjectConfiguration objectConfiguration = (ObjectConfiguration) elementConfiguration;
            updateAccessibleClass(objectConfiguration.getClassName(), objectConfiguration);
        }
        else if (elementConfiguration instanceof ReferenceConfiguration)
        {
            ReferenceConfiguration referenceConfiguration = (ReferenceConfiguration) elementConfiguration;
            implementingClassPresenter.setImplementingClass(new ImplementingClass(true, referenceConfiguration
                    .getReferenceName()));
        }
    }

    public ImplementingClass getImplementingClass()
    {
        return implementingClassPresenter.getImplementingClass();
    }

}

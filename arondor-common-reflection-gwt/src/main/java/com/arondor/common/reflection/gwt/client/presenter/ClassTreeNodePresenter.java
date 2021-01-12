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

import com.arondor.common.reflection.gwt.client.AccessibleClassPresenterFactory;
import com.arondor.common.reflection.gwt.client.event.TreeNodeClearEvent;
import com.arondor.common.reflection.gwt.client.service.GWTReflectionServiceAsync;
import com.arondor.common.reflection.model.config.ElementConfiguration;
import com.arondor.common.reflection.model.config.ObjectConfiguration;
import com.arondor.common.reflection.model.config.ObjectConfigurationFactory;
import com.arondor.common.reflection.model.config.ReferenceConfiguration;
import com.arondor.common.reflection.model.java.AccessibleClass;
import com.arondor.common.reflection.model.java.AccessibleField;
import com.arondor.common.reflection.util.PrimitiveTypeUtil;
import com.arondor.common.reflection.util.StrongReference;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.rpc.AsyncCallback;

import gwt.material.design.client.ui.MaterialDialog;
import gwt.material.design.client.ui.MaterialToast;

public class ClassTreeNodePresenter implements TreeNodePresenter
{
    private static final Logger LOG = Logger.getLogger(ClassTreeNodePresenter.class.getName());

    public interface ClassDisplay extends TreeNodePresenter.ChildCreatorDisplay
    {
        ImplementingClassPresenter.ImplementingClassDisplay getImplementingClassDisplay();

        HandlerRegistration onShare(ClickHandler handler);

        HandlerRegistration forwardToSharedObject(ClickHandler handler);

        String getKeyName();

        void clearKeyName();

        MaterialDialog getConvertTaskDialog();

        HandlerRegistration onCancelShare(ClickHandler handler);

        HandlerRegistration onDoShare(ClickHandler handler);

        void setSharedObjectDisplay(Boolean isRef);
    }

    private final Map<String, TreeNodePresenter> classTreeNodePresenterMap = new HashMap<String, TreeNodePresenter>();

    private final ClassDisplay display;

    private final ImplementingClassPresenter implementingClassPresenter;

    private final GWTReflectionServiceAsync rpcService;

    private final ObjectReferencesProvider objectReferencesProvider;

    public ClassTreeNodePresenter(GWTReflectionServiceAsync rpcService,
            ObjectReferencesProvider objectReferencesProvider, String baseClassName, ClassDisplay view)
    {
        this(rpcService, objectReferencesProvider, baseClassName, true, view);
        display.setNodeName(baseClassName);
    }

    protected ClassTreeNodePresenter(GWTReflectionServiceAsync rpcService,
            ObjectReferencesProvider objectReferencesProvider, String baseClassName, boolean isMandatory,
            ClassDisplay view)
    {
        this.rpcService = rpcService;
        this.display = view;
        this.objectReferencesProvider = objectReferencesProvider;

        LOG.finest("Create new TreeNodePresenter for baseClassName=" + baseClassName);

        implementingClassPresenter = new DefaultImplementingClassPresenter(rpcService, objectReferencesProvider,
                baseClassName, isMandatory, display.getImplementingClassDisplay());
        bind();
    }

    private void bind()
    {
        implementingClassPresenter.addValueChangeHandler(new ValueChangeHandler<ImplementingClass>()
        {
            @Override
            public void onValueChange(ValueChangeEvent<ImplementingClass> event)
            {
                LOG.finest("value change");
                if (event.getValue().isReference())
                {
                    clearFields();
                    display.setActive(true);
                }
                else
                {
                    updateAccessibleClass(event.getValue().getClassName(), null);
                }
            }
        });

        display.addTreeNodeClearHandler(new TreeNodeClearEvent.Handler()
        {
            @Override
            public void onTreeNodeClearEvent(TreeNodeClearEvent treeNodeClearEvent)
            {
                implementingClassPresenter.setImplementingClass(ImplementingClass.NULL_CLASS);
                clearFields();
            }
        });

        display.onShare(new ClickHandler()
        {
            @Override
            public void onClick(ClickEvent event)
            {
                display.getConvertTaskDialog().open();
            }
        });

        display.onCancelShare(new ClickHandler()
        {
            @Override
            public void onClick(ClickEvent event)
            {
                display.getConvertTaskDialog().close();
            }
        });

        display.onDoShare(new ClickHandler()
        {
            @Override
            public void onClick(ClickEvent event)
            {
                String name = display.getKeyName();
                if (!name.isEmpty())
                {
                    objectReferencesProvider.share(getObjectConfiguration(), name,
                            new AsyncCallback<ImplementingClass>()
                            {
                                @Override
                                public void onFailure(Throwable caught)
                                {
                                    LOG.info("Could not get reference configuration");
                                }

                                @Override
                                public void onSuccess(ImplementingClass result)
                                {
                                    clearFields();
                                    implementingClassPresenter.setImplementingClass(result);
                                    display.setActive(true);
                                    display.getConvertTaskDialog().close();
                                }
                            });
                    display.clearKeyName();
                }
                else
                {
                    MaterialToast.fireToast("Enter the name of your shared object");
                }
            }

        });

        display.forwardToSharedObject(new ClickHandler()
        {

            @Override
            public void onClick(ClickEvent event)
            {
                String key = implementingClassPresenter.getImplementingClass().getDisplayName();
                objectReferencesProvider.forward(key, new AsyncCallback<ImplementingClass>()
                {

                    @Override
                    public void onFailure(Throwable caught)
                    {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void onSuccess(ImplementingClass result)
                    {
                        // TODO Auto-generated method stub

                    }
                });

            }
        });
    }

    private void updateAccessibleClass(String className, final ObjectConfiguration objectConfiguration)
    {
        rpcService.getAccessibleClass(className, new AsyncCallback<AccessibleClass>()
        {
            @Override
            public void onSuccess(AccessibleClass result)
            {
                updateAccessibleClass(result, objectConfiguration);
            }

            @Override
            public void onFailure(Throwable caught)
            {
                clearFields();
            }
        });
    }

    private void clearFields()
    {
        classTreeNodePresenterMap.clear();
        display.clear();
    }

    private void updateAccessibleClass(AccessibleClass accessibleClass, ObjectConfiguration objectConfiguration)
    {
        LOG.finest("* updateAccessibleClass(class=" + (accessibleClass != null ? accessibleClass.getName() : null)
                + ", objectConfiguration=" + objectConfiguration + ")");

        clearFields();

        if (accessibleClass != null)
        {
            display.setActive(true);
            buildTree(accessibleClass);

            if (objectConfiguration != null)
            {
                ImplementingClass implementingClass = new ImplementingClass(accessibleClass);
                implementingClassPresenter.setImplementingClass(implementingClass);
                if (implementingClass.isReference())
                    display.setSharedObjectDisplay(true);
                else
                    display.setSharedObjectDisplay(false);

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
            if (accessibleField.getWritable())
            {
                TreeNodePresenter childPresenter = TreeNodePresenterFactory.getInstance()
                        .createChildNodePresenter(rpcService, objectReferencesProvider, display, accessibleField);
                LOG.finest("At field=" + accessibleField.getName() + ", created childPresenter=" + childPresenter);
                classTreeNodePresenterMap.put(accessibleField.getName(), childPresenter);
            }
        }
    }

    public String getBaseClassName()
    {
        return implementingClassPresenter.getBaseClassName();
    }

    @Override
    public ClassDisplay getDisplay()
    {
        return display;
    }

    @Override
    public ElementConfiguration getElementConfiguration()
    {
        ObjectConfigurationFactory objectConfigurationFactory = AccessibleClassPresenterFactory
                .getObjectConfigurationFactory();
        if (implementingClassPresenter.getImplementingClass() == null
                || implementingClassPresenter.getImplementingClass().getClassName() == null)
        {
            return null;
        }
        if (PrimitiveTypeUtil.isPrimitiveType(implementingClassPresenter.getImplementingClass().getClassName()))
        {
            String implementingClass = implementingClassPresenter.getImplementingClass().getClassName();
            if (implementingClass.equals("java.lang.String"))
            {
                /**
                 * This is rather hacky
                 */
                TreeNodePresenter valuePresenter = classTreeNodePresenterMap.get("value");
                if (valuePresenter != null)
                {
                    ElementConfiguration valueConfiguration = valuePresenter.getElementConfiguration();
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
            referenceConfiguration.setReferenceName(implementingClassPresenter.getImplementingClass().getDisplayName());
            return referenceConfiguration;
        }
        else
        {
            ImplementingClass implementingClass = implementingClassPresenter.getImplementingClass();
            LOG.info("Serializing for implementingClass=" + implementingClass);
            if (implementingClass.isReference())
            {
                ReferenceConfiguration referenceConfiguration = objectConfigurationFactory
                        .createReferenceConfiguration();
                referenceConfiguration.setReferenceName(implementingClass.getDisplayName());
                return referenceConfiguration;
            }
            return getObjectConfiguration();
        }

    }

    private ObjectConfiguration getObjectConfiguration()
    {
        ObjectConfigurationFactory objectConfigurationFactory = AccessibleClassPresenterFactory
                .getObjectConfigurationFactory();

        ObjectConfiguration objectConfiguration = objectConfigurationFactory.createObjectConfiguration();
        objectConfiguration.setFields(new LinkedHashMap<String, ElementConfiguration>());
        objectConfiguration.setClassName(implementingClassPresenter.getImplementingClass().getClassName());

        updateChildObjectConfigurations(objectConfigurationFactory, objectConfiguration);
        return objectConfiguration;
    }

    private void updateChildObjectConfigurations(ObjectConfigurationFactory objectConfigurationFactory,
            ObjectConfiguration objectConfiguration)
    {
        for (Map.Entry<String, TreeNodePresenter> presentersEntry : classTreeNodePresenterMap.entrySet())
        {
            LOG.info("* at field=" + presentersEntry.getKey());
            ElementConfiguration childConfiguration = presentersEntry.getValue().getElementConfiguration();
            if (childConfiguration != null)
            {
                objectConfiguration.getFields().put(presentersEntry.getKey(), childConfiguration);
            }
        }
    }

    @Override
    public void setElementConfiguration(ElementConfiguration elementConfiguration)
    {
        LOG.warning("setElementConfiguration :" + elementConfiguration);
        if (elementConfiguration instanceof ObjectConfiguration)
        {
            ObjectConfiguration objectConfiguration = (ObjectConfiguration) elementConfiguration;
            if (StrongReference.CLASSNAME.equals(objectConfiguration.getClassName()))
            {
                if (objectConfiguration.getConstructorArguments().size() == 1
                        && objectConfiguration.getConstructorArguments().get(0) != null
                        && objectConfiguration.getConstructorArguments().get(0) instanceof ReferenceConfiguration)
                {
                    ReferenceConfiguration referenceConfiguration = (ReferenceConfiguration) objectConfiguration
                            .getConstructorArguments().get(0);
                    setElementConfiguration(referenceConfiguration);
                }
                else
                {
                    throw new RuntimeException("StrongReference format not supported !" + elementConfiguration);
                }
            }
            else
            {
                updateAccessibleClass(objectConfiguration.getClassName(), objectConfiguration);
            }
        }
        else if (elementConfiguration instanceof ReferenceConfiguration)
        {
            clearFields();
            ReferenceConfiguration referenceConfiguration = (ReferenceConfiguration) elementConfiguration;
            implementingClassPresenter
                    .setImplementingClass(new ImplementingClass(true, null, referenceConfiguration.getReferenceName()));
            display.setActive(true);
        }
    }

    public ImplementingClass getImplementingClass()
    {
        return implementingClassPresenter.getImplementingClass();
    }

}

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
import java.util.function.Consumer;
import java.util.logging.Logger;

import com.arondor.common.reflection.gwt.client.AccessibleClassPresenterFactory;
import com.arondor.common.reflection.gwt.client.event.ClassChangeEvent;
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

import gwt.material.design.client.ui.MaterialToast;

public class ClassTreeNodePresenter implements TreeNodePresenter
{
    private static final Logger LOG = Logger.getLogger(ClassTreeNodePresenter.class.getName());

    public interface ClassDisplay extends TreeNodePresenter.ChildCreatorDisplay
    {
        ImplementingClassPresenter.ImplementingClassDisplay getImplementingClassDisplay();

        HandlerRegistration onReset(ClickHandler handler);

        void onShare(Consumer<String> onShare);

        HandlerRegistration forwardToSharedObject(ClickHandler handler);

        void enableReset(boolean enabled);

        void closeDialog();

        String getKeyName();

        void clearKeyName();

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

    public ClassTreeNodePresenter(GWTReflectionServiceAsync rpcService,
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

        display.onReset(new ClickHandler()
        {
            @Override
            public void onClick(ClickEvent event)
            {
                implementingClassPresenter.setImplementingClass(ImplementingClass.NULL_CLASS);
                clearFields();
            }
        });

        display.onShare(new Consumer<String>()
        {

            @Override
            public void accept(String scope)
            {
                share(scope);
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

    private void share(String scope)
    {
        String name = display.getKeyName();
        if (!name.isEmpty())
        {
            objectReferencesProvider.share(getObjectConfiguration(), name, scope, new AsyncCallback<ImplementingClass>()
            {
                @Override
                public void onFailure(Throwable caught)
                {
                    LOG.info("Could not share reference configuration because " + caught.getMessage());
                    display.clearKeyName();
                }

                @Override
                public void onSuccess(ImplementingClass result)
                {
                    clearFields();
                    implementingClassPresenter.setImplementingClass(result);
                    display.setActive(true);
                    display.closeDialog();
                }
            });
        }
        else
        {
            MaterialToast.fireToast("Enter the name of your shared object");
        }
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
        fireClassChange(accessibleClass, null);
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
        if (implementingClassPresenter.getImplementingClass() == null)
        {
            return null;
        }
        if (implementingClassPresenter.getImplementingClass().isReference())
        {
            ReferenceConfiguration referenceConfiguration = objectConfigurationFactory.createReferenceConfiguration();
            referenceConfiguration.setReferenceName(implementingClassPresenter.getImplementingClass().getDisplayName());
            return referenceConfiguration;
        }
        String implementingClass = implementingClassPresenter.getImplementingClass().getClassName();
        LOG.info("Serializing for implementingClass=" + implementingClass);
        if (implementingClass == null)
        {
            return null;
        }

        if (PrimitiveTypeUtil.isPrimitiveType(implementingClassPresenter.getImplementingClass().getClassName()))
        {
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
        return getObjectConfiguration();
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
            fireClassChange(null, referenceConfiguration.getReferenceName());
        }
    }

    public ImplementingClass getImplementingClass()
    {
        return implementingClassPresenter.getImplementingClass();
    }

    private final List<ClassChangeEvent.Handler> classChangeHandlers = new ArrayList<ClassChangeEvent.Handler>();

    private void fireClassChange(AccessibleClass accessibleClass, String reference)
    {
        ClassChangeEvent event = new ClassChangeEvent(accessibleClass, reference);
        classChangeHandlers.forEach(h -> h.onClassChange(event));
    }

    public HandlerRegistration addClassChangeHandler(ClassChangeEvent.Handler handler)
    {
        classChangeHandlers.add(handler);
        return new HandlerRegistration()
        {
            @Override
            public void removeHandler()
            {
                classChangeHandlers.remove(handler);
            }
        };
        /*
         * return implementingClassPresenter.addValueChangeHandler(new
         * ValueChangeHandler<ImplementingClass>() {
         * 
         * @Override public void
         * onValueChange(ValueChangeEvent<ImplementingClass> event) {
         * LOG.warning("onValueChange(" + event.getValue() + ")"); if
         * (event.getValue().isReference()) { ClassChangeEvent classChangeEvent
         * = new ClassChangeEvent(null, event.getValue().getDisplayName());
         * handler.onClassChange(classChangeEvent); } else if
         * (event.getValue().getClassName() != null) {
         * rpcService.getAccessibleClass(event.getValue().getClassName(), new
         * AsyncCallback<AccessibleClass>() {
         * 
         * @Override public void onFailure(Throwable caught) { }
         * 
         * @Override public void onSuccess(AccessibleClass result) {
         * ClassChangeEvent classChangeEvent = new ClassChangeEvent(result,
         * null); handler.onClassChange(classChangeEvent); } }); } else {
         * ClassChangeEvent classChangeEvent = new ClassChangeEvent(null, null);
         * handler.onClassChange(classChangeEvent); } } });
         */
    }

}

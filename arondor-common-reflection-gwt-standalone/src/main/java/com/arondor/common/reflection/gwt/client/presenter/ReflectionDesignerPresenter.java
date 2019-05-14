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
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.logging.Logger;

import com.arondor.common.reflection.bean.config.ObjectConfigurationFactoryBean;
import com.arondor.common.reflection.gwt.client.api.ObjectConfigurationMapPresenter;
import com.arondor.common.reflection.gwt.client.presenter.SimpleObjectConfigurationMapPresenter.ObjectConfigurationMapDisplay;
import com.arondor.common.reflection.gwt.client.service.GWTObjectConfigurationService;
import com.arondor.common.reflection.gwt.client.service.GWTObjectConfigurationServiceAsync;
import com.arondor.common.reflection.gwt.client.service.GWTReflectionServiceAsync;
import com.arondor.common.reflection.gwt.client.view.ObjectConfigurationMapView;
import com.arondor.common.reflection.gwt.client.view.ReflectionDesignerMenuView;
import com.arondor.common.reflection.gwt.client.view.ReflectionDesignerView;
import com.arondor.common.reflection.model.config.ElementConfiguration;
import com.arondor.common.reflection.model.config.MapConfiguration;
import com.arondor.common.reflection.model.config.ObjectConfiguration;
import com.arondor.common.reflection.model.config.ObjectConfigurationFactory;
import com.arondor.common.reflection.model.config.ObjectConfigurationMap;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;

public class ReflectionDesignerPresenter
{
    private static final Logger LOG = Logger.getLogger(ReflectionDesignerPresenter.class.getName());

    private final static GWTObjectConfigurationServiceAsync SERVICE = GWT.create(GWTObjectConfigurationService.class);

    private final ObjectConfigurationFactory objectConfigurationFactory = new ObjectConfigurationFactoryBean();

    private final ObjectConfigurationMapPresenter objectConfigurationMapPresenter;

    public interface MenuDisplay extends IsWidget
    {
        String getLoadConfigContext();

        void setLoadConfigContext(String context);

        HasClickHandlers getLoadConfigButton();

        String getLoadLibsContext();

        void setLoadLibsContext(String context);

        HasClickHandlers getLoadLibsButton();

        HasClickHandlers getSaveButton();
    }

    private final MenuDisplay menuDisplay;

    public MenuDisplay getMenuDisplay()
    {
        return menuDisplay;
    }

    public interface DesignerDisplay extends IsWidget
    {
        void setMenuDisplay(MenuDisplay menuDisplay);

        void setObjectConfigurationMapDisplay(ObjectConfigurationMapDisplay objectConfigurationMapDisplay);
    }

    private final DesignerDisplay designerDisplay;

    private final String fieldName = "fieldName";

    private final ObjectConfigurationMapDisplay objectConfigurationMapDisplay = new ObjectConfigurationMapView();

    public DesignerDisplay getDisplay()
    {
        return designerDisplay;
    }

    public ReflectionDesignerPresenter(GWTReflectionServiceAsync reflectionService, String baseClassName)
    {
        this.menuDisplay = new ReflectionDesignerMenuView();
        this.designerDisplay = new ReflectionDesignerView();

        this.designerDisplay.setMenuDisplay(menuDisplay);
        this.designerDisplay.setObjectConfigurationMapDisplay(objectConfigurationMapDisplay);
        this.objectConfigurationMapPresenter = new SimpleObjectConfigurationMapPresenter(reflectionService, fieldName,
                objectConfigurationMapDisplay);
        bind();
    }

    public void bind()
    {

        menuDisplay.getSaveButton().addClickHandler(new ClickHandler()
        {
            @Override
            public void onClick(ClickEvent event)
            {

            }
        });

        menuDisplay.getLoadConfigButton().addClickHandler(new ClickHandler()
        {
            @Override
            public void onClick(ClickEvent event)
            {
                String context = menuDisplay.getLoadConfigContext();
                readObjectConfiguration(context);
            }
        });

        menuDisplay.getLoadLibsButton().addClickHandler(new ClickHandler()
        {
            @Override
            public void onClick(ClickEvent event)
            {
                String context = menuDisplay.getLoadLibsContext();
                List<String> packagePrefixes = new ArrayList<String>();
                packagePrefixes.add("com.arondor");

                SERVICE.loadLib(context, packagePrefixes, new AsyncCallback<Void>()
                {

                    @Override
                    public void onFailure(Throwable caught)
                    {
                        Window.alert("Could not parse" + caught);
                    }

                    @Override
                    public void onSuccess(Void result)
                    {
                    }
                });

            }
        });

    }

    protected void readObjectConfiguration(final String context)
    {
        SERVICE.getObjectConfigurationMap(context, new AsyncCallback<ObjectConfigurationMap>()
        {
            @Override
            public void onSuccess(ObjectConfigurationMap result)
            {
                objectConfigurationMapPresenter.setObjectConfigurationMap(result);
            }

            @Override
            public void onFailure(Throwable caught)
            {
                Window.alert("Error ! context:" + context);
            }
        });
    }

    protected void setDefaultObject()
    {
        ObjectConfiguration childObjectConfiguration = objectConfigurationFactory.createObjectConfiguration();
        childObjectConfiguration.setClassName("com.arondor.common.reflection.gwt.server.samples.TestClass");

        childObjectConfiguration.setFields(new LinkedHashMap<String, ElementConfiguration>());

        childObjectConfiguration.getFields().put("aStringProperty",
                objectConfigurationFactory.createPrimitiveConfiguration("test"));
        childObjectConfiguration.getFields().put("aLongProperty",
                objectConfigurationFactory.createPrimitiveConfiguration("123"));

        ObjectConfiguration parentObjectConfiguration = objectConfigurationFactory.createObjectConfiguration();
        parentObjectConfiguration.setClassName("com.arondor.common.reflection.gwt.server.samples.ParentTestClass");
        parentObjectConfiguration.setFields(new HashMap<String, ElementConfiguration>());
        parentObjectConfiguration.getFields().put("primitiveField",
                objectConfigurationFactory.createPrimitiveConfiguration("My Primitive Field"));
        parentObjectConfiguration.getFields().put("testInterfaceField", childObjectConfiguration);

        ObjectConfiguration otherObject = objectConfigurationFactory.createObjectConfiguration();
        otherObject.setClassName("com.arondor.common.reflection.gwt.server.samples.TestClassBis");
        parentObjectConfiguration.getFields().put("testClassBisField", otherObject);

        MapConfiguration stringMapConfiguration = objectConfigurationFactory.createMapConfiguration();
        otherObject.setFields(new HashMap<String, ElementConfiguration>());
        otherObject.getFields().put("stringMapField", stringMapConfiguration);

        stringMapConfiguration.setMapConfiguration(new HashMap<ElementConfiguration, ElementConfiguration>());
        stringMapConfiguration.getMapConfiguration().put(
                objectConfigurationFactory.createPrimitiveConfiguration("key1"),
                objectConfigurationFactory.createPrimitiveConfiguration("value1"));
        stringMapConfiguration.getMapConfiguration().put(
                objectConfigurationFactory.createPrimitiveConfiguration("key2"),
                objectConfigurationFactory.createPrimitiveConfiguration("value2"));

        ObjectConfiguration myString = objectConfigurationFactory.createObjectConfiguration();
        myString.setClassName("java.lang.String");
        myString.setFields(new HashMap<String, ElementConfiguration>());
        myString.setConstructorArguments(new ArrayList<ElementConfiguration>());
        myString.getConstructorArguments().add(objectConfigurationFactory.createPrimitiveConfiguration("a value"));
        parentObjectConfiguration.getFields().put("anObject", myString);

        // classPresenter.setObjectConfiguration(parentObjectConfiguration);
    }
}

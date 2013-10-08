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
package com.arondor.common.reflection.sample.gwt.client;

import com.arondor.common.reflection.api.instantiator.InstantiationContext;
import com.arondor.common.reflection.api.instantiator.ReflectionInstantiator;
import com.arondor.common.reflection.model.config.ObjectConfigurationMap;
import com.arondor.common.reflection.sample.gwt.client.service.ConfigurationService;
import com.arondor.common.reflection.sample.gwt.client.service.ConfigurationServiceAsync;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

public class SampleEntryPoint implements EntryPoint
{

    public void onModuleLoad()
    {
        ConfigurationServiceAsync configurationService = GWT.create(ConfigurationService.class);

        configurationService.getObjectConfigurations(new AsyncCallback<ObjectConfigurationMap>()
        {

            public void onSuccess(ObjectConfigurationMap result)
            {
                useObjectConfigurationMap(result);
            }

            public void onFailure(Throwable caught)
            {
                Window.alert("Error : " + caught.getMessage());
            }
        });
    }

    protected void useObjectConfigurationMap(ObjectConfigurationMap objectConfigurationMap)
    {
        ReflectionInstantiator reflectionInstantiator = GWT.create(ReflectionInstantiator.class);
        InstantiationContext context = GWT.create(InstantiationContext.class);
        context.addSharedObjectConfigurations(objectConfigurationMap);
        Widget widget = reflectionInstantiator.instanciateObject("rootWidget", Widget.class, context);

        RootPanel.get().add(widget);
    }

}

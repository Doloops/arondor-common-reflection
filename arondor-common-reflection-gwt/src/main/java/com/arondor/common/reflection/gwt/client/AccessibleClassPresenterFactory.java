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
package com.arondor.common.reflection.gwt.client;

import java.util.List;

import com.arondor.common.reflection.bean.config.ObjectConfigurationFactoryBean;
import com.arondor.common.reflection.gwt.client.api.AccessibleClassPresenter;
import com.arondor.common.reflection.gwt.client.api.ObjectConfigurationMapPresenter;
import com.arondor.common.reflection.gwt.client.nview.NViewFactory;
import com.arondor.common.reflection.gwt.client.presenter.HierarchicAccessibleClassPresenter;
import com.arondor.common.reflection.gwt.client.presenter.ObjectReferencesProvider;
import com.arondor.common.reflection.gwt.client.presenter.SimpleObjectConfigurationMapPresenter;
import com.arondor.common.reflection.gwt.client.presenter.SimpleObjectConfigurationMapPresenter.ObjectConfigurationMapDisplay;
import com.arondor.common.reflection.gwt.client.presenter.ViewFactory;
import com.arondor.common.reflection.gwt.client.service.GWTReflectionServiceAsync;
import com.arondor.common.reflection.gwt.client.view.ObjectConfigurationMapView;
import com.arondor.common.reflection.model.config.ObjectConfigurationFactory;
import com.google.gwt.core.client.GWT;

/**
 * Helper class to create Hierarchic Class Presenters
 * 
 * @author Francois Barre
 * 
 */
public class AccessibleClassPresenterFactory
{
    /**
     * This is a utility class, so hide constructor
     */
    private AccessibleClassPresenterFactory()
    {
    }

    static
    {
        if (GWT.isClient())
        {
            CssBundle.INSTANCE.css().ensureInjected();
        }
    }

    private static final ViewFactory FACTORY = new NViewFactory();

    public static AccessibleClassPresenter createAccessibleClassPresenter(GWTReflectionServiceAsync rpcService,
            ObjectReferencesProvider objectReferencesProvider, String baseClassName)
    {
        boolean isLink = baseClassName.equals("com.fast2.model.taskflow.design.TaskLinkCondition");
        return new HierarchicAccessibleClassPresenter(rpcService, objectReferencesProvider, baseClassName,
                FACTORY.createClassDisplay(isLink));
    }

    public static ObjectConfigurationMapPresenter createObjectConfigurationMapPresenter(
            GWTReflectionServiceAsync rpcService, List<String> availableScopes)
    {
        ObjectConfigurationMapDisplay mapDisplay = new ObjectConfigurationMapView();
        return new SimpleObjectConfigurationMapPresenter(rpcService, "Shared Objects", mapDisplay, availableScopes);
    }

    private static final ObjectConfigurationFactory OBJECT_CONFIGURATION_FACTORY = new ObjectConfigurationFactoryBean();

    public static ObjectConfigurationFactory getObjectConfigurationFactory()
    {
        return OBJECT_CONFIGURATION_FACTORY;
    }
}

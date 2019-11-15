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

import com.arondor.common.reflection.gwt.client.api.AccessibleClassPresenter;
import com.arondor.common.reflection.gwt.client.api.ObjectConfigurationMapPresenter;
import com.arondor.common.reflection.gwt.client.presenter.HierarchicAccessibleClassPresenter;
import com.arondor.common.reflection.gwt.client.presenter.SimpleObjectConfigurationMapPresenter;
import com.arondor.common.reflection.gwt.client.presenter.SimpleObjectConfigurationMapPresenter.ObjectConfigurationMapDisplay;
import com.arondor.common.reflection.gwt.client.presenter.ViewFactory;
import com.arondor.common.reflection.gwt.client.service.GWTReflectionServiceAsync;
import com.arondor.common.reflection.gwt.client.view.DefaultViewFactory;
import com.arondor.common.reflection.gwt.client.view.ObjectConfigurationMapView;
import com.arondor.common.reflection.model.config.ObjectConfigurationMap;

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

    private static final ViewFactory FACTORY = new DefaultViewFactory();

    public static AccessibleClassPresenter createAccessibleClassPresenter(GWTReflectionServiceAsync rpcService,
            ObjectConfigurationMap objectConfigurationMap, String baseClassName)
    {
        return new HierarchicAccessibleClassPresenter(rpcService, objectConfigurationMap, baseClassName,
                FACTORY.createClassDisplay());
    }

    public static ObjectConfigurationMapPresenter createObjectConfigurationMapPresenter(
            GWTReflectionServiceAsync rpcService)
    {
        ObjectConfigurationMapDisplay mapDisplay = new ObjectConfigurationMapView();
        return new SimpleObjectConfigurationMapPresenter(rpcService, "Shared Objects", mapDisplay);
    }
}

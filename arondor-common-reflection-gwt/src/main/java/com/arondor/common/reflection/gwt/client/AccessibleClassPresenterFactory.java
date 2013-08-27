package com.arondor.common.reflection.gwt.client;

import com.arondor.common.reflection.gwt.client.api.AccessibleClassPresenter;
import com.arondor.common.reflection.gwt.client.api.ObjectConfigurationMapPresenter;
import com.arondor.common.reflection.gwt.client.presenter.HierarchicAccessibleClassPresenter;
import com.arondor.common.reflection.gwt.client.presenter.SimpleObjectConfigurationMapPresenter;
import com.arondor.common.reflection.gwt.client.presenter.SimpleObjectConfigurationMapPresenter.ObjectConfigurationMapDisplay;
import com.arondor.common.reflection.gwt.client.service.GWTReflectionServiceAsync;
import com.arondor.common.reflection.gwt.client.view.HierarchicAccessibleClassView;
import com.arondor.common.reflection.gwt.client.view.ObjectConfigurationMapView;
import com.arondor.common.reflection.model.config.ObjectConfigurationMap;

public class AccessibleClassPresenterFactory
{
    public static AccessibleClassPresenter createAccessibleClassPresenter(GWTReflectionServiceAsync rpcService,
            ObjectConfigurationMap objectConfigurationMap, String baseClassName)
    {
        return new HierarchicAccessibleClassPresenter(rpcService, objectConfigurationMap, baseClassName,
                new HierarchicAccessibleClassView());
    }

    public static ObjectConfigurationMapPresenter createObjectConfigurationMapPresenter(
            GWTReflectionServiceAsync rpcService)
    {
        ObjectConfigurationMapDisplay mapDisplay = new ObjectConfigurationMapView();
        return new SimpleObjectConfigurationMapPresenter(rpcService, "Shared Objects", mapDisplay);
    }
}

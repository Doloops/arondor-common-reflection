package com.arondor.common.reflection.gwt.client;

import com.arondor.common.reflection.gwt.client.api.AccessibleClassPresenter;
import com.arondor.common.reflection.gwt.client.presenter.HierarchicAccessibleClassPresenter;
import com.arondor.common.reflection.gwt.client.service.GWTReflectionServiceAsync;
import com.arondor.common.reflection.gwt.client.view.HierarchicAccessibleClassView;

public class AccessibleClassPresenterFactory
{
    public static AccessibleClassPresenter createAccessibleClassPresenter(GWTReflectionServiceAsync rpcService,
            String baseClassName)
    {
        return new HierarchicAccessibleClassPresenter(rpcService, baseClassName, new HierarchicAccessibleClassView());
    }
}

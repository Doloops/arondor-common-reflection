package com.arondor.common.reflection.gwt.client;

import com.arondor.common.reflection.gwt.client.presenter.AccessibleClassPresenter;
import com.arondor.common.reflection.gwt.client.presenter.SimpleAccessibleClassPresenter;
import com.arondor.common.reflection.gwt.client.service.GWTReflectionService;
import com.arondor.common.reflection.gwt.client.service.GWTReflectionServiceAsync;
import com.arondor.common.reflection.gwt.client.view.AccessibleClassView;
import com.google.gwt.core.client.GWT;

public class AccessibleClassPresenterFactory
{
    private static final GWTReflectionServiceAsync ACCESSIBLE_CLASS_SERVICE = GWT
            .create(GWTReflectionService.class);

    public static AccessibleClassPresenter createAccessibleClassPresenter()
    {
        return new SimpleAccessibleClassPresenter(ACCESSIBLE_CLASS_SERVICE, new AccessibleClassView());
    }
}

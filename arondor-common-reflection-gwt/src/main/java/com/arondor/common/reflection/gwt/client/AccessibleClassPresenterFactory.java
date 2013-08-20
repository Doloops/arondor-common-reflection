package com.arondor.common.reflection.gwt.client;

import com.arondor.common.reflection.gwt.client.presenter.AccessibleClassPresenter;
import com.arondor.common.reflection.gwt.client.presenter.SimpleAccessibleClassPresenter;
import com.arondor.common.reflection.gwt.client.service.AccessibleClassService;
import com.arondor.common.reflection.gwt.client.service.AccessibleClassServiceAsync;
import com.arondor.common.reflection.gwt.client.view.AccessibleClassView;
import com.google.gwt.core.client.GWT;

public class AccessibleClassPresenterFactory
{
    private static final AccessibleClassServiceAsync ACCESSIBLE_CLASS_SERVICE = GWT
            .create(AccessibleClassService.class);

    public static AccessibleClassPresenter createAccessibleClassPresenter()
    {
        return new SimpleAccessibleClassPresenter(ACCESSIBLE_CLASS_SERVICE, new AccessibleClassView());
    }
}

package com.arondor.common.reflection.gwt.client;

import com.arondor.common.reflection.gwt.client.presenter.AccessibleClassPresenter;
import com.arondor.common.reflection.gwt.client.view.AccessibleClassView;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerManager;

public class MVPEntryPoint implements EntryPoint
{

    public void onModuleLoad()
    {
        HandlerManager eventBus = new HandlerManager(null);
        bind();
        new AccessibleClassPresenter((AccessibleClassServiceAsync) GWT.create(AccessibleClassService.class), eventBus,
                new AccessibleClassView());
    }

    private void bind()
    {
    }
}

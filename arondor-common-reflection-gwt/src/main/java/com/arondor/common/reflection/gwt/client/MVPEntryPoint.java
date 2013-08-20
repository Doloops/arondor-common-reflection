package com.arondor.common.reflection.gwt.client;

import com.arondor.common.reflection.gwt.client.presenter.ClassDesignerPresenter;
import com.arondor.common.reflection.gwt.client.view.ClassDesignerView;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.shared.HandlerManager;

public class MVPEntryPoint implements EntryPoint
{

    public void onModuleLoad()
    {
        HandlerManager eventBus = new HandlerManager(null);
        bind();
        new ClassDesignerPresenter(eventBus, new ClassDesignerView());
    }

    private void bind()
    {
    }
}

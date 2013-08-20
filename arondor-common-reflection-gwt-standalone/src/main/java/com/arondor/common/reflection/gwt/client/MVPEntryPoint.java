package com.arondor.common.reflection.gwt.client;

import com.arondor.common.reflection.gwt.client.presenter.ClassDesignerPresenter;
import com.arondor.common.reflection.gwt.client.view.ClassDesignerView;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;

public class MVPEntryPoint implements EntryPoint
{

    public void onModuleLoad()
    {
        bind();
        ClassDesignerPresenter classPresenter = new ClassDesignerPresenter(new ClassDesignerView());

        RootPanel.get().clear();
        RootPanel.get().add(classPresenter.getDisplay());
    }

    private void bind()
    {
    }
}

package com.arondor.common.reflection.gwt.client.view;

import com.arondor.common.reflection.gwt.client.presenter.ClassDesignerPresenter;
import com.arondor.common.reflection.gwt.client.presenter.HierarchicAccessibleClassPresenter;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class ClassDesignerView extends Composite implements ClassDesignerPresenter.Display
{
    private Button getConfigButton = new Button("Get");

    private Button setConfigButton = new Button("Set");

    private final AbsolutePanel content = new AbsolutePanel();

    public ClassDesignerView()
    {
        content.add(getConfigButton);
        content.add(setConfigButton);
        initWidget(content);
    }

    public Widget asWidget()
    {
        return this;
    }

    public void setAccessibleClassView(HierarchicAccessibleClassPresenter.Display accessibleClassView)
    {
        this.content.add(accessibleClassView);
    }

    public HasClickHandlers getGetConfigButton()
    {
        return getConfigButton;
    }

    public HasClickHandlers getSetConfigButton()
    {
        return setConfigButton;
    }
}

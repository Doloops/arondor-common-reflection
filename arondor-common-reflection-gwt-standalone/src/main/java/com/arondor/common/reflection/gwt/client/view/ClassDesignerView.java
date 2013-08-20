package com.arondor.common.reflection.gwt.client.view;

import com.arondor.common.reflection.gwt.client.presenter.ClassDesignerPresenter;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

public class ClassDesignerView extends Composite implements ClassDesignerPresenter.Display
{
    private AccessibleClassView accessibleClassView;

    private Button getConfigButton;

    private Button setConfigButton;

    private final AbsolutePanel content = new AbsolutePanel();

    public ClassDesignerView()
    {
        initWidget(content);

        AbsolutePanel btnGroup = new AbsolutePanel();
        btnGroup.setStyleName("btn-group");

        content.add(new HTML("<h1>Class Designer</h1>"));

        getConfigButton = new Button("Get");
        getConfigButton.setStyleName("btn");
        getConfigButton.addStyleName("btn-primary");

        btnGroup.add(getConfigButton);

        setConfigButton = new Button("Set");
        setConfigButton.setStyleName("btn");
        setConfigButton.addStyleName("btn-primary");

        btnGroup.add(setConfigButton);

        content.add(btnGroup);
    }

    public AccessibleClassView getAccessibleClassView()
    {
        return accessibleClassView;
    }

    public Widget asWidget()
    {
        return this;
    }

    public void setAccessibleClassView(AccessibleClassView accessibleClassView)
    {
        this.accessibleClassView = accessibleClassView;
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

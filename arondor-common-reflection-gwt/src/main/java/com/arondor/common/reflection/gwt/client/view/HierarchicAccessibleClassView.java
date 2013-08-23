package com.arondor.common.reflection.gwt.client.view;

import com.arondor.common.reflection.gwt.client.presenter.ClassTreePresenter;
import com.arondor.common.reflection.gwt.client.presenter.ClassTreePresenter.Display;
import com.arondor.common.reflection.gwt.client.presenter.HierarchicAccessibleClassPresenter;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

public class HierarchicAccessibleClassView extends Composite implements HierarchicAccessibleClassPresenter.Display
{
    private ClassTreePresenter.Display classTreeDisplay = new ClassTreeView();

    public HierarchicAccessibleClassView()
    {
        AbsolutePanel content = new AbsolutePanel();

        initWidget(content);

        content.add(classTreeDisplay);
    }

    public Display getClassTreeDisplay()
    {
        return classTreeDisplay;
    }

    public IsWidget getClassTreeDisplayWidget()
    {
        return classTreeDisplay;
    }

    public Widget asWidget()
    {
        return this;
    }

}

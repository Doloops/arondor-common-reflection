package com.arondor.common.reflection.gwt.client.nview;

import com.arondor.common.reflection.gwt.client.CssBundle;
import com.arondor.common.reflection.gwt.client.presenter.ClassTreeNodePresenter;
import com.arondor.common.reflection.gwt.client.presenter.ClassTreePresenter;
import com.arondor.common.reflection.gwt.client.presenter.HierarchicAccessibleClassPresenter;
import com.google.gwt.user.client.ui.FlowPanel;

public class NClassTreeView extends FlowPanel
        implements ClassTreePresenter.Display, HierarchicAccessibleClassPresenter.Display
{
    private final NClassNodeView rootView = new NClassNodeView();

    public NClassTreeView(boolean isLink)
    {
        if (!isLink)
            rootView.disableReset();
        getElement().addClassName(CssBundle.INSTANCE.css().rootTreeNode());
        add(rootView);
    }

    public NClassTreeView()
    {
        this(false);
    }

    @Override
    public ClassTreePresenter.Display getClassTreeDisplay()
    {
        return this;
    }

    @Override
    public ClassTreeNodePresenter.ClassDisplay getRootView()
    {
        return rootView;
    }

}

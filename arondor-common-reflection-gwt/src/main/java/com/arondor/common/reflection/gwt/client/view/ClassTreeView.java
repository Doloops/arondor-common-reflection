package com.arondor.common.reflection.gwt.client.view;

import com.arondor.common.reflection.gwt.client.presenter.ClassTreeNodePresenter;
import com.arondor.common.reflection.gwt.client.presenter.ClassTreePresenter;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.Widget;

public class ClassTreeView extends Composite implements ClassTreePresenter.Display
{
    private Tree tree = new Tree();

    public ClassTreeView()
    {
        AbsolutePanel content = new AbsolutePanel();
        initWidget(content);

        content.add(new Label("Tree"));

        content.add(tree);
    }

    private Tree getTree()
    {
        return tree;
    }

    public ClassTreeNodePresenter.ClassDisplay createRootView(String baseClassName)
    {
        return new ClassTreeNodeView(getTree());
    }

    public Widget asWidget()
    {
        return this;
    }

}

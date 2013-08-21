package com.arondor.common.reflection.gwt.client.view;

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

    public Widget asWidget()
    {
        return this;
    }

    public Tree getTree()
    {
        return tree;
    }
}

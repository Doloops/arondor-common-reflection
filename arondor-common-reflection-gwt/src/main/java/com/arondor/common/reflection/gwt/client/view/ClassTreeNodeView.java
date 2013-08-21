package com.arondor.common.reflection.gwt.client.view;

import com.arondor.common.reflection.gwt.client.presenter.ClassTreeNodePresenter;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

public class ClassTreeNodeView extends Composite implements ClassTreeNodePresenter.Display
{

    private TreeItem nodeItem = new TreeItem();

    public ClassTreeNodeView(UIObject parentNode)
    {
        AbsolutePanel content = new AbsolutePanel();
        initWidget(content);

        if (parentNode instanceof Tree)
            ((Tree) parentNode).addItem(nodeItem);
        else if (parentNode instanceof TreeItem)
            ((TreeItem) parentNode).addItem(nodeItem);
    }

    public Widget asWidget()
    {
        return this;
    }

    public void addItem(String child)
    {
        TreeItem childItem = new TreeItem();
        childItem.setHTML(child);
        nodeItem.addItem(childItem);
    }

    public void setNodeName(String name)
    {
        nodeItem.setHTML(name);
    }

    public TreeItem getTreeItem()
    {
        return nodeItem;
    }
}

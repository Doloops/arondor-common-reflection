package com.arondor.common.reflection.gwt.client.view;

import com.arondor.common.reflection.gwt.client.presenter.AccessibleFieldMapPresenter;
import com.arondor.common.reflection.gwt.client.presenter.ClassTreeNodePresenter;
import com.arondor.common.reflection.gwt.client.presenter.ClassTreeNodePresenter.Display;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.UIObject;

public class ClassTreeNodeView extends TreeItem implements ClassTreeNodePresenter.Display
{

    private AccessibleFieldMapPresenter.Display fieldMapDisplay = new AccessibleFieldMapView();

    private ClassTreeNodePresenter classTreeNodePresenter;

    public ClassTreeNodeView(UIObject parentNode)
    {
        if (parentNode instanceof Tree)
        {
            ((Tree) parentNode).addItem(this);
        }
        else if (parentNode instanceof TreeItem)
        {
            ((TreeItem) parentNode).addItem(this);
        }
    }

    public void setNodeName(String name)
    {
        setHTML(name);
    }

    public Display createChild()
    {
        return new ClassTreeNodeView(getTreeItem());
    }

    public TreeItem getTreeItem()
    {
        return this;
    }

    public void setClassTreeNodePresenter(ClassTreeNodePresenter classTreeNodePresenter)
    {
        this.classTreeNodePresenter = classTreeNodePresenter;
    }

    public ClassTreeNodePresenter getClassTreeNodePresenter()
    {
        return classTreeNodePresenter;
    }

    public AccessibleFieldMapPresenter.Display getFieldMapDisplay()
    {
        return fieldMapDisplay;
    }
}
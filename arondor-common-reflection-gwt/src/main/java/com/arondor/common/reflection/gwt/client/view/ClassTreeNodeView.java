package com.arondor.common.reflection.gwt.client.view;

import com.arondor.common.reflection.gwt.client.presenter.AccessibleFieldMapPresenter;
import com.arondor.common.reflection.gwt.client.presenter.ClassTreeNodePresenter;
import com.arondor.common.reflection.gwt.client.presenter.ClassTreeNodePresenter.Display;
import com.arondor.common.reflection.gwt.client.presenter.ImplementingClassPresenter;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.UIObject;

public class ClassTreeNodeView extends TreeItem implements ClassTreeNodePresenter.Display
{
    private final Label nodeLabel = new Label("");

    private final AccessibleFieldMapPresenter.Display fieldMapDisplay = new AccessibleFieldMapView();

    private final ImplementingClassPresenter.Display implementingClassDisplay = new ImplementingClassView();

    private ClassTreeNodePresenter classTreeNodePresenter;

    public ClassTreeNodeView(UIObject parentNode)
    {
        addItemToParent(parentNode);
        HorizontalPanel contents = new HorizontalPanel();
        contents.add(nodeLabel);
        nodeLabel.addStyleDependentName("classTreeNodeView");
        contents.add(new Label(" - "));
        contents.add(implementingClassDisplay);
        setWidget(contents);

        bind();
    }

    private void bind()
    {
    }

    private void addItemToParent(UIObject parentNode)
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
        nodeLabel.setText(name);
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

    public ImplementingClassPresenter.Display getImplementingClassDisplay()
    {
        return implementingClassDisplay;
    }

    public void clear()
    {
        removeItems();
    }
}
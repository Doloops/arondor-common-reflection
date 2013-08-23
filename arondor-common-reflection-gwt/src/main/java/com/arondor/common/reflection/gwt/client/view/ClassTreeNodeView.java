package com.arondor.common.reflection.gwt.client.view;

import com.arondor.common.reflection.gwt.client.presenter.ClassTreeNodePresenter;
import com.arondor.common.reflection.gwt.client.presenter.ClassTreeNodePresenter.ClassDisplay;
import com.arondor.common.reflection.gwt.client.presenter.ImplementingClassPresenter;
import com.arondor.common.reflection.gwt.client.presenter.PrimitiveTreeNodePresenter.PrimitiveDisplay;
import com.arondor.common.reflection.gwt.client.presenter.StringListTreeNodePresenter.StringListDisplay;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.UIObject;

public class ClassTreeNodeView extends AbstractTreeNodeView implements ClassTreeNodePresenter.ClassDisplay
{
    private final ImplementingClassPresenter.Display implementingClassDisplay = new ImplementingClassView();

    public ClassTreeNodeView(UIObject parentNode)
    {
        super(parentNode);
        getContents().add(implementingClassDisplay.asWidget());
    }

    public ClassDisplay createClassChild()
    {
        return new ClassTreeNodeView(getTreeItem());
    }

    public PrimitiveDisplay createPrimitiveChild(String fieldClassName)
    {
        if (fieldClassName.equals("boolean") || fieldClassName.equals("java.lang.Boolean"))
        {
            return new PrimitiveBooleanView(getTreeItem());
        }
        return new PrimitiveStringView(getTreeItem());
    }

    public StringListDisplay createStringListChild()
    {
        return new StringListView(getTreeItem());
    }

    public TreeItem getTreeItem()
    {
        return this;
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
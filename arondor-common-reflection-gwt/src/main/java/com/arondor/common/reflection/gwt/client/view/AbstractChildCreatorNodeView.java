package com.arondor.common.reflection.gwt.client.view;

import com.arondor.common.reflection.gwt.client.presenter.ClassTreeNodePresenter.ClassDisplay;
import com.arondor.common.reflection.gwt.client.presenter.TreeNodePresenter;
import com.arondor.common.reflection.gwt.client.presenter.fields.MapTreeNodePresenter.MapRootDisplay;
import com.arondor.common.reflection.gwt.client.presenter.fields.PrimitiveTreeNodePresenter.PrimitiveDisplay;
import com.arondor.common.reflection.gwt.client.presenter.fields.StringListTreeNodePresenter.StringListDisplay;
import com.arondor.common.reflection.gwt.client.view.fields.MapRootView;
import com.arondor.common.reflection.gwt.client.view.fields.PrimitiveBooleanView;
import com.arondor.common.reflection.gwt.client.view.fields.PrimitiveStringView;
import com.arondor.common.reflection.gwt.client.view.fields.StringListView;
import com.google.gwt.user.client.ui.UIObject;

public abstract class AbstractChildCreatorNodeView extends AbstractTreeNodeView implements
        TreeNodePresenter.ChildCreatorDisplay
{

    protected AbstractChildCreatorNodeView(UIObject parentNode)
    {
        super(parentNode);
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

    public MapRootDisplay createMapChild()
    {
        return new MapRootView(getTreeItem());
    }

    public StringListDisplay createStringListChild()
    {
        return new StringListView(getTreeItem());
    }
}

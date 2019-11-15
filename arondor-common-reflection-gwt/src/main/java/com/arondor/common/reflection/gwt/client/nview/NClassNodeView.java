package com.arondor.common.reflection.gwt.client.nview;

import com.arondor.common.reflection.gwt.client.CssBundle;
import com.arondor.common.reflection.gwt.client.nview.prim.NBooleanView;
import com.arondor.common.reflection.gwt.client.nview.prim.NStringView;
import com.arondor.common.reflection.gwt.client.presenter.ClassTreeNodePresenter;
import com.arondor.common.reflection.gwt.client.presenter.ImplementingClassPresenter.ImplementingClassDisplay;
import com.arondor.common.reflection.gwt.client.presenter.fields.EnumTreeNodePresenter.EnumDisplay;
import com.arondor.common.reflection.gwt.client.presenter.fields.ListTreeNodePresenter.ListRootDisplay;
import com.arondor.common.reflection.gwt.client.presenter.fields.MapTreeNodePresenter.MapRootDisplay;
import com.arondor.common.reflection.gwt.client.presenter.fields.PrimitiveTreeNodePresenter.PrimitiveDisplay;
import com.arondor.common.reflection.gwt.client.presenter.fields.StringListTreeNodePresenter.StringListDisplay;
import com.arondor.common.reflection.gwt.client.view.ImplementingClassView;

public class NClassNodeView extends NNodeView implements ClassTreeNodePresenter.ClassDisplay
{
    private final ImplementingClassDisplay implementingClassView = new ImplementingClassView();

    public NClassNodeView()
    {
        getElement().addClassName(CssBundle.INSTANCE.css().classNode());
        add(implementingClassView);
    }

    @Override
    public ImplementingClassDisplay getImplementingClassDisplay()
    {
        return implementingClassView;
    }

    @Override
    public ClassTreeNodePresenter.ClassDisplay createClassChild()
    {
        NClassNodeView childView = new NClassNodeView();
        add(childView);
        return childView;
    }

    @Override
    public PrimitiveDisplay createPrimitiveChild(String fieldClassName)
    {
        NNodeView view;
        if (fieldClassName.equals("boolean"))
        {
            view = new NBooleanView();
        }
        else
        {
            view = new NStringView();
        }
        add(view);
        return (PrimitiveDisplay) view;
    }

    @Override
    public EnumDisplay createEnumListChild()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public StringListDisplay createStringListChild()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public MapRootDisplay createMapChild()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ListRootDisplay createListChild()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void clear()
    {
        super.clear();
        add(implementingClassView);
    }

}

package com.arondor.common.reflection.gwt.client.view;

import com.arondor.common.reflection.gwt.client.presenter.ClassTreeNodePresenter;
import com.arondor.common.reflection.gwt.client.presenter.ImplementingClassPresenter;
import com.google.gwt.user.client.ui.UIObject;

public class ClassTreeNodeView extends AbstractChildCreatorNodeView implements ClassTreeNodePresenter.ClassDisplay
{
    private final ImplementingClassPresenter.Display implementingClassDisplay = new ImplementingClassView();

    public ClassTreeNodeView(UIObject parentNode)
    {
        super(parentNode);
        setHasRemoveButton(true);
        getContents().add(implementingClassDisplay.asWidget());
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
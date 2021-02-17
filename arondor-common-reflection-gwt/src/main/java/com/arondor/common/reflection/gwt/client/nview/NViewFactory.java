package com.arondor.common.reflection.gwt.client.nview;

import com.arondor.common.reflection.gwt.client.presenter.HierarchicAccessibleClassPresenter;
import com.arondor.common.reflection.gwt.client.presenter.ViewFactory;

public class NViewFactory implements ViewFactory
{

    @Override
    public HierarchicAccessibleClassPresenter.Display createClassDisplay(boolean isLink)
    {
        return new NClassTreeView(isLink);
    }
}

package com.arondor.common.reflection.gwt.client.nview;

import java.util.List;

import com.arondor.common.reflection.gwt.client.presenter.HierarchicAccessibleClassPresenter;
import com.arondor.common.reflection.gwt.client.presenter.ViewFactory;

public class NViewFactory implements ViewFactory
{

    @Override
    public HierarchicAccessibleClassPresenter.Display createClassDisplay(List<String> availableScopes)
    {
        return new NClassTreeView(availableScopes);
    }

}

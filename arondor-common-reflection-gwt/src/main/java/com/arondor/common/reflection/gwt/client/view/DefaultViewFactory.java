package com.arondor.common.reflection.gwt.client.view;

import com.arondor.common.reflection.gwt.client.presenter.HierarchicAccessibleClassPresenter.Display;
import com.arondor.common.reflection.gwt.client.presenter.ViewFactory;

public class DefaultViewFactory implements ViewFactory
{
    @Override
    public Display createClassDisplay()
    {
        return new ClassTreeView();
    }
}

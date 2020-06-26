package com.arondor.common.reflection.gwt.client.view;

import com.arondor.common.reflection.gwt.client.CssBundle;
import com.arondor.common.reflection.gwt.client.nview.NMapNodeView;
import com.arondor.common.reflection.gwt.client.presenter.SimpleObjectConfigurationMapPresenter.ObjectConfigurationMapDisplay;
import com.google.gwt.user.client.ui.IsWidget;

public class ObjectConfigurationMapView extends NMapNodeView implements ObjectConfigurationMapDisplay
{
    public ObjectConfigurationMapView()
    {
        getElement().addClassName(CssBundle.INSTANCE.css().rootTreeNode());
    }

    @Override
    public IsWidget getDisplayWidget()
    {
        return this;
    }

}

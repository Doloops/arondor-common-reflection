package com.arondor.common.reflection.gwt.client.view;

import com.arondor.common.reflection.gwt.client.presenter.SimpleObjectConfigurationMapPresenter;
import com.arondor.common.reflection.gwt.client.view.fields.MapRootView;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Tree;

public class ObjectConfigurationMapView extends MapRootView implements
        SimpleObjectConfigurationMapPresenter.ObjectConfigurationMapDisplay
{

    public ObjectConfigurationMapView()
    {
        super(new Tree());
    }

    public IsWidget getDisplayWidget()
    {
        return (IsWidget) getTree();
    }

}

package com.arondor.common.reflection.gwt.client.view;

import com.arondor.common.reflection.gwt.client.CssBundle;
import com.arondor.common.reflection.gwt.client.nview.NMapNodeView;
import com.arondor.common.reflection.gwt.client.nview.NScopeClassView;
import com.arondor.common.reflection.gwt.client.presenter.SimpleObjectConfigurationMapPresenter.ObjectConfigurationMapDisplay;
import com.arondor.common.reflection.gwt.client.presenter.fields.MapTreeNodePresenter.MapPairDisplay;
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

    @Override
    public MapPairDisplay createPair(String keyClass, String valueClass)
    {
        NScopeClassView newPair = new NScopeClassView(keyClass, valueClass);
        newPair.clear();
        mappingTable.add(newPair.asWidget());
        return newPair;
    }
}

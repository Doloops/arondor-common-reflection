package com.arondor.common.reflection.gwt.client.nview;

import java.util.List;

import com.arondor.common.reflection.gwt.client.api.ObjectConfigurationMapPresenter.MapPairDisplayWithScope;
import com.google.gwt.dom.client.Style.Unit;

import gwt.material.design.client.ui.MaterialListBox;

public class NScopeClassView extends NClassNodeViewWithKey implements MapPairDisplayWithScope
{
    private MaterialListBox scopeBox = new MaterialListBox();

    public NScopeClassView(String keyClass, String valueClass)
    {
        super(keyClass, valueClass);
        scopeBox.getElement().getStyle().setWidth(120f, Unit.PX);
    }

    @Override
    protected void bind()
    {
        if (scopeBox != null)
            add(scopeBox);
        super.bind();
    }

    @Override
    public void setAvailableScopes(List<String> scopes)
    {
        for (String scope : scopes)
            scopeBox.add(scope);
    }

    @Override
    public void setScope(String scope)
    {
        scopeBox.setValue(scope);
    }

    @Override
    public String getScope()
    {
        return scopeBox.getValue();
    }
}
package com.arondor.common.reflection.gwt.client.nview;

import java.util.List;

import com.arondor.common.reflection.gwt.client.CssBundle;
import com.arondor.common.reflection.gwt.client.api.ObjectConfigurationMapPresenter.MapPairDisplayWithScope;

import gwt.material.design.addins.client.combobox.MaterialComboBox;

public class NScopeClassView extends NClassNodeViewWithKey implements MapPairDisplayWithScope
{
    private MaterialComboBox<String> scopeBox = new MaterialComboBox<String>();

    public NScopeClassView(String keyClass, String valueClass)
    {
        super(keyClass, valueClass);
        // scopeBox.getElement().getStyle().setWidth(120f, Unit.PX);
        scopeBox.getElement().setClassName("outlined col-1 pl-0");
        scopeBox.getElement().addClassName(CssBundle.INSTANCE.css().scopeSelector());
        scopeBox.setLabel("Scope");
        scopeBox.setHideSearch(true);
    }

    @Override
    protected void attachChildren()
    {
        if (scopeBox != null)
            add(scopeBox);
        super.attachChildren();
    }

    @Override
    public void setAvailableScopes(List<String> scopes)
    {
        for (String scope : scopes)
            scopeBox.addItem(scope);
    }

    @Override
    public void setScope(String scope)
    {
        scopeBox.unselect();
        scopeBox.setSelectedIndex(scopeBox.getValueIndex(scope));
    }

    @Override
    public String getScope()
    {
        if (scopeBox.getSelectedValue().isEmpty())
            return null;
        return scopeBox.getSelectedValue().get(0);
    }
}
package com.arondor.common.reflection.gwt.client.nview;

import com.arondor.common.reflection.gwt.client.presenter.ClassTreeNodePresenter.ClassDisplay;
import com.arondor.common.reflection.gwt.client.presenter.TreeNodePresenter.Display;
import com.arondor.common.reflection.gwt.client.presenter.fields.ListTreeNodePresenter.ListRootDisplay;
import com.google.gwt.event.dom.client.HasClickHandlers;

import gwt.material.design.client.ui.MaterialButton;

/**
 * TODO : Implement this !
 */
public class NListView extends NNodeView implements ListRootDisplay
{
    private final MaterialButton addButton = new MaterialButton();

    @Override
    public void setNodeDescription(String description)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public HasClickHandlers addElementClickHandler()
    {
        return addButton;
    }

    @Override
    public void removeChild(Display childDisplay)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public ClassDisplay createChildDisplay()
    {
        // TODO Auto-generated method stub
        return null;
    }

}

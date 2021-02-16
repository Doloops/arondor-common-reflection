package com.arondor.common.reflection.gwt.client.nview;

import java.util.logging.Logger;

import com.arondor.common.reflection.gwt.client.presenter.ClassTreeNodePresenter.ClassDisplay;
import com.arondor.common.reflection.gwt.client.presenter.TreeNodePresenter.Display;
import com.arondor.common.reflection.gwt.client.presenter.fields.ListTreeNodePresenter.ListRootDisplay;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.FlowPanel;

import gwt.material.design.client.ui.MaterialButton;

/**
 * TODO : Implement this !
 */
public class NListView extends NNodeView implements ListRootDisplay
{
    private static final Logger LOG = Logger.getLogger(NListView.class.getName());

    private final MaterialButton addButton = new MaterialButton();

    private final FlowPanel inputGroupPanel = new FlowPanel();

    public NListView()
    {
        LOG.severe(">> asking to create a listView");
        inputGroupPanel.getElement().setAttribute("style", "border:3px solid red");
        add(inputGroupPanel);
    }

    @Override
    public void setNodeDescription(String description)
    {
        // TODO Auto-generated method stub
        LOG.severe(">> node description is : " + description);
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

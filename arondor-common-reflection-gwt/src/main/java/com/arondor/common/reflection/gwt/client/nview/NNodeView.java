package com.arondor.common.reflection.gwt.client.nview;

import com.arondor.common.reflection.gwt.client.CssBundle;
import com.arondor.common.reflection.gwt.client.event.TreeNodeClearEvent.Handler;
import com.arondor.common.reflection.gwt.client.presenter.TreeNodePresenter;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FocusPanel;

public class NNodeView extends FlowPanel implements TreeNodePresenter.Display
{
    private final FlowPanel nodeNamePanel = new FlowPanel();

    public FlowPanel getNodeNamePanel()
    {
        return nodeNamePanel;
    }

    // private final FlowPanel resetDefault = new FlowPanel();

    // private final FlowPanel resetFieldBtn = new FlowPanel();
    private final FocusPanel resetFieldBtn = new FocusPanel();

    protected NNodeView()
    {
        getElement().addClassName(CssBundle.INSTANCE.css().nodeField());
        resetFieldBtn.getElement().addClassName(CssBundle.INSTANCE.css().resetFieldBtn());
        resetFieldBtn.getElement().addClassName(CssBundle.INSTANCE.css().hidden());
        resetFieldBtn.getElement().setInnerHTML("<span><i></i></span>");

        addChildren();

        nodeNamePanel.getElement().addClassName(CssBundle.INSTANCE.css().nodeName());
    }

    // protected FlowPanel getResetFieldBtn()
    protected FocusPanel getResetFieldBtn()
    {
        return resetFieldBtn;
    }

    @Override
    public void setNodeName(String name)
    {
        nodeNamePanel.getElement().setInnerHTML(name);
    }

    @Override
    public void setNodeDescription(String description)
    {
        nodeNamePanel.getElement().setInnerHTML(description);
    }

    @Override
    public void setNodeLongDescription(String longDescription)
    {
        nodeNamePanel.setTitle(longDescription);
    }

    private boolean active;

    @Override
    public void setActive(boolean active)
    {
        this.active = active;
        if (active)
        {
            resetFieldBtn.getElement().removeClassName(CssBundle.INSTANCE.css().hidden());
        }
        else
        {
            resetFieldBtn.getElement().addClassName(CssBundle.INSTANCE.css().hidden());
        }
    }

    @Override
    public boolean isActive()
    {
        return active;
    }

    @Override
    public void addTreeNodeClearHandler(Handler handler)
    {
        // TODO Auto-generated method stub
    }

    private void addChildren()
    {
        add(nodeNamePanel);
        add(resetFieldBtn);
    }

    @Override
    public void clear()
    {
        super.clear();
        addChildren();
    }

}

package com.arondor.common.reflection.gwt.client.nview;

import com.arondor.common.reflection.gwt.client.CssBundle;
import com.arondor.common.reflection.gwt.client.event.TreeNodeClearEvent.Handler;
import com.arondor.common.reflection.gwt.client.presenter.TreeNodePresenter;
import com.google.gwt.user.client.ui.FlowPanel;

public class NNodeView extends FlowPanel implements TreeNodePresenter.Display
{
    private final FlowPanel nodeNamePanel = new FlowPanel();

    protected NNodeView()
    {
        getElement().addClassName(CssBundle.INSTANCE.css().nodeField());
        add(nodeNamePanel);

        nodeNamePanel.getElement().addClassName(CssBundle.INSTANCE.css().nodeName());

        // nodeNamePanel.setTitle("Coucou caché");
    }

    @Override
    public void setNodeName(String name)
    {
        nodeNamePanel.getElement().setInnerHTML(name);
    }

    @Override
    public void setNodeDescription(String description)
    {
    }

    @Override
    public void setActive(boolean active)
    {
    }

    @Override
    public boolean isActive()
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void addTreeNodeClearHandler(Handler handler)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void clear()
    {
        super.clear();
        add(nodeNamePanel);
    }

}

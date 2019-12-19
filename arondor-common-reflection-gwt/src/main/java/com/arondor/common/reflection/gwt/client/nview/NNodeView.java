package com.arondor.common.reflection.gwt.client.nview;

import com.arondor.common.reflection.gwt.client.CssBundle;
import com.arondor.common.reflection.gwt.client.event.TreeNodeClearEvent.Handler;
import com.arondor.common.reflection.gwt.client.presenter.TreeNodePresenter;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FocusPanel;

public class NNodeView extends FlowPanel implements TreeNodePresenter.Display
{
    private boolean active;

    private final FocusPanel resetBtn = new FocusPanel();

    private boolean resetEnabled = true;

    protected NNodeView()
    {
        getElement().addClassName(CssBundle.INSTANCE.css().nodeField());

        resetBtn.getElement().setInnerHTML("<i></i>");
        resetBtn.getElement().addClassName(CssBundle.INSTANCE.css().resetBtn());
        resetBtn.getElement().addClassName(CssBundle.INSTANCE.css().hidden());
    }

    protected FocusPanel getResetFieldBtn()
    {
        return resetBtn;
    }

    @Override
    public void setNodeName(String name)
    {
        setProperLabel(name);
    }

    @Override
    public void setNodeDescription(String description)
    {
        setProperLabel(description);
    }

    @Override
    public void setProperLabel(String label)
    {
    }

    @Override
    public void setNodeLongDescription(String longDescription)
    {
        String suffix = "<img src=\"data:image/png;base64,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\" title=\""
                + longDescription + "\" style=\"width:25px;\">";

        // nodeNamePanel.getElement().setInnerHTML(nodeNamePanel.getElement().getInnerHTML()
        // + suffix);
        // nodeNamePanel.setTitle(longDescription);
    }

    @Override
    public void setActive(boolean active)
    {
        this.active = active;
        if (active)
        {
            resetBtn.getElement().removeClassName(CssBundle.INSTANCE.css().hidden());
        }
        else
        {
            resetBtn.getElement().addClassName(CssBundle.INSTANCE.css().hidden());
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
    }

    protected void disableReset()
    {
        resetEnabled = false;
        getResetFieldBtn().setVisible(false);

    }

    @Override
    public void clear()
    {
        super.clear();
    }

}

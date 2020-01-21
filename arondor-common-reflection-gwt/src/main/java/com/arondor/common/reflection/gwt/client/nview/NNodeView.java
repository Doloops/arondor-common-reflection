package com.arondor.common.reflection.gwt.client.nview;

import com.arondor.common.reflection.gwt.client.CssBundle;
import com.arondor.common.reflection.gwt.client.event.TreeNodeClearEvent.Handler;
import com.arondor.common.reflection.gwt.client.presenter.TreeNodePresenter;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FocusPanel;

public abstract class NNodeView extends FlowPanel implements TreeNodePresenter.Display
{
    private boolean active;

    private final FocusPanel resetBtn = new FocusPanel();

    private boolean resetEnabled = true;

    private String helperTextContent = "";

    protected NNodeView()
    {
        getElement().addClassName(CssBundle.INSTANCE.css().nodeField());

        resetBtn.getElement().setInnerHTML("<i></i><span></span>");
        // resetBtn.getElement().getElementsByTagName("span").getItem(0).addClassName(CssBundle.INSTANCE.css().halo());
        resetBtn.getElement().addClassName(CssBundle.INSTANCE.css().resetBtn());
        resetBtn.getElement().addClassName(CssBundle.INSTANCE.css().hidden());
    }

    protected FocusPanel getResetFieldBtn()
    {
        return resetBtn;
    }

    protected String getHelperTextContent()
    {
        return helperTextContent;
    }

    @Override
    public void setNodeName(String name)
    {
    }

    @Override
    public void setNodeLongDescription(String longDescription)
    {
        this.helperTextContent = longDescription;
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

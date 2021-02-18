package com.arondor.common.reflection.gwt.client.nview;

import com.arondor.common.reflection.gwt.client.CssBundle;
import com.arondor.common.reflection.gwt.client.presenter.TreeNodePresenter;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FocusPanel;

public abstract class NNodeView extends FlowPanel implements TreeNodePresenter.Display
{
    private boolean active;

    private final FocusPanel resetBtn = new FocusPanel();

    private String helperTextContent = "";

    private boolean enableReset;

    protected NNodeView()
    {
        getElement().addClassName(CssBundle.INSTANCE.css().nodeField());

        resetBtn.getElement().setInnerHTML("<i></i><span></span>");
        resetBtn.getElement().addClassName(CssBundle.INSTANCE.css().resetBtn());

        enableReset(false);
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
    public void setIsPassword()
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
        getResetFieldBtn().setVisible(enableReset && active);

    }

    @Override
    public boolean isActive()
    {
        return active;
    }

    public void enableReset(boolean enabled)
    {
        enableReset = enabled;
        getResetFieldBtn().setVisible(enabled && active);
    }

    @Override
    public void clear()
    {
        super.clear();
    }

}

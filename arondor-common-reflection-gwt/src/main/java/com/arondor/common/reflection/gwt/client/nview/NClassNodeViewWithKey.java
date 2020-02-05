package com.arondor.common.reflection.gwt.client.nview;

import com.arondor.common.reflection.gwt.client.CssBundle;
import com.arondor.common.reflection.gwt.client.presenter.TreeNodePresenter.Display;
import com.arondor.common.reflection.gwt.client.presenter.fields.MapTreeNodePresenter.MapPairDisplay;
import com.arondor.common.reflection.gwt.client.presenter.fields.PrimitiveTreeNodePresenter.PrimitiveDisplay;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.FocusPanel;

import gwt.material.design.client.ui.MaterialTextBox;

public class NClassNodeViewWithKey extends NClassNodeView implements MapPairDisplay
{
    private final MaterialTextBox keyTextBox = new MaterialTextBox();

    private final FocusPanel deleteRowBtn = newDeleteRowBtn();

    public NClassNodeViewWithKey(String keyClass, String valueClass)
    {
        keyTextBox.setClass("outlined col-5 pl-0");
        keyTextBox.getElement().getElementsByTagName("input").getItem(0).addClassName("mb-0");
        getOptionsArea().getElement().setAttribute("style", "width:100%;padding-left:10px;");

        getElement().addClassName("pl-0 m-0");

        getSelectGroup().getElement().addClassName("col-6 pr-0 pl-0 mb-0");

        deleteRowBtn.addClickHandler(new ClickHandler()
        {
            @Override
            public void onClick(ClickEvent event)
            {
                getElement().removeFromParent();
            }
        });
        add(deleteRowBtn);
    }

    @Override
    public void bind()
    {
        if (keyTextBox != null)
        {
            add(keyTextBox);
        }
        if (deleteRowBtn != null)
        {
            // add on value change
            add(deleteRowBtn);
        }
        super.bind();
    }

    @Override
    public HasClickHandlers removePairClickHandler()
    {
        return deleteRowBtn;
    }

    private FocusPanel newDeleteRowBtn()
    {
        FocusPanel deleteRowBtn = new FocusPanel();
        deleteRowBtn.getElement().setInnerHTML("<i></i>");
        deleteRowBtn.getElement().addClassName(CssBundle.INSTANCE.css().deleteRowBtn());
        return deleteRowBtn;
    }

    @Override
    public PrimitiveDisplay getKeyDisplay()
    {
        /*
         * Force adding the Key TextBox if is has not been added at bind() time
         */
        if (keyTextBox.getParent() == null)
        {
            insert(keyTextBox, 0);
        }
        return new PrimitiveMaterialDisplay(keyTextBox);
    }

    @Override
    public Display getValueDisplay()
    {
        return this;
    }
}

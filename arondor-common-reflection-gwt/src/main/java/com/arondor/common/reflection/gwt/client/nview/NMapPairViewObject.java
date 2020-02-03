package com.arondor.common.reflection.gwt.client.nview;

import com.arondor.common.reflection.gwt.client.CssBundle;
import com.arondor.common.reflection.gwt.client.presenter.TreeNodePresenter.Display;
import com.arondor.common.reflection.gwt.client.presenter.fields.MapTreeNodePresenter.MapPairDisplay;
import com.arondor.common.reflection.gwt.client.presenter.fields.PrimitiveTreeNodePresenter.PrimitiveDisplay;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Widget;

import gwt.material.design.client.ui.MaterialTextBox;

public class NMapPairViewObject implements MapPairDisplay
{
    private final FlowPanel pairPanel = new FlowPanel();

    private final MaterialTextBox keyBox = new MaterialTextBox();

    private final NClassNodeView valueView = new NClassNodeView();

    // private final MaterialDialog popup = new MaterialDialog();

    private final FocusPanel deleteRowBtn = newDeleteRowBtn();

    private FocusPanel newDeleteRowBtn()
    {
        FocusPanel deleteRowBtn = new FocusPanel();
        deleteRowBtn.getElement().setInnerHTML("<i></i>");
        deleteRowBtn.getElement().addClassName(CssBundle.INSTANCE.css().deleteRowBtn());
        return deleteRowBtn;
    }

    private FocusPanel newResetBtn()
    {
        FocusPanel resetBtn = new FocusPanel();
        resetBtn.getElement().setInnerHTML("<i></i>");
        resetBtn.getElement().addClassName(CssBundle.INSTANCE.css().resetBtn());
        resetBtn.getElement().addClassName(CssBundle.INSTANCE.css().hidden());
        return resetBtn;
    }

    public NMapPairViewObject(String keyClass, String valueClass)
    {
        valueView.getElement().addClassName("outlined col-5");
        // valueBox.getElement().addClassName(CssBundle.INSTANCE.css().mappingCell());

        FocusPanel resetBtnN = newResetBtn();

        deleteRowBtn.addClickHandler(new ClickHandler()
        {
            @Override
            public void onClick(ClickEvent event)
            {
                pairPanel.removeFromParent();
            }
        });

        resetBtnN.addClickHandler(new ClickHandler()
        {
            @Override
            public void onClick(ClickEvent event)
            {
                // valueBox.clear();
                keyBox.clear();
                resetBtnN.getElement().addClassName(CssBundle.INSTANCE.css().hidden());
            }
        });

        // valueBox.addKeyUpHandler(new KeyUpHandler()
        // {
        // @Override
        // public void onKeyUp(KeyUpEvent event)
        // {
        // resetBtnN.getElement().removeClassName(CssBundle.INSTANCE.css().hidden());
        // }
        // });

        keyBox.addKeyUpHandler(new KeyUpHandler()
        {
            @Override
            public void onKeyUp(KeyUpEvent event)
            {
                resetBtnN.getElement().removeClassName(CssBundle.INSTANCE.css().hidden());
            }
        });

        // valueBox.add(resetBtnN);

        keyBox.setClass("outlined col-5");
        keyBox.getElement().addClassName(CssBundle.INSTANCE.css().mappingCell());
        valueView.getElement().addClassName(CssBundle.INSTANCE.css().mappingCell());

        pairPanel.getElement().addClassName(CssBundle.INSTANCE.css().mappingTablePair());
        pairPanel.add(keyBox);

        pairPanel.add(valueView);
        pairPanel.add(deleteRowBtn);

    }

    @Override
    public PrimitiveDisplay getKeyDisplay()
    {
        return new PrimitiveMaterialDisplay(keyBox);
    }

    @Override
    public Display getValueDisplay()
    {
        return valueView;
    }

    public FlowPanel getPairPanel()
    {
        return pairPanel;
    }

    @Override
    public HasClickHandlers removePairClickHandler()
    {
        return deleteRowBtn;
    }

    @Override
    public Widget asWidget()
    {
        return pairPanel.asWidget();
    }
}

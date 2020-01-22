package com.arondor.common.reflection.gwt.client.nview;

import java.util.Map;

import com.arondor.common.reflection.gwt.client.CssBundle;
import com.arondor.common.reflection.gwt.client.presenter.fields.MapTreeNodePresenter.MapNodeDisplay;
import com.arondor.common.reflection.gwt.client.presenter.fields.MapTreeNodePresenter.MapRootDisplay;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FocusPanel;

import gwt.material.design.client.ui.MaterialCard;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialTextBox;

public class NMapNodeView extends NNodeView implements MapRootDisplay
{
    CellTable<Map<String, String>> table = new CellTable<Map<String, String>>();

    FlowPanel pair = new FlowPanel();

    FlowPanel header = new FlowPanel();

    MaterialCard mappingTable = new MaterialCard();

    MaterialLabel label = new MaterialLabel();

    private final FocusPanel newPairBtn = new FocusPanel();

    private int counter = 0;

    public NMapNodeView()
    {
        getElement().addClassName(CssBundle.INSTANCE.css().mappingField());

        header.getElement().setAttribute("style", "display:flex");

        label.setStyle(
                "color:#9E9E9E;font-size:14px;font-family: Arial Unicode MS, Arial, sans-serif;padding-right:10px;");

        newPairBtn.getElement().setInnerHTML("<i></i>");
        newPairBtn.getElement().addClassName(CssBundle.INSTANCE.css().newPairBtn());

        mappingTable.getElement().addClassName(CssBundle.INSTANCE.css().mappingTable());
        mappingTable.getElement().addClassName("container");

        attachElements();

        attachHandlers();
    }

    private void attachElements()
    {
        mappingTable.add(newPair());

        header.add(label);
        header.add(newPairBtn);

        add(header);
        add(mappingTable);
    }

    private void attachHandlers()
    {

        newPairBtn.addClickHandler(new ClickHandler()
        {

            @Override
            public void onClick(ClickEvent event)
            {
                mappingTable.add(newPair());

            }
        });
    }

    private void checkActive(boolean active, FocusPanel resetBtn)
    {

        if (active)
        {
            resetBtn.getElement().removeClassName(CssBundle.INSTANCE.css().hidden());
        }
        else
        {
            resetBtn.getElement().addClassName(CssBundle.INSTANCE.css().hidden());
        }

    }

    private FlowPanel newPair()
    {
        FlowPanel pairN = new FlowPanel();
        pairN.getElement().setId("pair_" + counter);
        MaterialTextBox keyBoxN = new MaterialTextBox();
        MaterialTextBox valueBoxN = new MaterialTextBox();

        valueBoxN.setClass("outlined col-5");
        valueBoxN.getElement().addClassName(CssBundle.INSTANCE.css().mappingCell());
        valueBoxN.setLabel("V" + counter);

        FocusPanel resetBtnN = newResetBtn(counter);
        FocusPanel deleteRowBtnN = newDeleteRowBtn(counter);

        deleteRowBtnN.addClickHandler(new ClickHandler()
        {
            @Override
            public void onClick(ClickEvent event)
            {
                pairN.removeFromParent();
            }
        });

        resetBtnN.addClickHandler(new ClickHandler()
        {
            @Override
            public void onClick(ClickEvent event)
            {
                valueBoxN.clear();
                keyBoxN.clear();
                resetBtnN.getElement().addClassName(CssBundle.INSTANCE.css().hidden());
            }
        });

        valueBoxN.addKeyUpHandler(new KeyUpHandler()
        {
            @Override
            public void onKeyUp(KeyUpEvent event)
            {
                resetBtnN.getElement().removeClassName(CssBundle.INSTANCE.css().hidden());
            }
        });

        keyBoxN.addKeyUpHandler(new KeyUpHandler()
        {
            @Override
            public void onKeyUp(KeyUpEvent event)
            {
                resetBtnN.getElement().removeClassName(CssBundle.INSTANCE.css().hidden());
            }
        });

        valueBoxN.add(resetBtnN);

        keyBoxN.setClass("outlined col-5");
        keyBoxN.getElement().addClassName(CssBundle.INSTANCE.css().mappingCell());
        keyBoxN.setLabel("K" + counter);

        pairN.getElement().addClassName(CssBundle.INSTANCE.css().mappingTablePair());
        pairN.add(keyBoxN);
        pairN.add(valueBoxN);
        pairN.add(deleteRowBtnN);

        counter++;
        return pairN;
    }

    private FocusPanel newDeleteRowBtn(int id)
    {
        FocusPanel deleteRowBtn = new FocusPanel();
        deleteRowBtn.getElement().setInnerHTML("<i></i>");
        deleteRowBtn.getElement().addClassName(CssBundle.INSTANCE.css().deleteRowBtn());
        deleteRowBtn.getElement().setId("delete_row_" + id);
        return deleteRowBtn;
    }

    private FocusPanel newResetBtn(int id)
    {
        FocusPanel resetBtn = new FocusPanel();
        resetBtn.getElement().setInnerHTML("<i></i><span></span>");
        resetBtn.getElement().addClassName(CssBundle.INSTANCE.css().resetBtn());
        resetBtn.getElement().addClassName(CssBundle.INSTANCE.css().hidden());
        resetBtn.getElement().setId("reset_btn_" + id);

        return resetBtn;
    }

    @Override
    public void setNodeDescription(String description)
    {
        label.setText(description);
    }

    @Override
    public HasClickHandlers addElementClickHandler()
    {
        return new HasClickHandlers()
        {

            @Override
            public void fireEvent(GwtEvent<?> event)
            {
                // TODO Auto-generated method stub

            }

            @Override
            public HandlerRegistration addClickHandler(ClickHandler handler)
            {
                return new HandlerRegistration()
                {
                    @Override
                    public void removeHandler()
                    {
                    }
                };
            }
        };
    }

    @Override
    public MapNodeDisplay createChildNode()
    {
        // TODO Auto-generated method stub
        return null;
    }

}

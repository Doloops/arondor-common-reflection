package com.arondor.common.reflection.gwt.client.nview;

import com.arondor.common.reflection.gwt.client.CssBundle;
import com.arondor.common.reflection.gwt.client.presenter.fields.MapTreeNodePresenter.MapPairDisplay;
import com.arondor.common.reflection.gwt.client.presenter.fields.MapTreeNodePresenter.MapRootDisplay;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FocusPanel;

import gwt.material.design.client.ui.MaterialCard;
import gwt.material.design.client.ui.MaterialLabel;

public class NMapNodeView extends NNodeView implements MapRootDisplay
{
    private final FlowPanel header = new FlowPanel();

    protected final MaterialCard mappingTable = new MaterialCard();

    private final MaterialLabel label = new MaterialLabel();

    private final FocusPanel newPairBtn = new FocusPanel();

    public NMapNodeView()
    {
        getElement().addClassName(CssBundle.INSTANCE.css().mappingField());

        header.getElement().setAttribute("style", "display:flex;position:relative;");

        label.setStyle(
                "color:#9E9E9E;font-size:14px;font-family: Arial Unicode MS, Arial, sans-serif;padding-right:30px;");

        newPairBtn.getElement().setInnerHTML("<i></i>");
        newPairBtn.getElement().addClassName(CssBundle.INSTANCE.css().newPairBtn());

        mappingTable.getElement().addClassName(CssBundle.INSTANCE.css().mappingTable());
        mappingTable.getElement().addClassName("container");

        header.add(label);
        header.add(newPairBtn);

        attachElements();
    }

    private void attachElements()
    {
        add(header);
        add(mappingTable);
    }

    @Override
    public void clear()
    {
        super.clear();
        mappingTable.clear();
        attachElements();
    }

    @Override
    public void setNodeDescription(String description)
    {
        label.setText(description);
    }

    @Override
    public HasClickHandlers addElementClickHandler()
    {
        return newPairBtn;
    }

    @Override
    public MapPairDisplay createPair(String keyClass, String valueClass)
    {
        if (keyClass.equals(String.class.getName()) && valueClass.equals(String.class.getName()))
        {
            MapPairDisplay newPair = new NMapPairViewString(keyClass, valueClass);
            mappingTable.add(newPair.asWidget());
            return newPair;
        }
        else if (keyClass.equals(String.class.getName()))
        {
            MapPairDisplay newPair = new NClassNodeViewWithKey(keyClass, valueClass);
            mappingTable.add(newPair.asWidget());
            return newPair;
        }
        throw new RuntimeException("Not supported : pair key=" + keyClass + ", value=" + valueClass);
    }

}

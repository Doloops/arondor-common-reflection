package com.arondor.common.reflection.gwt.client.nview.prim;

import com.arondor.common.reflection.gwt.client.CssBundle;
import com.arondor.common.reflection.gwt.client.event.MyValueChangeEvent;
import com.arondor.common.reflection.gwt.client.nview.NNodeView;
import com.arondor.common.reflection.gwt.client.presenter.fields.PrimitiveTreeNodePresenter.PrimitiveDisplay;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.FlowPanel;

import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialSwitch;

public class NBooleanView extends NNodeView implements PrimitiveDisplay
{
    // private final MaterialCheckBox checkBox = new MaterialCheckBox();

    private final MaterialSwitch checkBox = new MaterialSwitch();

    private FlowPanel groupPanel = new FlowPanel();

    private boolean defaultValue = false;

    public NBooleanView()
    {
        getElement().addClassName(CssBundle.INSTANCE.css().booleanField());

        checkBox.getSpan().setStyle("zoom:0.8;");
        checkBox.getSpan().setMarginLeft(6);
        checkBox.setTextAlign(TextAlign.LEFT);
        checkBox.getLabel().setStyle("align-items:center");
        checkBox.getOnLabel().getElement().addClassName(CssBundle.INSTANCE.css().description());

        groupPanel.getElement().addClassName("input-group");
        groupPanel.getElement().setAttribute("style", "margin:0px");

        attachElements();

        attachHandlers();
    }

    private void attachElements()
    {
        setDefaultValue(String.valueOf(defaultValue));

        groupPanel.add(getResetFieldBtn());
        groupPanel.add(checkBox);

        add(groupPanel);
    }

    private void attachHandlers()
    {
        getResetFieldBtn().addClickHandler(new ClickHandler()
        {
            @Override
            public void onClick(ClickEvent event)
            {
                setDefaultValue(String.valueOf(defaultValue));
            }
        });
    }

    @Override
    public void setNodeDescription(String label)
    {
        checkBox.setOnLabel(label);
    }

    @Override
    public void setValue(String value)
    {
        boolean booleanValue = Boolean.parseBoolean(value);
        toggleActive(booleanValue != defaultValue);
        doSetValue(booleanValue);
    }

    private void doSetValue(boolean value)
    {
        checkBox.setValue(value);
    }

    private void toggleActive(boolean active)
    {
        setActive(active);
        if (active)
        {
            checkBox.setStyle("padding-right:30px");
        }
        else
        {
            checkBox.setStyle("padding-right:12px");
        }
    }

    @Override
    public void setDefaultValue(String value)
    {
        defaultValue = Boolean.parseBoolean(value);
        doSetValue(defaultValue);
        toggleActive(false);
    }

    @Override
    public HandlerRegistration addValueChangeHandler(final ValueChangeHandler<String> valueChangeHandler)
    {
        return checkBox.addValueChangeHandler(new ValueChangeHandler<Boolean>()
        {
            @Override
            public void onValueChange(ValueChangeEvent<Boolean> event)
            {
                Boolean newValue = event.getValue();
                toggleActive(defaultValue != newValue.booleanValue());
                valueChangeHandler.onValueChange(new MyValueChangeEvent<String>(newValue.toString()));
            }
        });
    }

    @Override
    public void setNodeLongDescription(String longDescription)
    {
        checkBox.setTooltip(longDescription);
    }

    @Override
    public void setPlaceholder(String value)
    {
        // required for string fields
    }

}

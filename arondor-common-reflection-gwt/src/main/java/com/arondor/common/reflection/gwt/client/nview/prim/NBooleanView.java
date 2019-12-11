package com.arondor.common.reflection.gwt.client.nview.prim;

import com.arondor.common.reflection.gwt.client.CssBundle;
import com.arondor.common.reflection.gwt.client.nview.NNodeView;
import com.arondor.common.reflection.gwt.client.presenter.fields.PrimitiveTreeNodePresenter.PrimitiveDisplay;
import com.arondor.common.reflection.gwt.client.view.MyValueChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.CheckBox;

public class NBooleanView extends NNodeView implements PrimitiveDisplay
{
    private final CheckBox valueBox = new CheckBox("");

    private boolean defaultValue = false;

    private String rnd = "";

    public NBooleanView()
    {
        getElement().addClassName(CssBundle.INSTANCE.css().booleanField());
        rnd = String.valueOf(Math.random()).substring(2);

        getResetFieldBtn().addClickHandler(new ClickHandler()
        {

            @Override
            public void onClick(ClickEvent event)
            {
                setDefaultValue(String.valueOf(defaultValue));
            }
        });

        valueBox.getElement().getElementsByTagName("input").getItem(0).setId(rnd);
        valueBox.getElement().getElementsByTagName("label").getItem(0).setAttribute("for", rnd);

        add(valueBox);
    }

    @Override
    public void setNodeDescription(String description)
    {
        getNodeNamePanel().getElement().setInnerHTML("<label for=\"" + rnd + "\">" + description + "</label>");
    }

    @Override
    public void setValue(String value)
    {
        boolean booleanValue = Boolean.parseBoolean(value);
        setActive(booleanValue != defaultValue);
        doSetValue(booleanValue);
    }

    private void doSetValue(boolean value)
    {
        valueBox.setValue(value);
    }

    @Override
    public void setDefaultValue(String value)
    {
        defaultValue = Boolean.parseBoolean(value);
        doSetValue(defaultValue);
        setActive(false);
    }

    @Override
    public HandlerRegistration addValueChangeHandler(final ValueChangeHandler<String> valueChangeHandler)
    {
        return valueBox.addValueChangeHandler(new ValueChangeHandler<Boolean>()
        {
            @Override
            public void onValueChange(ValueChangeEvent<Boolean> event)
            {
                Boolean newValue = event.getValue();
                setActive(defaultValue != newValue.booleanValue());
                valueChangeHandler.onValueChange(new MyValueChangeEvent<String>(newValue.toString()));
            }
        });
    }

    @Override
    public void setPlaceholder(String value)
    {
        // required for string fields
    }
}

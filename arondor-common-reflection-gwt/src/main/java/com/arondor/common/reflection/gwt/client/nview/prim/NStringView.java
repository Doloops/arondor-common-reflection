package com.arondor.common.reflection.gwt.client.nview.prim;

import com.arondor.common.reflection.gwt.client.CssBundle;
import com.arondor.common.reflection.gwt.client.nview.NNodeView;
import com.arondor.common.reflection.gwt.client.presenter.fields.PrimitiveTreeNodePresenter.PrimitiveDisplay;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.TextBox;

public class NStringView extends NNodeView implements PrimitiveDisplay
{
    protected final TextBox textBox = new TextBox();

    private FlowPanel resetFieldBtn = new FlowPanel();

    private FlowPanel inputGroupPanel = new FlowPanel();

    private String defaultValue = "";

    private String placeholder = "";

    public NStringView()
    {
        getElement().addClassName(CssBundle.INSTANCE.css().stringField());
        // getElement().addClassName("input-group");
        // valueBox.getElement().setClassName("form-control");

        resetFieldBtn.getElement().addClassName("input-group-append");
        resetFieldBtn.getElement().addClassName(CssBundle.INSTANCE.css().resetFieldBtn());
        resetFieldBtn.getElement()
                .setInnerHTML("<span class=\"input-group-text\"><i class=\"fa fa-trash\"></i></span>");
        textBox.getElement().addClassName("form-control");

        inputGroupPanel.getElement().addClassName("input-group");
        inputGroupPanel.add(textBox);
        inputGroupPanel.add(resetFieldBtn);

        add(inputGroupPanel);
    }

    protected TextBox getTextBox()
    {
        return textBox;
    }

    @Override
    public void setValue(String value)
    {
        textBox.setValue(value);
    }

    @Override
    public void setDefaultValue(String value)
    {
        setValue(value);
    }

    @Override
    public void setPlaceholder(String placeholder)
    {
        textBox.getElement().setAttribute("placeholder", placeholder);
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<String> valueChangeHandler)
    {
        // return textBox.addValueChangeHandler(new
        // ValueChangeHandler<Boolean>()
        // {
        // @Override
        // public void onValueChange(ValueChangeEvent<Boolean> event)
        // {
        // Boolean newValue = event.getValue();
        // setActive(textBox.getValue().contains(defaultValue);
        // valueChangeHandler.onValueChange(new
        // MyValueChangeEvent<String>(newValue.toString()));
        // }
        // });
        return null;
    }

}

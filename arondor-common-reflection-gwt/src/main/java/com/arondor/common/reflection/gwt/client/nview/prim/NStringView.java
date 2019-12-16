package com.arondor.common.reflection.gwt.client.nview.prim;

import com.arondor.common.reflection.gwt.client.CssBundle;
import com.arondor.common.reflection.gwt.client.nview.NNodeView;
import com.arondor.common.reflection.gwt.client.presenter.fields.PrimitiveTreeNodePresenter.PrimitiveDisplay;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.dom.client.MouseWheelEvent;
import com.google.gwt.event.dom.client.MouseWheelHandler;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.TextBox;

public class NStringView extends NNodeView implements PrimitiveDisplay
{
    protected final TextBox textBox = new TextBox();

    private FlowPanel inputGroupPanel = new FlowPanel();

    private String defaultValue = "";

    public NStringView()
    {
        getElement().addClassName(CssBundle.INSTANCE.css().stringField());

        getResetFieldBtn().getElement().addClassName("input-group-append");
        getResetFieldBtn().getElement().addClassName(CssBundle.INSTANCE.css().resetFieldBtn());
        getResetFieldBtn().getElement().setInnerHTML("<span class=\"input-group-text\"><i></i></span>");

        getResetFieldBtn().addClickHandler(new ClickHandler()
        {

            @Override
            public void onClick(ClickEvent event)
            {
                setDefaultValue(defaultValue);
            }
        });

        textBox.getElement().addClassName("form-control");
        inputGroupPanel.getElement().addClassName("input-group");
        inputGroupPanel.getElement().setAttribute("style", "margin:0px");
        inputGroupPanel.add(textBox);
        inputGroupPanel.add(getResetFieldBtn());

        add(inputGroupPanel);

        bind();
    }

    private void bind()
    {
        textBox.addKeyUpHandler(new KeyUpHandler()
        {
            @Override
            public void onKeyUp(KeyUpEvent event)
            {
                checkActive();
            }
        });

        textBox.addChangeHandler(new ChangeHandler()
        {

            @Override
            public void onChange(ChangeEvent event)
            {
                checkActive();
            }
        });

        textBox.addMouseWheelHandler(new MouseWheelHandler()
        {

            @Override
            public void onMouseWheel(MouseWheelEvent event)
            {
                checkActive();
            }

        });
    }

    private void checkActive()
    {
        String input = textBox.getValue();
        setActive(!input.equals(defaultValue));
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
        this.defaultValue = value;
        setValue(value);
        setActive(false);
    }

    @Override
    public void setPlaceholder(String placeholder)
    {
        textBox.getElement().setAttribute("placeholder", placeholder);
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<String> valueChangeHandler)
    {
        return textBox.addValueChangeHandler(valueChangeHandler);
    }
}

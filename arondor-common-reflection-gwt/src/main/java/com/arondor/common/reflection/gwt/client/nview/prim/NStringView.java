package com.arondor.common.reflection.gwt.client.nview.prim;

import com.arondor.common.reflection.gwt.client.CssBundle;
import com.arondor.common.reflection.gwt.client.nview.NNodeView;
import com.arondor.common.reflection.gwt.client.presenter.fields.PrimitiveTreeNodePresenter.PrimitiveDisplay;
import com.google.gwt.dom.client.Element;
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

import gwt.material.design.client.ui.MaterialTextBox;

public class NStringView extends NNodeView implements PrimitiveDisplay
{
    protected final MaterialTextBox textBox = new MaterialTextBox();

    private FlowPanel inputGroupPanel = new FlowPanel();

    private String defaultValue = "";

    public NStringView()
    {
        getElement().addClassName(CssBundle.INSTANCE.css().stringField());

        textBox.getElement().addClassName("outlined");
        textBox.getElement().setAttribute("style", "width:100%;display:flex;");

        textBox.getElement().getElementsByTagName("input").getItem(0).setAttribute("style", "padding-right:12px");

        inputGroupPanel.getElement().addClassName("input-group");
        inputGroupPanel.getElement().setAttribute("style", "margin:0px");

        attachElements();

        bind();
    }

    private void attachElements()
    {
        inputGroupPanel.add(textBox);
        inputGroupPanel.add(getResetFieldBtn());

        add(inputGroupPanel);
    }

    @Override
    public void setProperLabel(String label)
    {
        textBox.getElement().getElementsByTagName("label").getItem(0).setInnerHTML(label);
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

        getResetFieldBtn().addClickHandler(new ClickHandler()
        {

            @Override
            public void onClick(ClickEvent event)
            {
                setDefaultValue(defaultValue);
                Element input = textBox.getElement().getElementsByTagName("input").getItem(0);
                input.focus();
                input.setAttribute("style", "padding-right:12px");
            }
        });
    }

    private void checkActive()
    {
        String input = textBox.getValue();
        boolean active = !input.equals(defaultValue);
        if (active)
        {
            textBox.getElement().getElementsByTagName("input").getItem(0).setAttribute("style", "padding-right:30px");
        }
        setActive(active);
    }

    protected MaterialTextBox getTextBox()
    {
        return textBox;
    }

    @Override
    public void setValue(String value)
    {
        textBox.setValue(value);
        checkActive();
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

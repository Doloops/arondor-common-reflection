package com.arondor.common.reflection.gwt.client.nview.prim;

import com.arondor.common.reflection.gwt.client.CssBundle;
import com.arondor.common.reflection.gwt.client.nview.NNodeView;
import com.arondor.common.reflection.gwt.client.presenter.fields.PrimitiveTreeNodePresenter.PrimitiveDisplay;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.dom.client.MouseWheelEvent;
import com.google.gwt.event.dom.client.MouseWheelHandler;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.FlowPanel;

import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialTextBox;

public class NStringView extends NNodeView implements PrimitiveDisplay
{
    protected final MaterialTextBox textBox = new MaterialTextBox();

    private FlowPanel inputGroupPanel = new FlowPanel();

    private String defaultValue, defaultPlaceholder, helperText = "";

    public NStringView()
    {
        getElement().addClassName(CssBundle.INSTANCE.css().stringField());

        textBox.setClass("outlined");
        textBox.setStyle("width:100%;");

        textBox.getElement().getElementsByTagName("input").getItem(0).setAttribute("style", "padding-right:12px;");

        textBox.setTextAlign(TextAlign.LEFT);

        inputGroupPanel.getElement().addClassName("input-group");
        inputGroupPanel.getElement().setAttribute("style", "margin:0px");

        attachElements();

        attachHandlers();
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
        textBox.setLabel(label);
    }

    private void attachHandlers()
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

        textBox.addFocusHandler(new FocusHandler()
        {

            @Override
            public void onFocus(FocusEvent event)
            {
                textBox.setPlaceholder(defaultPlaceholder);

                textBox.setHelperText(helperText);
                textBox.getElement().getElementsByTagName("span").getItem(0)
                        .addClassName(CssBundle.INSTANCE.css().helperText());
            }
        });

        textBox.addBlurHandler(new BlurHandler()
        {

            @Override
            public void onBlur(BlurEvent event)
            {
                if (textBox.getValue() == null || textBox.getValue().trim() == "")
                {
                    textBox.getValueBoxBase().getElement().removeAttribute("placeholder");
                    textBox.getLabel().getElement().removeClassName("active");
                }
                textBox.clearHelperText();
            }
        });

        getResetFieldBtn().addClickHandler(new ClickHandler()
        {

            @Override
            public void onClick(ClickEvent event)
            {
                setDefaultValue(defaultValue);
                textBox.setFocus(true);
                checkActive();
            }
        });
    }

    private void checkActive()
    {
        String input = textBox.getValue();
        boolean active = !input.equals(defaultValue);

        setInputStyle(active ? "padding-right:30px" : "padding-right:12px");

        setActive(active);
    }

    private void setInputStyle(String style)
    {
        textBox.getElement().getElementsByTagName("input").getItem(0).setAttribute("style", style);
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
        this.defaultPlaceholder = placeholder;
    }

    @Override
    public void setNodeLongDescription(String longDescription)
    {
        helperText = longDescription;
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<String> valueChangeHandler)
    {
        return textBox.addValueChangeHandler(valueChangeHandler);
    }
}

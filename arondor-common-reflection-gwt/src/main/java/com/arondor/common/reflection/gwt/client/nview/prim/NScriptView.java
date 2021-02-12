package com.arondor.common.reflection.gwt.client.nview.prim;

import java.util.logging.Logger;

import com.arondor.common.reflection.gwt.client.CssBundle;
import com.arondor.common.reflection.gwt.client.nview.NNodeView;
import com.arondor.common.reflection.gwt.client.presenter.fields.PrimitiveTreeNodePresenter.PrimitiveDisplay;
import com.arondor.common.reflection.gwt.client.view.ImplementingClassView;
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
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.FlowPanel;

import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialTextArea;

public class NScriptView extends NNodeView implements PrimitiveDisplay
{
    private static final Logger LOG = Logger.getLogger(ImplementingClassView.class.getName());

    protected final MaterialTextArea textArea = new MaterialTextArea();

    private FlowPanel inputGroupPanel = new FlowPanel();

    private String defaultValue = "";

    private String defaultPlaceholder = "";

    public NScriptView()
    {
        getElement().addClassName(CssBundle.INSTANCE.css().stringField());

        textArea.setClass("outlined");
        textArea.setStyle("width:100%;height:fit-content");
        textArea.setTextAlign(TextAlign.LEFT);

        inputGroupPanel.getElement().addClassName("input-group");
        inputGroupPanel.getElement().setAttribute("style", "margin:0px");

        attachElements();

        attachHandlers();
    }

    private void attachElements()
    {
        inputGroupPanel.add(textArea);
        inputGroupPanel.add(getResetFieldBtn());

        add(inputGroupPanel);
    }

    @Override
    public void setNodeDescription(String label)
    {
        textArea.setLabel(label);
    }

    private void attachHandlers()
    {
        textArea.addKeyUpHandler(new KeyUpHandler()
        {
            @Override
            public void onKeyUp(KeyUpEvent event)
            {
                checkActive();
            }
        });

        textArea.addChangeHandler(new ChangeHandler()
        {

            @Override
            public void onChange(ChangeEvent event)
            {
                checkActive();
            }
        });

        textArea.addFocusHandler(new FocusHandler()
        {

            @Override
            public void onFocus(FocusEvent event)
            {
                textArea.setPlaceholder(String.join("\n", defaultPlaceholder));
                textArea.setHelperText(getHelperTextContent());
            }
        });

        textArea.addBlurHandler(new BlurHandler()
        {

            @Override
            public void onBlur(BlurEvent event)
            {
                boolean contentIsEmpty = textArea.getValue().isEmpty();
                boolean contentIsUnchanged = textArea.getValue().equals(defaultValue);

                if (contentIsEmpty && contentIsUnchanged)
                {
                    textArea.getValueBoxBase().getElement().removeAttribute("placeholder");
                    textArea.getLabel().getElement().removeClassName("active");
                }
                textArea.clearHelperText();
            }
        });

        getResetFieldBtn().addClickHandler(new ClickHandler()
        {
            @Override
            public void onClick(ClickEvent event)
            {
                setDefaultValue(defaultValue);
                textArea.setFocus(true);
                textArea.triggerAutoResize();
            }
        });
    }

    private void checkActive()
    {
        String input = textArea.getValue();
        boolean active = !input.equals(defaultValue);

        setActive(active);
    }

    @Override
    public void setValue(String value)
    {
        textArea.setValue(value);
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
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<String> valueChangeHandler)
    {
        return textArea.addValueChangeHandler(valueChangeHandler);
    }
}

package com.arondor.common.reflection.gwt.client.nview.prim;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.arondor.common.reflection.gwt.client.CssBundle;
import com.arondor.common.reflection.gwt.client.nview.NNodeView;
import com.arondor.common.reflection.gwt.client.presenter.fields.StringListTreeNodePresenter.StringListDisplay;
import com.arondor.common.reflection.gwt.client.view.MyValueChangeEvent;
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
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.FlowPanel;

import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialTextArea;

public class NStringListView extends NNodeView implements StringListDisplay
{
    protected final MaterialTextArea textArea = new MaterialTextArea();

    private FlowPanel inputGroupPanel = new FlowPanel();

    private List<String> defaultValue = Arrays.asList("");

    private List<String> defaultPlaceholder = Arrays.asList("value A", "value B");

    public NStringListView()
    {
        getElement().addClassName(CssBundle.INSTANCE.css().stringField());

        textArea.setClass("outlined");
        textArea.setStyle("width:100%;height:fit-content");
        textArea.setTextAlign(TextAlign.LEFT);

        inputGroupPanel.getElement().addClassName("input-group");
        inputGroupPanel.getElement().setAttribute("style", "margin:0px");

        // setDefaultValue(Arrays.asList("a", "b", "c"));
        // setPlaceholder(Arrays.asList("a", "b"));

        attachElements();

        attachHandlers();
    }

    private void attachElements()
    {
        inputGroupPanel.add(textArea);
        inputGroupPanel.add(getResetFieldBtn());

        add(inputGroupPanel);
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
                boolean contentIsUnchanged = textArea.getValue().equals(String.join("\n", defaultValue));

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
        List<String> input = Arrays.asList(textArea.getValue().trim().split("\n"));
        boolean active = !input.equals(defaultValue);
        setActive(active);
    }

    @Override
    public void setValue(List<String> values)
    {
        textArea.setValue(String.join("\n", values));
        setActive(true);
        textArea.triggerAutoResize();
    }

    @Override
    public void setDefaultValue(List<String> value)
    {
        this.defaultValue = value;
        setValue(this.defaultValue);
        setActive(false);
    }

    @Override
    public void setProperLabel(String label)
    {
        textArea.setLabel(label);
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<List<String>> valueChangeHandler)
    {
        return textArea.addValueChangeHandler(new ValueChangeHandler<String>()
        {
            @Override
            public void onValueChange(ValueChangeEvent<String> event)
            {
                String[] splitted = event.getValue().split("\n");
                List<String> values = new ArrayList<String>();
                for (String value : splitted)
                {
                    values.add(value);
                }
                valueChangeHandler.onValueChange(new MyValueChangeEvent<List<String>>(values));
            }
        });
    }

    @Override
    public void setPlaceholder(List<String> values)
    {
        defaultPlaceholder = values;
    }

    @Override
    public void clear()
    {
        super.clear();
        attachElements();
    }

}

package com.arondor.common.reflection.gwt.client.nview.prim;

import java.util.ArrayList;
import java.util.List;

import com.arondor.common.reflection.gwt.client.CssBundle;
import com.arondor.common.reflection.gwt.client.nview.NNodeView;
import com.arondor.common.reflection.gwt.client.presenter.fields.StringListTreeNodePresenter.StringListDisplay;
import com.arondor.common.reflection.gwt.client.view.MyValueChangeEvent;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.TextArea;

public class NStringListView extends NNodeView implements StringListDisplay
{
    protected final TextArea textArea = new TextArea();

    private FlowPanel inputGroupPanel = new FlowPanel();

    private List<String> defaultValue = new ArrayList<String>();

    public NStringListView()
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

        textArea.getElement().setAttribute("placeholder", "value A\nvalue B");

        textArea.getElement().addClassName("form-control");
        inputGroupPanel.getElement().addClassName("input-group");

        // setDefaultValue(Arrays.asList("a", "b", "c"));

        bind();

        attachHandlers();
    }

    private void bind()
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
    }

    private void checkActive()
    {
        String input = textArea.getValue();
        setActive(!input.equals(defaultValue.toString()));
    }

    @Override
    public void setValue(List<String> values)
    {
        textArea.setValue(String.join("\n", values));
        setActive(true);
    }

    @Override
    public void setDefaultValue(List<String> value)
    {
        this.defaultValue = value;
        setValue(value);
        setActive(false);
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
    public void setPlaceholder(List<String> value)
    {
        textArea.getElement().setAttribute("placeholder", String.join("\n", value));
    }

    @Override
    public void clear()
    {
        super.clear();
        bind();
    }
}

package com.arondor.common.reflection.gwt.client.nview.prim;

import java.util.List;

import com.arondor.common.reflection.gwt.client.CssBundle;
import com.arondor.common.reflection.gwt.client.nview.NNodeView;
import com.arondor.common.reflection.gwt.client.presenter.fields.StringListTreeNodePresenter.StringListDisplay;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.TextArea;

public class NStringListView extends NNodeView implements StringListDisplay
{
    protected final TextArea textArea = new TextArea();

    private String defaultValue = "";

    public NStringListView()
    {
        getElement().addClassName(CssBundle.INSTANCE.css().stringField());

        addChildren();

        bind();
    }

    private void addChildren()
    {
        add(textArea);
    }

    private void bind()
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
        setActive(!input.equals(defaultValue));
    }

    @Override
    public void setValue(List<String> values)
    {
        StringBuilder stringBuilder = new StringBuilder();
        for (String value : values)
        {
            stringBuilder.append(value);
            stringBuilder.append('\n');
        }
        textArea.setValue(stringBuilder.toString());
        setActive(true);
    }

    @Override
    public void setDefaultValue(List<String> value)
    {
        setValue(value);
        setActive(false);
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<List<String>> valueChangeHandler)
    {
        return null;
    }

    @Override
    public void setPlaceholder(List<String> value)
    {
        textArea.getElement().setAttribute("placeholder", value.get(0));
    }

    @Override
    public void clear()
    {
        super.clear();
        addChildren();
    }
}

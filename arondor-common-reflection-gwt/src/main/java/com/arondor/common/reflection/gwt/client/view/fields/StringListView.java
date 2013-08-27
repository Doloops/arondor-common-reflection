package com.arondor.common.reflection.gwt.client.view.fields;

import java.util.ArrayList;
import java.util.List;

import com.arondor.common.reflection.gwt.client.presenter.fields.StringListTreeNodePresenter.StringListDisplay;
import com.arondor.common.reflection.gwt.client.view.AbstractTreeNodeView;
import com.arondor.common.reflection.gwt.client.view.MyValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.UIObject;

public class StringListView extends AbstractTreeNodeView implements StringListDisplay
{
    private final TextArea textArea = new TextArea();

    public StringListView(UIObject parentNode)
    {
        super(parentNode);
        setHasRemoveButton(true);
        getContents().add(textArea);
        textArea.setWidth("400px");
    }

    public void clear()
    {
        textArea.setValue("");
    }

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

    public HandlerRegistration addValueChangeHandler(final ValueChangeHandler<List<String>> valueChangeHandler)
    {
        return textArea.addValueChangeHandler(new ValueChangeHandler<String>()
        {
            public void onValueChange(ValueChangeEvent<String> event)
            {
                setActive(true);
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

}

package com.arondor.common.reflection.gwt.client.view;

import com.arondor.common.reflection.gwt.client.presenter.PrimitiveTreeNodePresenter.PrimitiveDisplay;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.UIObject;

public class PrimitiveBooleanView extends AbstractTreeNodeView implements PrimitiveDisplay
{
    private final CheckBox checkBox = new CheckBox();

    protected PrimitiveBooleanView(UIObject parentNode)
    {
        super(parentNode);
        getContents().add(checkBox);
    }

    public void clear()
    {
        checkBox.setValue(false);
    }

    public void setValue(String value)
    {
        checkBox.setValue(Boolean.parseBoolean(value));
    }

    public HandlerRegistration addValueChangeHandler(final ValueChangeHandler<String> valueChangeHandler)
    {
        return checkBox.addValueChangeHandler(new ValueChangeHandler<Boolean>()
        {

            public void onValueChange(ValueChangeEvent<Boolean> event)
            {
                valueChangeHandler.onValueChange(new StringValueChangeEvent(event.getValue().toString()));
            }
        });
    }
}

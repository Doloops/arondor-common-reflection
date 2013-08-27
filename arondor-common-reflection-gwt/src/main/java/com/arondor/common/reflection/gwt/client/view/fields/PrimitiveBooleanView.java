package com.arondor.common.reflection.gwt.client.view.fields;

import com.arondor.common.reflection.gwt.client.presenter.fields.PrimitiveTreeNodePresenter.PrimitiveDisplay;
import com.arondor.common.reflection.gwt.client.view.AbstractTreeNodeView;
import com.arondor.common.reflection.gwt.client.view.MyValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.UIObject;

public class PrimitiveBooleanView extends AbstractTreeNodeView implements PrimitiveDisplay
{
    private final CheckBox checkBox = new CheckBox();

    public PrimitiveBooleanView(UIObject parentNode)
    {
        super(parentNode);
        setHasRemoveButton(true);
        getContents().add(checkBox);
    }

    public void clear()
    {
        checkBox.setValue(false);
    }

    public void setValue(String value)
    {
        checkBox.setValue(Boolean.parseBoolean(value));
        setActive(true);
    }

    public HandlerRegistration addValueChangeHandler(final ValueChangeHandler<String> valueChangeHandler)
    {
        return checkBox.addValueChangeHandler(new ValueChangeHandler<Boolean>()
        {
            public void onValueChange(ValueChangeEvent<Boolean> event)
            {
                setActive(true);
                valueChangeHandler.onValueChange(new MyValueChangeEvent<String>(event.getValue().toString()));
            }
        });
    }
}

package com.arondor.common.reflection.gwt.client.view.fields;

import com.arondor.common.reflection.gwt.client.presenter.fields.PrimitiveTreeNodePresenter;
import com.arondor.common.reflection.gwt.client.view.AbstractTreeNodeView;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.UIObject;

public class PrimitiveStringView extends AbstractTreeNodeView implements PrimitiveTreeNodePresenter.PrimitiveDisplay
{
    private final TextBox textBox = new TextBox();

    public PrimitiveStringView(UIObject parentNode)
    {
        super(parentNode);
        setHasRemoveButton(true);
        getContents().add(textBox.asWidget());
    }

    public void clear()
    {
        textBox.setValue("");
    }

    public void setValue(String value)
    {
        textBox.setValue(value);
        setActive(true);
    }

    public void setTextWidth(int width)
    {
        textBox.setWidth(width + "px");
    }

    public HandlerRegistration addValueChangeHandler(final ValueChangeHandler<String> valueChangeHandler)
    {
        return textBox.addValueChangeHandler(new ValueChangeHandler<String>()
        {
            public void onValueChange(ValueChangeEvent<String> event)
            {
                setActive(true);
                valueChangeHandler.onValueChange(event);
            }
        });
    }

}

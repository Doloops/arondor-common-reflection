package com.arondor.common.reflection.gwt.client.view;

import com.arondor.common.reflection.gwt.client.presenter.PrimitiveTreeNodePresenter;
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
        getContents().add(textBox.asWidget());
    }

    public void clear()
    {
        textBox.setValue("");
    }

    public void setValue(String value)
    {
        textBox.setValue(value);
    }

    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<String> valueChangeHandler)
    {
        return textBox.addValueChangeHandler(valueChangeHandler);
    }

}

package com.arondor.common.reflection.gwt.client.nview.prim;

import com.arondor.common.reflection.gwt.client.nview.NNodeView;
import com.arondor.common.reflection.gwt.client.presenter.fields.PrimitiveTreeNodePresenter.PrimitiveDisplay;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.TextBox;

public class NStringView extends NNodeView implements PrimitiveDisplay
{
    private final TextBox valueBox = new TextBox();

    public NStringView()
    {
        getElement().addClassName("primitiveView");
        add(valueBox);
    }

    @Override
    public void setValue(String value)
    {
        valueBox.setValue(value);
    }

    @Override
    public void setDefaultValue(String value)
    {
        setValue(value);
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<String> valueChangeHandler)
    {
        // TODO Auto-generated method stub
        return null;
    }

}

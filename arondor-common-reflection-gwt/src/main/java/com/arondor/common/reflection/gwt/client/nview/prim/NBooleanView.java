package com.arondor.common.reflection.gwt.client.nview.prim;

import com.arondor.common.reflection.gwt.client.nview.NNodeView;
import com.arondor.common.reflection.gwt.client.presenter.fields.PrimitiveTreeNodePresenter.PrimitiveDisplay;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.CheckBox;

public class NBooleanView extends NNodeView implements PrimitiveDisplay
{
    private final CheckBox valueBox = new CheckBox("");

    public NBooleanView()
    {
        getElement().addClassName("primitiveView");
        add(valueBox);
    }

    @Override
    public void setValue(String value)
    {
        valueBox.setValue(value.equals("true"));
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

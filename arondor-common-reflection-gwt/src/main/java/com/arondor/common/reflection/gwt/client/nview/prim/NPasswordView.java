package com.arondor.common.reflection.gwt.client.nview.prim;

import com.arondor.common.reflection.gwt.client.event.MyValueChangeEvent;
import com.arondor.common.reflection.util.PasswordCipher;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;

import gwt.material.design.client.constants.InputType;

public class NPasswordView extends NStringView
{
    private static final PasswordCipher CIPHER = new PasswordCipher();

    public NPasswordView()
    {
        textBox.setType(InputType.PASSWORD);
    }

    @Override
    public void setValue(String value)
    {
        String decoded = CIPHER.decode(value);
        super.setValue(decoded);
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<String> valueChangeHandler)
    {
        return super.addValueChangeHandler(new ValueChangeHandler<String>()
        {
            @Override
            public void onValueChange(ValueChangeEvent<String> event)
            {
                String raw = event.getValue();
                String encoded = CIPHER.encode(raw);
                valueChangeHandler.onValueChange(new MyValueChangeEvent<String>(encoded));
            }
        });
    }

}

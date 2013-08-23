package com.arondor.common.reflection.gwt.client.view;

import com.google.gwt.event.logical.shared.ValueChangeEvent;

class StringValueChangeEvent extends ValueChangeEvent<String>
{
    public StringValueChangeEvent(String value)
    {
        super(value);
    }
}
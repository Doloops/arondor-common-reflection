package com.arondor.common.reflection.gwt.client.view;

import com.google.gwt.event.logical.shared.ValueChangeEvent;

public class MyValueChangeEvent<T> extends ValueChangeEvent<T>
{
    public MyValueChangeEvent(T value)
    {
        super(value);
    }
}
package com.arondor.common.reflection.gwt.client.event;

import com.arondor.common.reflection.model.java.AccessibleClass;
import com.google.gwt.event.shared.GwtEvent;

public class ClassChangeEvent extends GwtEvent<ClassChangeEvent.Handler>
{
    public static final Type<Handler> TYPE = new Type<Handler>();

    private final AccessibleClass accessibleClass;

    private final String reference;

    public static interface Handler extends com.google.gwt.event.shared.EventHandler
    {
        public void onClassChange(ClassChangeEvent event);
    }

    public ClassChangeEvent(AccessibleClass accessibleClass, String reference)
    {
        this.accessibleClass = accessibleClass;
        this.reference = reference;
    }

    public AccessibleClass getAccessibleClass()
    {
        return accessibleClass;
    }

    public String getReference()
    {
        return reference;
    }

    @Override
    protected void dispatch(Handler handler)
    {
        handler.onClassChange(this);
    }

    @Override
    public com.google.gwt.event.shared.GwtEvent.Type<Handler> getAssociatedType()
    {
        return TYPE;
    }
}

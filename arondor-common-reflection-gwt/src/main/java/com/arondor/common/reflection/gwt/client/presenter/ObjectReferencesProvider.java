package com.arondor.common.reflection.gwt.client.presenter;

import java.util.Collection;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ObjectReferencesProvider
{
    void provide(AsyncCallback<Collection<ImplementingClass>> callback);
}

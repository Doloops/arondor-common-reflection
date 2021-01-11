package com.arondor.common.reflection.gwt.client.presenter;

import java.util.Collection;

import com.arondor.common.reflection.model.config.ObjectConfiguration;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ObjectReferencesProvider
{
    void provide(AsyncCallback<Collection<ImplementingClass>> callback);

    void share(ObjectConfiguration objectConfiguration, String name, AsyncCallback<ImplementingClass> callback);

    void forward(String key, AsyncCallback<ImplementingClass> callback);
}

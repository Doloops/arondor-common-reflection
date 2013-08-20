package com.arondor.common.reflection.gwt.client.service;

import java.util.Collection;

import com.arondor.common.reflection.model.java.AccessibleClass;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface GWTReflectionServiceAsync
{
    void getAccessibleClass(String className, AsyncCallback<AccessibleClass> callback);

    void getImplementingAccessibleClasses(String name, AsyncCallback<Collection<AccessibleClass>> callback);
}

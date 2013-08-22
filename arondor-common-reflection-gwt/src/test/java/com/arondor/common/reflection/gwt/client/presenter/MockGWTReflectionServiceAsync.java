package com.arondor.common.reflection.gwt.client.presenter;

import java.rmi.RemoteException;
import java.util.Collection;

import com.arondor.common.reflection.api.service.ReflectionService;
import com.arondor.common.reflection.gwt.client.service.GWTReflectionServiceAsync;
import com.arondor.common.reflection.model.java.AccessibleClass;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class MockGWTReflectionServiceAsync implements GWTReflectionServiceAsync
{
    private final ReflectionService reflectionService;

    public MockGWTReflectionServiceAsync(ReflectionService reflectionService)
    {
        this.reflectionService = reflectionService;
    }

    public void getAccessibleClass(String className, AsyncCallback<AccessibleClass> callback)
    {
        try
        {
            callback.onSuccess(reflectionService.getAccessibleClass(className));
        }
        catch (RemoteException e)
        {
            callback.onFailure(e);
        }

    }

    public void getImplementingAccessibleClasses(String name, AsyncCallback<Collection<AccessibleClass>> callback)
    {
        try
        {
            callback.onSuccess(reflectionService.getImplementingAccessibleClasses(name));
        }
        catch (RemoteException e)
        {
            callback.onFailure(e);
        }
    }

}

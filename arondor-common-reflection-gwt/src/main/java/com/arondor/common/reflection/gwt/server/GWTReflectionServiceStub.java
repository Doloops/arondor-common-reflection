package com.arondor.common.reflection.gwt.server;

import java.rmi.RemoteException;
import java.util.Collection;

import com.arondor.common.reflection.api.service.ReflectionService;
import com.arondor.common.reflection.gwt.client.service.GWTReflectionService;
import com.arondor.common.reflection.model.java.AccessibleClass;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public abstract class GWTReflectionServiceStub extends RemoteServiceServlet implements GWTReflectionService
{
    /**
     * 
     */
    private static final long serialVersionUID = 8761387653627498785L;

    protected abstract ReflectionService getReflectionService();

    public GWTReflectionServiceStub()
    {
    }

    public AccessibleClass getAccessibleClass(String className)
    {
        try
        {
            return getReflectionService().getAccessibleClass(className);
        }
        catch (RemoteException e)
        {
            throw new RuntimeException(e);
        }
    }

    public Collection<AccessibleClass> getImplementingAccessibleClasses(String name)
    {
        try
        {
            return getReflectionService().getImplementingAccessibleClasses(name);
        }
        catch (RemoteException e)
        {
            throw new RuntimeException(e);
        }
    }

}

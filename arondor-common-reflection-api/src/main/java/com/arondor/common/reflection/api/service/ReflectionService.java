package com.arondor.common.reflection.api.service;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Collection;

import com.arondor.common.reflection.model.java.AccessibleClass;

public interface ReflectionService extends Remote
{
    /**
     * 
     * @param clazzName
     * @return
     * @throws RemoteException
     */
    AccessibleClass getAccessibleClass(String clazzName) throws RemoteException;

    Collection<AccessibleClass> getImplementingAccessibleClasses(String name) throws RemoteException;

}

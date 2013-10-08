/*
 *  Copyright 2013, Arondor
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
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

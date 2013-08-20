package com.arondor.common.reflection.gwt.client.service;

import java.util.Collection;

import com.arondor.common.reflection.model.java.AccessibleClass;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("reflectionService")
public interface GWTReflectionService extends RemoteService
{
    AccessibleClass getAccessibleClass(String className);

    Collection<AccessibleClass> getImplementingAccessibleClasses(String name);
}

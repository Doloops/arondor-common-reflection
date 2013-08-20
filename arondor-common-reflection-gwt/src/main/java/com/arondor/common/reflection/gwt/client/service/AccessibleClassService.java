package com.arondor.common.reflection.gwt.client.service;

import com.arondor.common.reflection.model.java.AccessibleClass;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("accessibleClassService")
public interface AccessibleClassService extends RemoteService
{
    AccessibleClass getAccessibleClass(String className);
}

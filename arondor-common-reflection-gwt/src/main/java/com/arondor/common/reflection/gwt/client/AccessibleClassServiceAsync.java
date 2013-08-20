package com.arondor.common.reflection.gwt.client;

import com.arondor.common.reflection.model.java.AccessibleClass;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface AccessibleClassServiceAsync
{

    public void getAccessibleClass(String className, AsyncCallback<AccessibleClass> callback);
}

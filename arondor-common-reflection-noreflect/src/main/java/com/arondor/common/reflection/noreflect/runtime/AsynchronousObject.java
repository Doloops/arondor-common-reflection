package com.arondor.common.reflection.noreflect.runtime;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface AsynchronousObject
{
    void addCallback(AsyncCallback<Object> callback);
}

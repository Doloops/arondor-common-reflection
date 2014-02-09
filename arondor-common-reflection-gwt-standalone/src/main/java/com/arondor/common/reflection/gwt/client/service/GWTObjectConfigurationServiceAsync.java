package com.arondor.common.reflection.gwt.client.service;

import com.arondor.common.reflection.model.config.ObjectConfigurationMap;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface GWTObjectConfigurationServiceAsync
{

    void getObjectConfigurationMap(String context, AsyncCallback<ObjectConfigurationMap> callback);

}

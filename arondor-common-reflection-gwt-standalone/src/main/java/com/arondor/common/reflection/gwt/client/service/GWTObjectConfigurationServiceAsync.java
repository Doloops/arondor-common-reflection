package com.arondor.common.reflection.gwt.client.service;

import java.util.List;

import com.arondor.common.reflection.model.config.ObjectConfigurationMap;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface GWTObjectConfigurationServiceAsync
{
    void loadLib(String context, List<String> packagePrefixes, AsyncCallback<Void> callback);

    void getObjectConfigurationMap(String context, AsyncCallback<ObjectConfigurationMap> callback);
}

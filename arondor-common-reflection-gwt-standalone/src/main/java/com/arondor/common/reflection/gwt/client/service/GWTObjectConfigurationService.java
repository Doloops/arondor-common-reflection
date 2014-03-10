package com.arondor.common.reflection.gwt.client.service;

import java.util.List;

import com.arondor.common.reflection.model.config.ObjectConfigurationMap;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("objectConfigurationService")
public interface GWTObjectConfigurationService extends RemoteService
{
    void loadLib(String context, List<String> packagePrefixes);

    ObjectConfigurationMap getObjectConfigurationMap(String context);
}

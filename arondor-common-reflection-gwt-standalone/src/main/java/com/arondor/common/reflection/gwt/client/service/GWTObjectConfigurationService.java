package com.arondor.common.reflection.gwt.client.service;

import com.arondor.common.reflection.model.config.ObjectConfigurationMap;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("objectConfigurationService")
public interface GWTObjectConfigurationService extends RemoteService
{
    ObjectConfigurationMap getObjectConfigurationMap(String context);
}

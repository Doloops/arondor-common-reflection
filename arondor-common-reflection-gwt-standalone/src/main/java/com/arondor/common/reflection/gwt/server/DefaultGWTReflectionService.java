package com.arondor.common.reflection.gwt.server;

import com.arondor.common.reflection.api.service.ReflectionService;
import com.arondor.common.reflection.gwt.server.GWTReflectionServiceStub;
import com.arondor.common.reflection.service.ReflectionServiceFactory;

public class DefaultGWTReflectionService extends GWTReflectionServiceStub
{
    @Override
    protected ReflectionService getReflectionService()
    {
        return ReflectionServiceFactory.getInstance().getReflectionService();
    }
}

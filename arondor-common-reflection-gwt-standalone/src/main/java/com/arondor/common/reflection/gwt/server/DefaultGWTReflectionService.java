package com.arondor.common.reflection.gwt.server;

import java.util.ArrayList;
import java.util.List;

import com.arondor.common.reflection.api.service.ReflectionService;
import com.arondor.common.reflection.gwt.server.GWTReflectionServiceStub;
import com.arondor.common.reflection.parser.java.JavaClassPathAccessibleClassProvider;
import com.arondor.common.reflection.service.DefaultReflectionService;
import com.arondor.common.reflection.service.ReflectionServiceFactory;

public class DefaultGWTReflectionService extends GWTReflectionServiceStub
{
    /**
     * 
     */
    private static final long serialVersionUID = -5917820772085494747L;
    
    public DefaultGWTReflectionService()
    {
        DefaultReflectionService defaultReflectionService = (DefaultReflectionService) getReflectionService();
        
        JavaClassPathAccessibleClassProvider classPathProvider = new JavaClassPathAccessibleClassProvider();
        List<String> packagePrefixes = new ArrayList<String>();
        packagePrefixes.add("com.arondor.common.reflection.gwt.server.samples");
        classPathProvider.setPackagePrefixes(packagePrefixes);
        classPathProvider.provideClasses(defaultReflectionService.getAccessibleClassCatalog());
    }

    @Override
    protected ReflectionService getReflectionService()
    {
        return ReflectionServiceFactory.getInstance().getReflectionService();
    }
}

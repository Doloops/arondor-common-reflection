package com.arondor.common.reflection.service;

import com.arondor.common.reflection.api.service.ReflectionService;
import com.arondor.common.reflection.catalog.SimpleAccessibleClassCatalog;
import com.arondor.common.reflection.parser.java.JavaAccessibleClassParser;

public class ReflectionServiceFactory
{
    private static ReflectionServiceFactory INSTANCE = new ReflectionServiceFactory();

    public static ReflectionServiceFactory getInstance()
    {
        return INSTANCE;
    }

    private ReflectionServiceFactory()
    {
        defaultReflectionService = new DefaultReflectionService();
        defaultReflectionService.setAccessibleClassCatalog(new SimpleAccessibleClassCatalog());
        defaultReflectionService.setAccessibleClassParser(new JavaAccessibleClassParser());
    }

    private DefaultReflectionService defaultReflectionService;

    public ReflectionService getReflectionService()
    {
        return defaultReflectionService;
    }

}

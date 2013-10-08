/*
 *  Copyright 2013, Arondor
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.arondor.common.reflection.service;

import com.arondor.common.reflection.api.service.ReflectionService;
import com.arondor.common.reflection.catalog.SimpleAccessibleClassCatalog;
import com.arondor.common.reflection.parser.java.JavaAccessibleClassParser;

public class ReflectionServiceFactory
{
    private static final ReflectionServiceFactory INSTANCE = new ReflectionServiceFactory();

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

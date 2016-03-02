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
package com.arondor.common.reflection.api.instantiator;

import com.arondor.common.reflection.model.config.ObjectConfiguration;

/**
 * 
 * @author Francois Barre
 * 
 */
public interface ReflectionInstantiator
{
    /**
     * Create a default InstantiationContext
     * 
     * @return an empty InstantiationContext
     */
    public InstantiationContext createDefaultInstantiationContext();

    /**
     * Instantiate an object based on its ObjectConfiguration
     * 
     * @param objectConfiguration
     *            the object configuration
     * @param desiredClass
     *            cast to the target class
     * @param context
     *            the Instantiation Context to use
     * @return the object, or null upon error
     */
    public <T> T instanciateObject(ObjectConfiguration objectConfiguration, Class<T> desiredClass,
            InstantiationContext context);

    /**
     * Instantiate an object based on its name
     * 
     * @param beanName
     *            the object bean name to lookup in instantation context
     * @param desiredClass
     *            cast to the target class
     * @param context
     *            the Instantiation Context to use
     * @return the object, or null upon error
     */
    public <T> T instanciateObject(String beanName, Class<T> desiredClass, InstantiationContext context);

    /**
     * Hook interface triggered each time an object has been created
     */
    interface ObjectInstanciationHook
    {
        void onObjectInstanciated(ObjectConfiguration objectConfiguration, Object object);
    }

    /**
     * Hook remover
     */
    interface HookHandler
    {
        void remove();
    }

    /**
     * Add a {@link ObjectInstanciationHook} which will be triggered each time
     * an object is instanciated
     * 
     * @param hook
     *            the hook to add, must not be null
     * @return the {@link HookHandler}, an easy way to remove this hook
     */
    HookHandler addObjectInstanciationHook(ObjectInstanciationHook hook);
}

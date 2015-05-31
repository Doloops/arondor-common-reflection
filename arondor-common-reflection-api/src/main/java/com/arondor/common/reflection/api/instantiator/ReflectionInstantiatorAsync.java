package com.arondor.common.reflection.api.instantiator;

import com.arondor.common.reflection.model.config.ObjectConfiguration;

/**
 * Async Instantiator
 * 
 * @author Francois Barre
 *
 */
public interface ReflectionInstantiatorAsync
{
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
    public <T> void instanciateObject(ObjectConfiguration objectConfiguration, Class<T> desiredClass,
            InstantiationContext context, InstantiationCallback<T> callback);

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
    public <T> void instanciateObject(String beanName, Class<T> desiredClass, InstantiationContext context,
            InstantiationCallback<T> callback);
}

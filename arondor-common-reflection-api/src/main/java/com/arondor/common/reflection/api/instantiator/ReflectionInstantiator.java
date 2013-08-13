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

}

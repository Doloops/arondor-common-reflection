package com.arondor.common.reflection.model.config;

import java.io.Serializable;

/**
 * An Element configuration can represent any of the Primitive types, Objects,
 * List, Map, Reference, ..
 * 
 * @author Francois Barre
 * 
 */
public interface ElementConfiguration extends Serializable
{
    public enum ElementConfigurationType
    {
        /**
         * The Primitive is for java.lang.* and int, long, double, ...
         */
        Primitive,

        /**
         * Any kind of non-primitive object
         */
        Object,

        /**
         * Reference to a shared object (singleton)
         */
        Reference,

        /**
         * List of Primitive, Object, ...
         */
        List,

        /**
         * Associative Map
         */
        Map
    }

    ElementConfigurationType getFieldConfigurationType();
}

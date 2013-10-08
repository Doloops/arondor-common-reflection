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

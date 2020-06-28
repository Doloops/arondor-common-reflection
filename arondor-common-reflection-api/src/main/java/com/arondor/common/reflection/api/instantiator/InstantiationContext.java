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
import com.arondor.common.reflection.model.config.ObjectConfigurationMap;

/**
 * Instantiation Context, holding shared objects and shared object
 * configurations
 * 
 * @author Francois Barre
 * 
 */
public interface InstantiationContext
{
    public void putSharedObject(String name, Object reference);

    public Object getSharedObject(String name);

    public ObjectConfiguration getSharedObjectConfiguration(String name);

    public void addSharedObjectConfigurations(ObjectConfigurationMap objetConfigurations);

    public void addSharedObjectResolver(SharedObjectResolver sharedObjectResolver);
}

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
package com.arondor.common.reflection.api.parser;

import com.arondor.common.reflection.model.config.ObjectConfiguration;
import com.arondor.common.reflection.model.config.ObjectConfigurationMap;

/**
 * Defines bean definitions parser. This parser allows to get
 * {@link ObjectConfiguration}
 * 
 * @author Christopher Laszczuk
 * 
 */
public interface ObjectConfigurationMapParser
{
    /**
     * Parses a set of bean definition
     * 
     * @return A set of {@link ObjectConfiguration}
     */
    ObjectConfigurationMap parse();
}

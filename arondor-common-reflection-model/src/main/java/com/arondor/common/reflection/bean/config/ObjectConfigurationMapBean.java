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
package com.arondor.common.reflection.bean.config;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.arondor.common.reflection.model.config.ObjectConfiguration;
import com.arondor.common.reflection.model.config.ObjectConfigurationMap;

/**
 * Simple HashMap wrapper for persistence
 * 
 * @author Francois Barre
 * 
 */
public class ObjectConfigurationMapBean implements ObjectConfigurationMap
{
    private static final long serialVersionUID = 1231620174802436482L;

    public ObjectConfigurationMapBean()
    {

    }

    public ObjectConfigurationMapBean(Map<String, ObjectConfiguration> initialMap)
    {
        map.putAll(initialMap);
    }

    private Map<String, ObjectConfiguration> map = new LinkedHashMap<String, ObjectConfiguration>();

    @Override
    public void clear()
    {
        map.clear();
    }

    @Override
    public boolean containsKey(Object key)
    {
        return map.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value)
    {
        return map.containsValue(value);
    }

    @Override
    public Set<java.util.Map.Entry<String, ObjectConfiguration>> entrySet()
    {
        return map.entrySet();
    }

    @Override
    public ObjectConfiguration get(Object key)
    {
        return map.get(key);
    }

    @Override
    public boolean isEmpty()
    {
        return map.isEmpty();
    }

    @Override
    public Set<String> keySet()
    {
        return map.keySet();
    }

    @Override
    public ObjectConfiguration put(String key, ObjectConfiguration value)
    {
        return map.put(key, value);
    }

    @Override
    public void putAll(Map<? extends String, ? extends ObjectConfiguration> m)
    {
        map.putAll(m);
    }

    @Override
    public ObjectConfiguration remove(Object key)
    {
        return map.remove(key);
    }

    @Override
    public int size()
    {
        return map.size();
    }

    @Override
    public Collection<ObjectConfiguration> values()
    {
        return map.values();
    }
}

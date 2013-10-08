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
package com.arondor.common.reflection.gwt.server.samples;

import java.util.List;
import java.util.Map;

import com.arondor.common.management.mbean.annotation.Description;

public class TestClassBis
{
    @Description("This is a String property")
    private String field;

    public String getField()
    {
        return field;
    }

    public void setField(String field)
    {
        this.field = field;
    }

    private Map<String, TestInterface> mapField;

    public Map<String, TestInterface> getMapField()
    {
        return mapField;
    }

    public void setMapField(Map<String, TestInterface> mapField)
    {
        this.mapField = mapField;
    }

    @Description("A map of String, String")
    private Map<String, String> stringMapField;

    public Map<String, String> getStringMapField()
    {
        return stringMapField;
    }

    public void setStringMapField(Map<String, String> stringMapField)
    {
        this.stringMapField = stringMapField;
    }

    @Description("A list of strings")
    private List<String> stringList;

    public List<String> getStringList()
    {
        return stringList;
    }

    public void setStringList(List<String> stringList)
    {
        this.stringList = stringList;
    }

    @Description("A list of objects")
    private List<TestInterface> objectList;

    public List<TestInterface> getObjectList()
    {
        return objectList;
    }

    public void setObjectList(List<TestInterface> objectList)
    {
        this.objectList = objectList;
    }

}

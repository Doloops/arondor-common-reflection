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

import com.arondor.common.management.mbean.annotation.Description;

public class TestClass implements TestInterface
{
    @Description("This is a string property")
    private String aStringProperty;

    @Description("This is a long property")
    private long aLongProperty;

    @Description("This is a sub class")
    private SubTestClass subClass;

    public String getAStringProperty()
    {
        return aStringProperty;
    }

    public void setAStringProperty(String aStringProperty)
    {
        this.aStringProperty = aStringProperty;
    }

    public long getALongProperty()
    {
        return aLongProperty;
    }

    public void setALongProperty(long aLongProperty)
    {
        this.aLongProperty = aLongProperty;
    }

    @Override
    public String toString()
    {
        return "TestClass [aStringProperty=" + aStringProperty + ", aLongProperty=" + aLongProperty + "]";
    }

    public SubTestClass getSubClass()
    {
        return subClass;
    }

    public void setSubClass(SubTestClass subClass)
    {
        this.subClass = subClass;
    }
}

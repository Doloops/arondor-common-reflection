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

import java.util.List;

import com.arondor.common.reflection.model.config.ElementConfiguration;
import com.arondor.common.reflection.model.config.ListConfiguration;

public class ListConfigurationBean extends ElementConfigurationBean implements ListConfiguration
{
    private static final long serialVersionUID = 5759802389815006707L;

    @Override
    public ElementConfigurationType getFieldConfigurationType()
    {
        return ElementConfigurationType.List;
    }

    public ListConfigurationBean()
    {

    }

    private List<ElementConfiguration> listConfiguration;

    @Override
    public List<ElementConfiguration> getListConfiguration()
    {
        return listConfiguration;
    }

    @Override
    public void setListConfiguration(List<ElementConfiguration> listConfiguration)
    {
        this.listConfiguration = listConfiguration;
    }

    @Override
    public String toString()
    {
        return "ListConfigurationBean [listConfiguration=" + listConfiguration + "]";
    }

}

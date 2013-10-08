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

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import com.arondor.common.reflection.model.config.ReferenceConfiguration;

@Entity
@DiscriminatorValue("REF")
public class ReferenceConfigurationBean extends ElementConfigurationBean implements ReferenceConfiguration
{

    /**
     * 
     */
    private static final long serialVersionUID = 1417667245091703766L;

    public ElementConfigurationType getFieldConfigurationType()
    {
        return ElementConfigurationType.Reference;
    }

    public ReferenceConfigurationBean()
    {

    }

    private String referenceName;

    public String getReferenceName()
    {
        return referenceName;
    }

    public void setReferenceName(String referenceName)
    {
        this.referenceName = referenceName;
    }

    public String toString()
    {
        return "ReferenceConfigurationBean [" + referenceName + "]";
    }
}

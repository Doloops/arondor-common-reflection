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

import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import com.arondor.common.reflection.model.config.ElementConfiguration;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "ElementConfiguration")
@DiscriminatorColumn(discriminatorType = DiscriminatorType.STRING)
public abstract class ElementConfigurationBean implements ElementConfiguration
{

    /**
     * 
     */
    private static final long serialVersionUID = 6717045194610707737L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long persistentId;

}

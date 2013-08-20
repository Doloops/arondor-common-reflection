package com.arondor.common.reflection.bean.config;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

import com.arondor.common.reflection.model.config.ElementConfiguration;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class ElementConfigurationBean implements ElementConfiguration
{

    /**
     * 
     */
    private static final long serialVersionUID = 6717045194610707737L;

    @Id
    @GeneratedValue
    private long persistentId;

}

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

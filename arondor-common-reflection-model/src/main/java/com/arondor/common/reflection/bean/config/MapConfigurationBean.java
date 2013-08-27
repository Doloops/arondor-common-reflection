package com.arondor.common.reflection.bean.config;

import java.util.Map;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

import com.arondor.common.reflection.model.config.ElementConfiguration;
import com.arondor.common.reflection.model.config.MapConfiguration;

@Entity
@DiscriminatorValue("MAP")
public class MapConfigurationBean extends ElementConfigurationBean implements MapConfiguration
{
    /**
     * 
     */
    private static final long serialVersionUID = 2968136864852828441L;

    public ElementConfigurationType getFieldConfigurationType()
    {
        return ElementConfigurationType.Map;
    }

    public MapConfigurationBean()
    {

    }

    // @ManyToMany(cascade = CascadeType.ALL)
    // , targetEntity = ElementConfigurationBean.class
    @Transient
    private Map<ElementConfiguration, ElementConfiguration> mapConfiguration;

    public Map<ElementConfiguration, ElementConfiguration> getMapConfiguration()
    {
        return mapConfiguration;
    }

    public void setMapConfiguration(Map<ElementConfiguration, ElementConfiguration> mapConfiguration)
    {
        this.mapConfiguration = mapConfiguration;
    }

    public String toString()
    {
        return "MapConfigurationBean[" + mapConfiguration.entrySet() + "]";
    }
}

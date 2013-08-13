package com.arondor.common.reflection.model.config;

import java.util.List;

/**
 * ListConfiguration : list of EementConfiguration, corresponding to a generic
 * List<?> instance
 * 
 * @author Francois Barre
 * 
 */
public interface ListConfiguration extends ElementConfiguration
{
    List<ElementConfiguration> getListConfiguration();

    void setListConfiguration(List<ElementConfiguration> listConfiguration);
}

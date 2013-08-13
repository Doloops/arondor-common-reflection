package com.arondor.common.reflection.model.config;

import java.util.List;

public interface ListConfiguration extends ElementConfiguration
{
    List<ElementConfiguration> getListConfiguration();

    void setListConfiguration(List<ElementConfiguration> listConfiguration);
}

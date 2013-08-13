package com.arondor.common.reflection.bean.config;

import java.util.List;

import com.arondor.common.reflection.model.config.ElementConfiguration;
import com.arondor.common.reflection.model.config.ListConfiguration;

public class ListConfigurationBean extends ElementConfigurationBean implements ListConfiguration
{

    /**
     * 
     */
    private static final long serialVersionUID = 5759802389815006707L;

    public ElementConfigurationType getFieldConfigurationType()
    {
        return ElementConfigurationType.List;
    }

    private List<ElementConfiguration> listConfiguration;

    public List<ElementConfiguration> getListConfiguration()
    {
        return listConfiguration;
    }

    public void setListConfiguration(List<ElementConfiguration> listConfiguration)
    {
        this.listConfiguration = listConfiguration;
    }

}

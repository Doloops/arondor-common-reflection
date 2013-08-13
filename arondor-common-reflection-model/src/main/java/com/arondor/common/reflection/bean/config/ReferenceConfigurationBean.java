package com.arondor.common.reflection.bean.config;

import com.arondor.common.reflection.model.config.ReferenceConfiguration;

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

    private String referenceName;

    public String getReferenceName()
    {
        return referenceName;
    }

    public void setReferenceName(String referenceName)
    {
        this.referenceName = referenceName;
    }

}

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

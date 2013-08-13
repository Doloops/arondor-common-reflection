package com.arondor.common.reflection.bean.config;

import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.arondor.common.reflection.model.config.FieldConfiguration;
import com.arondor.common.reflection.model.config.ObjectConfiguration;

@Entity
@Table(name = "ObjectConfiguration")
public class ObjectConfigurationBean implements ObjectConfiguration
{

    /**
     * 
     */
    private static final long serialVersionUID = 5011548715444426003L;

    @Id
    @GeneratedValue
    private long persistentId;

    public void setPersistentId(long persistentId)
    {
        this.persistentId = persistentId;
    }

    public long getPersistentId()
    {
        return persistentId;
    }

    public ObjectConfigurationBean()
    {

    }

    private String className;

    public String getClassName()
    {
        return className;
    }

    public void setClassName(String className)
    {
        this.className = className;
    }

    @OneToMany(cascade = CascadeType.ALL, targetEntity = FieldConfigurationBean.class)
    @JoinTable(name = "FieldConfigMap", joinColumns = @JoinColumn(name = "objectConfigId"), inverseJoinColumns = @JoinColumn(name = "fieldConfigId"))
    private Map<String, FieldConfiguration> fields;

    public Map<String, FieldConfiguration> getFields()
    {
        return fields;
    }

    public void setFields(Map<String, FieldConfiguration> fields)
    {
        this.fields = fields;
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

    @OneToMany(cascade = CascadeType.ALL, targetEntity = FieldConfigurationBean.class)
    private List<FieldConfiguration> constructorArguments;

    public List<FieldConfiguration> getConstructorArguments()
    {
        return constructorArguments;
    }

    public void setConstructorArguments(List<FieldConfiguration> constructorArguments)
    {
        this.constructorArguments = constructorArguments;
    }

}

//package com.arondor.common.reflection.bean.config;
//
//import java.util.List;
//
//import javax.persistence.CascadeType;
//import javax.persistence.Column;
//import javax.persistence.ElementCollection;
//import javax.persistence.Entity;
//import javax.persistence.GeneratedValue;
//import javax.persistence.Id;
//import javax.persistence.JoinColumn;
//import javax.persistence.JoinTable;
//import javax.persistence.OneToMany;
//import javax.persistence.OneToOne;
//import javax.persistence.OrderColumn;
//import javax.persistence.Table;
//
//import com.arondor.common.reflection.model.config.ElementConfiguration;
//import com.arondor.common.reflection.model.config.ObjectConfiguration;
//
//@Entity
//@Table(name = "FieldConfiguration")
//public class FieldConfigurationBean implements com.arondor.common.reflection.model.config.ElementConfiguration
//{
//    /**
//     * 
//     */
//    private static final long serialVersionUID = 1L;
//
//    @Id
//    @GeneratedValue
//    private long persistentId;
//
//    public void setPersistentId(long persistentId)
//    {
//        this.persistentId = persistentId;
//    }
//
//    public long getPersistentId()
//    {
//        return persistentId;
//    }
//
//    public FieldConfigurationBean()
//    {
//
//    }
//
//    private FieldConfigurationType fieldConfigurationType;
//
//    @Column(length = 4096)
//    private String valueString;
//
//    @ElementCollection
//    @OrderColumn
//    @JoinTable(name = "FieldConfigValues", joinColumns = @JoinColumn(name = "fieldConfigId"))
//    private List<String> valuesString;
//
//    @OneToOne(cascade = CascadeType.ALL, targetEntity = com.arondor.common.reflection.bean.config.ObjectConfigurationBean.class)
//    @JoinColumn(name = "singleObjectId")
//    private ObjectConfiguration objectConfiguration;
//
//    @OneToMany(cascade = CascadeType.ALL, targetEntity = com.arondor.common.reflection.bean.config.FieldConfigurationBean.class)
//    @JoinTable(name = "ObjectConfigList", joinColumns = @JoinColumn(name = "fieldConfigId"), inverseJoinColumns = @JoinColumn(name = "subObjectId"))
//    private List<ElementConfiguration> objectConfigurations;
//
//    public FieldConfigurationType getFieldConfigurationType()
//    {
//        return fieldConfigurationType;
//    }
//
//    public void setFieldConfigurationType(FieldConfigurationType fieldConfigurationType)
//    {
//        this.fieldConfigurationType = fieldConfigurationType;
//    }
//
//    public List<String> getValues()
//    {
//        return valuesString;
//    }
//
//    public void setValues(List<String> valuesString)
//    {
//        this.valuesString = valuesString;
//    }
//
//    public ObjectConfiguration getObjectConfiguration()
//    {
//        return objectConfiguration;
//    }
//
//    public void setObjectConfiguration(ObjectConfiguration objectConfiguration)
//    {
//        this.objectConfiguration = objectConfiguration;
//    }
//
//    public List<ElementConfiguration> getObjectConfigurations()
//    {
//        return objectConfigurations;
//    }
//
//    public void setObjectConfigurations(List<ElementConfiguration> objectConfigurations)
//    {
//        this.objectConfigurations = objectConfigurations;
//    }
//
//    public void setValue(String valueString)
//    {
//        this.valueString = valueString;
//    }
//
//    public String getValue()
//    {
//        return valueString;
//    }
//
// }

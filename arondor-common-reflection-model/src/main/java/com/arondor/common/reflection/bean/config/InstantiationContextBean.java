//package com.arondor.common.reflection.bean.config;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import com.arondor.common.reflection.model.config.InstantiationContext;
//import com.arondor.common.reflection.model.config.ObjectConfiguration;
//import com.arondor.common.reflection.model.config.ObjectConfigurationMap;
//
//public class InstantiationContextBean implements InstantiationContext
//{
//    private Map<String, Object> instances = new HashMap<String, Object>();
//
//    public Object getSharedObject(String name)
//    {
//        return instances.get(name);
//    }
//
//    public void putSharedObject(String name, Object reference)
//    {
//        instances.put(name, reference);
//    }
//
//    public ObjectConfiguration getSharedObjectConfiguration(String name)
//    {
//        if (objetConfigurations == null)
//        {
//            return null;
//        }
//        return objetConfigurations.get(name);
//    }
//
//    private Map<String, ObjectConfiguration> objetConfigurations;
//
//    public void setSharedObjectConfigurations(ObjectConfigurationMap objetConfigurations)
//    {
//        this.objetConfigurations = objetConfigurations;
//    }
//
// }

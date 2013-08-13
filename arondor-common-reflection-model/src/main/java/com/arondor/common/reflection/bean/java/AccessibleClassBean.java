package com.arondor.common.reflection.bean.java;

import java.util.List;
import java.util.Map;

import com.arondor.common.reflection.model.java.AccessibleClass;
import com.arondor.common.reflection.model.java.AccessibleConstructor;
import com.arondor.common.reflection.model.java.AccessibleField;
import com.arondor.common.reflection.model.java.AccessibleMethod;

public class AccessibleClassBean implements AccessibleClass
{
    /**
     * 
     */
    private static final long serialVersionUID = 5205325249459644854L;

    private Map<String, AccessibleField> accessibleFields;

    public AccessibleClassBean()
    {

    }

    public void setAccessibleFields(Map<String, AccessibleField> accessibleFields)
    {
        this.accessibleFields = accessibleFields;
    }

    public Map<String, AccessibleField> getAccessibleFields()
    {
        return accessibleFields;
    }

    private String name;

    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    private String description;

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getDescription()
    {
        return description;
    }

    public String getClassBaseName()
    {
        int idx = name.lastIndexOf('.');
        if (idx != -1)
        {
            return name.substring(idx + 1);
        }
        return name;
    }

    public String getPackageName()
    {
        int idx = name.lastIndexOf('.');
        if (idx != -1)
        {
            return name.substring(0, idx);
        }
        return "";
    }

    private List<String> allInterfaces;

    private List<String> interfaces;

    private String superclass;

    public List<String> getAllInterfaces()
    {
        return allInterfaces;
    }

    public void setAllInterfaces(List<String> allInterfaces)
    {
        this.allInterfaces = allInterfaces;
    }

    public List<String> getInterfaces()
    {
        return interfaces;
    }

    public void setInterfaces(List<String> interfaces)
    {
        this.interfaces = interfaces;
    }

    public String getSuperclass()
    {
        return superclass;
    }

    public void setSuperclass(String superclass)
    {
        this.superclass = superclass;
    }

    private List<AccessibleConstructor> constructors;

    public List<AccessibleConstructor> getConstructors()
    {
        return constructors;
    }

    public void setConstructors(List<AccessibleConstructor> constructors)
    {
        this.constructors = constructors;
    }

    private List<AccessibleMethod> methods;

    public List<AccessibleMethod> getAccessibleMethods()
    {
        return methods;
    }

    public void setAccessibleMethods(List<AccessibleMethod> methods)
    {
        this.methods = methods;
    }
}

package com.arondor.common.reflection.noreflect.model;

import java.util.Collection;

/**
 * Runtime hardwired Object reflection catalog : Create objects and Set fields
 * 
 * @author Francois Barre
 * 
 */
public interface ReflectionInstantiatorCatalog
{
    void registerObjectInheritance(String className, Collection<String> inheritance);

    void registerObjectConstructor(String className, ObjectConstructor objectConstructor);

    void registerFieldSetter(String className, String fieldName, FieldSetter fieldSetter);

    ObjectConstructor getObjectConstructor(String className);

    FieldSetter getFieldSetter(String className, String fieldName);
}

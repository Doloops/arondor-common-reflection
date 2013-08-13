package com.arondor.common.reflection.noreflect.model;

public interface ReflectionInstantiatorCatalog
{
    void registerObjectConstructor(String className, ObjectConstructor objectConstructor);

    void registerFieldSetter(String className, String fieldName, FieldSetter fieldSetter);

    ObjectConstructor getObjectConstructor(String className);

    FieldSetter getFieldSetter(String className, String fieldName);
}

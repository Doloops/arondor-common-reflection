package com.arondor.common.reflection.xstream;

public interface TypeOracle
{
    String guessType(String className, String fieldName);
}

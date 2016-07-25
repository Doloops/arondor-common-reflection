package com.arondor.common.reflection.xstream;

import com.arondor.common.reflection.model.config.ObjectConfiguration;

public interface ObjectDefinitionReader
{
    ObjectConfiguration parse(String xmlString);
}

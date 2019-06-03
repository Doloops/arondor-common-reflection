package com.arondor.common.reflection.xstream;

import com.arondor.common.reflection.model.config.ObjectConfiguration;

public interface ObjectConfigurationReader
{
    ObjectConfiguration parse(String xmlString);

    void setTypeOracle(TypeOracle typeOracle);
}

package com.arondor.common.reflection.api.parser;

import com.arondor.common.reflection.model.config.ObjectConfiguration;
import com.arondor.common.reflection.model.config.ObjectConfigurationMap;

/**
 * Defines bean definitions parser. This parser allows to get
 * {@link ObjectConfiguration}
 * 
 * @author cla
 * 
 */
public interface ObjectConfigurationMapParser
{
    /**
     * Parses a set of bean definition
     * 
     * @return A set of {@link ObjectConfiguration}
     */
    ObjectConfigurationMap parse();
}

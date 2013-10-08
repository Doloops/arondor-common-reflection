/*
 *  Copyright 2013, Arondor
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.arondor.common.reflection.sample.gwt.server;

import com.arondor.common.reflection.api.parser.ObjectConfigurationMapParser;
import com.arondor.common.reflection.model.config.ObjectConfigurationMap;
import com.arondor.common.reflection.parser.spring.XMLBeanDefinitionParser;
import com.arondor.common.reflection.sample.gwt.client.service.ConfigurationService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class SampleConfigurationService extends RemoteServiceServlet implements ConfigurationService
{

    /**
     * 
     */
    private static final long serialVersionUID = 5712040837301289904L;

    private static final String configurationLocation = "configuration.xml";

    public ObjectConfigurationMap getObjectConfigurations()
    {
        ObjectConfigurationMapParser beanDefinitionParser = new XMLBeanDefinitionParser(configurationLocation);
        return beanDefinitionParser.parse();
    }

}

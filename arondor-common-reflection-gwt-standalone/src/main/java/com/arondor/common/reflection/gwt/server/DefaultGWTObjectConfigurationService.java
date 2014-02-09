package com.arondor.common.reflection.gwt.server;

import com.arondor.common.reflection.gwt.client.service.GWTObjectConfigurationService;
import com.arondor.common.reflection.model.config.ObjectConfigurationMap;
import com.arondor.common.reflection.parser.spring.XMLBeanDefinitionParser;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class DefaultGWTObjectConfigurationService extends RemoteServiceServlet implements GWTObjectConfigurationService
{
    /**
     * 
     */
    private static final long serialVersionUID = -7953708354980206361L;

    public ObjectConfigurationMap getObjectConfigurationMap(String context)
    {
        XMLBeanDefinitionParser parser = new XMLBeanDefinitionParser(context);
        return parser.parse();
    }

}

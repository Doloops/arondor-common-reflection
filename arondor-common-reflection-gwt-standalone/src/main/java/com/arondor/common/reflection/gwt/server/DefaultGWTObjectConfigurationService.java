package com.arondor.common.reflection.gwt.server;

import java.util.ArrayList;
import java.util.List;

import com.arondor.common.reflection.gwt.client.service.GWTObjectConfigurationService;
import com.arondor.common.reflection.model.config.ObjectConfigurationMap;
import com.arondor.common.reflection.parser.java.DirectoryAccessibleClassProvider;
import com.arondor.common.reflection.parser.spring.XMLBeanDefinitionParser;
import com.arondor.common.reflection.service.DefaultReflectionService;
import com.arondor.common.reflection.service.ReflectionServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class DefaultGWTObjectConfigurationService extends RemoteServiceServlet implements GWTObjectConfigurationService
{
    /**
     * 
     */
    private static final long serialVersionUID = -7953708354980206361L;

    protected DefaultReflectionService getReflectionService()
    {
        return (DefaultReflectionService) ReflectionServiceFactory.getInstance().getReflectionService();
    }

    public ObjectConfigurationMap getObjectConfigurationMap(String context)
    {
        XMLBeanDefinitionParser parser = new XMLBeanDefinitionParser(context);
        return parser.parse();
    }

    public void loadLib(String context, List<String> packagePrefixes)
    {
        DirectoryAccessibleClassProvider provider = new DirectoryAccessibleClassProvider();
        List<String> directories = new ArrayList<String>();
        directories.add(context);
        provider.setDirectories(directories);
        provider.setPackagePrefixes(packagePrefixes);

        provider.provideClasses(getReflectionService().getAccessibleClassCatalog());
    }
}

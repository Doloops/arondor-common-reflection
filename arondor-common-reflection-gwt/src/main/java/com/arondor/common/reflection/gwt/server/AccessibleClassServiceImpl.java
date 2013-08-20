package com.arondor.common.reflection.gwt.server;

import com.arondor.common.reflection.api.parser.AccessibleClassParser;
import com.arondor.common.reflection.gwt.client.service.AccessibleClassService;
import com.arondor.common.reflection.model.java.AccessibleClass;
import com.arondor.common.reflection.parser.java.JavaAccessibleClassParser;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class AccessibleClassServiceImpl extends RemoteServiceServlet implements AccessibleClassService
{
    private AccessibleClassParser accessibleClassParser = new JavaAccessibleClassParser();

    public AccessibleClassServiceImpl()
    {
    }

    public AccessibleClass getAccessibleClass(String className)
    {
        Class<?> clazz;
        try
        {
            clazz = Class.forName(className);
            return accessibleClassParser.parseAccessibleClass(clazz);
        }
        catch (ClassNotFoundException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

}

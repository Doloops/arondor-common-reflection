package com.arondor.common.reflection.gwt.client.presenter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.arondor.common.reflection.bean.java.AccessibleClassBean;
import com.arondor.common.reflection.gwt.client.service.GWTReflectionServiceAsync;
import com.arondor.common.reflection.model.config.ObjectConfigurationMap;
import com.arondor.common.reflection.model.java.AccessibleClass;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestDefaultImplementingClassPresenter
{
    private static final Logger LOG = Logger.getLogger(TestDefaultImplementingClassPresenter.class);

    @Test
    public void testAbstractClass()
    {
        GWTReflectionServiceAsync rpcService = Mockito.mock(GWTReflectionServiceAsync.class);

        Mockito.doAnswer(new Answer<Void>()
        {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable
            {
                String accessibleClassName = (String) invocation.getArguments()[0];
                AsyncCallback<AccessibleClass> callback = (AsyncCallback<AccessibleClass>) invocation.getArguments()[1];
                LOG.info("getAccessibleClass(" + accessibleClassName + ")");

                AccessibleClassBean bean = new AccessibleClassBean();
                bean.setName(accessibleClassName);
                callback.onSuccess(bean);
                return null;
            }
        }).when(rpcService).getAccessibleClass(Matchers.anyString(), Matchers.any(AsyncCallback.class));

        Mockito.doAnswer(new Answer<Void>()
        {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable
            {
                String accessibleClass = (String) invocation.getArguments()[0];
                AsyncCallback<Collection<AccessibleClass>> callback = (AsyncCallback<Collection<AccessibleClass>>) invocation
                        .getArguments()[1];
                LOG.info("getImplementingAccessibleClasses(" + accessibleClass + ")");

                List<AccessibleClass> classes = new ArrayList<AccessibleClass>();
                {
                    AccessibleClassBean bean1 = new AccessibleClassBean();
                    bean1.setName("com.arondor.testing.SomeAbstractClass");
                    bean1.setAbstract(true);
                    bean1.setSuperclass("java.lang.Object");
                    classes.add(bean1);
                }
                {
                    AccessibleClassBean bean1 = new AccessibleClassBean();
                    bean1.setName("com.arondor.testing.SomeConcreteClass");
                    bean1.setAbstract(false);
                    bean1.setSuperclass("java.lang.Object");
                    classes.add(bean1);
                }

                callback.onSuccess(classes);
                return null;
            }
        }).when(rpcService).getImplementingAccessibleClasses(Matchers.anyString(), Matchers.any(AsyncCallback.class));

        ObjectConfigurationMap objectConfigurationMap = null;
        String baseClassName = "java.lang.Object";
        ImplementingClassPresenter.Display display = Mockito.mock(ImplementingClassPresenter.Display.class);
        DefaultImplementingClassPresenter presenter = new DefaultImplementingClassPresenter(rpcService,
                objectConfigurationMap, baseClassName, display);

        ArgumentCaptor<Collection> captor = ArgumentCaptor.forClass(Collection.class);
        Mockito.verify(display, Mockito.atLeastOnce()).setImplementingClasses(captor.capture());

        List<String> result = (List<String>) captor.getValue();
        Assert.assertEquals("Invalid result : " + result, 2, result.size());
        Assert.assertEquals(null, result.get(0));
        Assert.assertEquals("com.arondor.testing.SomeConcreteClass", result.get(1));
    }
}

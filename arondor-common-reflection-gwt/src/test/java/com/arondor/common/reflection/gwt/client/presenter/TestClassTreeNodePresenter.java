package com.arondor.common.reflection.gwt.client.presenter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.arondor.common.reflection.catalog.SimpleAccessibleClassCatalog;
import com.arondor.common.reflection.gwt.client.presenter.ClassTreeNodePresenter.Display;
import com.arondor.common.reflection.gwt.client.testclasses.ParentTestClass;
import com.arondor.common.reflection.gwt.client.testclasses.TestClass;
import com.arondor.common.reflection.gwt.client.testclasses.TestInterface;
import com.arondor.common.reflection.parser.java.JavaAccessibleClassParser;
import com.arondor.common.reflection.parser.java.JavaClassPathAccessibleClassProvider;
import com.arondor.common.reflection.service.DefaultReflectionService;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;

public class TestClassTreeNodePresenter
{
    private MockGWTReflectionServiceAsync rpcService;

    @Before
    public void init()
    {
        DefaultReflectionService reflectionService = new DefaultReflectionService();
        reflectionService.setAccessibleClassCatalog(new SimpleAccessibleClassCatalog());
        reflectionService.setAccessibleClassParser(new JavaAccessibleClassParser());
        JavaClassPathAccessibleClassProvider provider = new JavaClassPathAccessibleClassProvider();
        List<String> packagePrefixes = new ArrayList<String>();
        packagePrefixes.add(ParentTestClass.class.getPackage().getName());
        provider.setPackagePrefixes(packagePrefixes);
        provider.provideClasses(reflectionService.getAccessibleClassCatalog());

        rpcService = new MockGWTReflectionServiceAsync(reflectionService);
    }

    private ClassTreeNodePresenter.Display mockClassTreeNodePresenterDisplay()
    {
        ClassTreeNodePresenter.Display nodeView = mock(ClassTreeNodePresenter.Display.class);
        ImplementingClassPresenter.Display implView = mock(ImplementingClassPresenter.Display.class);
        AccessibleFieldMapPresenter.Display fieldsView = mock(AccessibleFieldMapPresenter.Display.class);

        when(fieldsView.createAccessibleFieldDisplay()).thenAnswer(new Answer<AccessibleFieldPresenter.Display>()
        {
            public com.arondor.common.reflection.gwt.client.presenter.AccessibleFieldPresenter.Display answer(
                    InvocationOnMock invocation) throws Throwable
            {
                return mock(AccessibleFieldPresenter.Display.class);
            }
        });
        when(nodeView.getImplementingClassDisplay()).thenReturn(implView);
        when(nodeView.getFieldMapDisplay()).thenReturn(fieldsView);
        when(nodeView.createChild()).thenAnswer(new Answer<ClassTreeNodePresenter.Display>()
        {

            public Display answer(InvocationOnMock invocation) throws Throwable
            {
                return mockClassTreeNodePresenterDisplay();
            }
        });
        return nodeView;
    }

    @Test
    public void testChangeBaseClassName()
    {
        ClassTreeNodePresenter.Display nodeView = mockClassTreeNodePresenterDisplay();

        final List<ValueChangeHandler<String>> changeHandlers = new ArrayList<ValueChangeHandler<String>>();
        when(nodeView.getImplementingClassDisplay().addValueChangeHandler(any(ValueChangeHandler.class))).then(
                new Answer<HandlerRegistration>()
                {
                    public HandlerRegistration answer(InvocationOnMock invocation) throws Throwable
                    {
                        changeHandlers.add((ValueChangeHandler<String>) invocation.getArguments()[0]);
                        return mock(HandlerRegistration.class);
                    }
                });

        ClassTreeNodePresenter nodePresenter = new ClassTreeNodePresenter(rpcService, TestInterface.class.getName(),
                nodeView);
        assertEquals(TestInterface.class.getName(), nodePresenter.getBaseClassName());
        assertNull(nodePresenter.getImplementClassName());
        verify(nodeView.getImplementingClassDisplay()).setBaseClassName(TestInterface.class.getName());

        ValueChangeEvent<String> valueChangeEvent = mock(ValueChangeEvent.class);
        when(valueChangeEvent.getValue()).thenReturn(TestClass.class.getName());
        for (ValueChangeHandler<String> changeHandler : changeHandlers)
        {
            changeHandler.onValueChange(valueChangeEvent);
        }
        /*
         * We shall not call selectImplementingClass because it's the widget
         * that selected the class
         */
        verify(nodeView.getImplementingClassDisplay(), times(0)).selectImplementingClass(anyString());
        assertEquals(TestClass.class.getName(), nodePresenter.getImplementClassName());
    }
}

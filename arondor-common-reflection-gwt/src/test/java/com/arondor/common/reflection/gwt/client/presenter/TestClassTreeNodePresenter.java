package com.arondor.common.reflection.gwt.client.presenter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.arondor.common.reflection.bean.config.ObjectConfigurationFactoryBean;
import com.arondor.common.reflection.catalog.SimpleAccessibleClassCatalog;
import com.arondor.common.reflection.gwt.client.presenter.ClassTreeNodePresenter.ClassDisplay;
import com.arondor.common.reflection.gwt.client.presenter.fields.PrimitiveTreeNodePresenter;
import com.arondor.common.reflection.gwt.client.testclasses.ParentTestClass;
import com.arondor.common.reflection.gwt.client.testclasses.TestClass;
import com.arondor.common.reflection.gwt.client.testclasses.TestInterface;
import com.arondor.common.reflection.model.config.ElementConfiguration;
import com.arondor.common.reflection.model.config.ObjectConfiguration;
import com.arondor.common.reflection.model.config.ObjectConfigurationFactory;
import com.arondor.common.reflection.parser.java.JavaAccessibleClassParser;
import com.arondor.common.reflection.parser.java.JavaClassPathAccessibleClassProvider;
import com.arondor.common.reflection.service.DefaultReflectionService;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;

public class TestClassTreeNodePresenter
{
    private MockGWTReflectionServiceAsync rpcService;

    private ObjectConfigurationFactory factory = new ObjectConfigurationFactoryBean();

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

    private ClassTreeNodePresenter.ClassDisplay mockClassTreeNodePresenterDisplay()
    {
        ClassTreeNodePresenter.ClassDisplay nodeView = mock(ClassTreeNodePresenter.ClassDisplay.class);
        ImplementingClassPresenter.ImplementingClassDisplay implView = mock(
                ImplementingClassPresenter.ImplementingClassDisplay.class);
        when(nodeView.getImplementingClassDisplay()).thenReturn(implView);
        doAnswer(new Answer<Void>()
        {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable
            {
                System.err.println("selectImplementingClass(" + invocation.getArguments()[0] + ")");
                return null;
            }
        }).when(implView).selectImplementingClass(ImplementingClass.NULL_CLASS);
        when(nodeView.createPrimitiveChild(anyString(), Matchers.eq(false)))
                .thenAnswer(new Answer<PrimitiveTreeNodePresenter.PrimitiveDisplay>()
                {
                    @Override
                    public PrimitiveTreeNodePresenter.PrimitiveDisplay answer(InvocationOnMock invocation)
                            throws Throwable
                    {
                        return mock(PrimitiveTreeNodePresenter.PrimitiveDisplay.class);
                    }
                });
        when(nodeView.createClassChild(false)).thenAnswer(new Answer<ClassTreeNodePresenter.ClassDisplay>()
        {
            @Override
            public ClassDisplay answer(InvocationOnMock invocation) throws Throwable
            {
                return mockClassTreeNodePresenterDisplay();
            }
        });
        return nodeView;
    }

    @Test
    public void testChangeBaseClassName()
    {
        ClassTreeNodePresenter.ClassDisplay nodeView = mockClassTreeNodePresenterDisplay();

        final List<ValueChangeHandler<ImplementingClass>> changeHandlers = new ArrayList<ValueChangeHandler<ImplementingClass>>();
        when(nodeView.getImplementingClassDisplay().addValueChangeHandler(any(ValueChangeHandler.class)))
                .then(new Answer<HandlerRegistration>()
                {
                    @Override
                    public HandlerRegistration answer(InvocationOnMock invocation) throws Throwable
                    {
                        changeHandlers.add((ValueChangeHandler<ImplementingClass>) invocation.getArguments()[0]);
                        return mock(HandlerRegistration.class);
                    }
                });

        ClassTreeNodePresenter nodePresenter = new ClassTreeNodePresenter(rpcService, null,
                TestInterface.class.getName(), nodeView);
        assertEquals(TestInterface.class.getName(), nodePresenter.getBaseClassName());
        assertNull(nodePresenter.getImplementingClass().getName());
        verify(nodeView.getImplementingClassDisplay()).setBaseClassName(TestInterface.class.getName());

        ValueChangeEvent<ImplementingClass> valueChangeEvent = mock(ValueChangeEvent.class);
        when(valueChangeEvent.getValue()).thenReturn(new ImplementingClass(false, TestClass.class.getName()));
        for (ValueChangeHandler<ImplementingClass> changeHandler : changeHandlers)
        {
            changeHandler.onValueChange(valueChangeEvent);
        }
        /*
         * We shall not call selectImplementingClass because it's the widget
         * that selected the class
         */
        verify(nodeView.getImplementingClassDisplay(), atLeastOnce())
                .selectImplementingClass(ImplementingClass.NULL_CLASS);
        assertEquals(TestClass.class.getName(), nodePresenter.getImplementingClass().getName());
    }

    @Test
    public void testChangeBaseClassNameFromObjectConfiguration()
    {
        ClassTreeNodePresenter.ClassDisplay nodeView = mockClassTreeNodePresenterDisplay();

        ClassTreeNodePresenter nodePresenter = new ClassTreeNodePresenter(rpcService, null,
                TestInterface.class.getName(), nodeView);
        assertEquals(TestInterface.class.getName(), nodePresenter.getBaseClassName());
        assertNotNull(nodePresenter.getImplementingClass());
        assertNull(nodePresenter.getImplementingClass().getName());
        verify(nodeView.getImplementingClassDisplay()).setBaseClassName(TestInterface.class.getName());

        ObjectConfiguration objectConfiguration = factory.createObjectConfiguration();
        objectConfiguration.setFields(new HashMap<String, ElementConfiguration>());
        objectConfiguration.setClassName(TestClass.class.getName());

        nodePresenter.setElementConfiguration(objectConfiguration);

        verify(nodeView.getImplementingClassDisplay(), atLeastOnce())
                .selectImplementingClass(ImplementingClass.NULL_CLASS);
        assertEquals(TestClass.class.getName(), nodePresenter.getImplementingClass().getName());

        ElementConfiguration elementConfiguration = nodePresenter.getElementConfiguration(factory);
        assertNotNull(elementConfiguration);
        assertTrue(elementConfiguration instanceof ObjectConfiguration);
        ObjectConfiguration createdObjectConfiguration = (ObjectConfiguration) elementConfiguration;
        assertEquals(TestClass.class.getName(), createdObjectConfiguration.getClassName());
    }
}

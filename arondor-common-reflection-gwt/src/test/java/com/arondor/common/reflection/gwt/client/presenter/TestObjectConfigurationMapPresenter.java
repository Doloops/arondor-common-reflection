package com.arondor.common.reflection.gwt.client.presenter;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.arondor.common.reflection.api.parser.AccessibleClassProvider;
import com.arondor.common.reflection.bean.config.ObjectConfigurationFactoryBean;
import com.arondor.common.reflection.bean.config.ObjectConfigurationMapBean;
import com.arondor.common.reflection.catalog.SimpleAccessibleClassCatalog;
import com.arondor.common.reflection.gwt.client.api.ObjectConfigurationMapPresenter;
import com.arondor.common.reflection.gwt.client.presenter.ClassTreeNodePresenter.ClassDisplay;
import com.arondor.common.reflection.gwt.client.presenter.ImplementingClassPresenter.ImplementingClassDisplay;
import com.arondor.common.reflection.gwt.client.presenter.SimpleObjectConfigurationMapPresenter.ObjectConfigurationMapDisplay;
import com.arondor.common.reflection.gwt.client.presenter.fields.ListTreeNodePresenter.ListRootDisplay;
import com.arondor.common.reflection.gwt.client.presenter.fields.MapTreeNodePresenter.MapPairDisplay;
import com.arondor.common.reflection.gwt.client.presenter.fields.MapTreeNodePresenter.MapRootDisplay;
import com.arondor.common.reflection.gwt.client.presenter.fields.PrimitiveTreeNodePresenter.PrimitiveDisplay;
import com.arondor.common.reflection.gwt.client.presenter.fields.StringListTreeNodePresenter.StringListDisplay;
import com.arondor.common.reflection.gwt.client.service.GWTReflectionServiceAsync;
import com.arondor.common.reflection.model.config.ElementConfiguration;
import com.arondor.common.reflection.model.config.ListConfiguration;
import com.arondor.common.reflection.model.config.ObjectConfiguration;
import com.arondor.common.reflection.model.config.ObjectConfigurationFactory;
import com.arondor.common.reflection.model.config.ObjectConfigurationMap;
import com.arondor.common.reflection.parser.java.JavaAccessibleClassParser;
import com.arondor.common.reflection.parser.spring.XMLBeanDefinitionParser;
import com.arondor.common.reflection.service.DefaultReflectionService;
import com.google.gwt.event.dom.client.HasClickHandlers;

public class TestObjectConfigurationMapPresenter
{
    private final ObjectConfigurationFactory objectConfigurationFactory = new ObjectConfigurationFactoryBean();

    @Test
    public void testSetObjectConfigurationMap()
    {
        String fieldName = "SomeClass";

        DefaultReflectionService reflectionService = new DefaultReflectionService();
        reflectionService.setAccessibleClassCatalog(new SimpleAccessibleClassCatalog());
        reflectionService.setAccessibleClassParser(new JavaAccessibleClassParser());

        BeanFactory beanFactory = new ClassPathXmlApplicationContext("testreflection-config.xml");

        AccessibleClassProvider accessibleClassProvider = beanFactory.getBean("accessibleClassProvider",
                AccessibleClassProvider.class);

        accessibleClassProvider.provideClasses(reflectionService.getAccessibleClassCatalog());

        GWTReflectionServiceAsync gwtReflectionService = new MockGWTReflectionServiceAsync(reflectionService);
        ObjectConfigurationMapDisplay objectConfigurationMapDisplay = Mockito.mock(ObjectConfigurationMapDisplay.class);
        HasClickHandlers hasClickHandlers = Mockito.mock(HasClickHandlers.class);
        Mockito.when(objectConfigurationMapDisplay.addElementClickHandler()).thenReturn(hasClickHandlers);
        MapPairDisplay mapNodeDisplay = Mockito.mock(MapPairDisplay.class);
        Mockito.when(objectConfigurationMapDisplay.createPair(Matchers.any(), Matchers.any()))
                .thenReturn(mapNodeDisplay);
        PrimitiveDisplay primitiveDisplay = Mockito.mock(PrimitiveDisplay.class);
        Mockito.when(mapNodeDisplay.getKeyDisplay()).thenReturn(Mockito.mock(PrimitiveDisplay.class));
        ClassDisplay subClassDisplay = Mockito.mock(ClassDisplay.class);
        ClassDisplay valueDisplay = subClassDisplay;
        Mockito.when(mapNodeDisplay.getValueDisplay()).thenReturn(valueDisplay);
        Mockito.when(valueDisplay.getImplementingClassDisplay())
                .thenReturn(Mockito.mock(ImplementingClassDisplay.class));
        Mockito.when(valueDisplay.createClassChild(Matchers.anyBoolean())).thenReturn(subClassDisplay);
        Mockito.when(mapNodeDisplay.removePairClickHandler()).thenReturn(Mockito.mock(HasClickHandlers.class));
        Mockito.when(subClassDisplay.getImplementingClassDisplay())
                .thenReturn(Mockito.mock(ImplementingClassDisplay.class));
        // Mockito.when(mapNodeDisplay.createPrimitiveChild(Matchers.eq(false))).thenReturn(primitiveDisplay);
        ClassDisplay classDisplay = subClassDisplay;
        // Mockito.when(mapNodeDisplay.createClassChild(false)).thenReturn(classDisplay);
        Mockito.when(classDisplay.createClassChild(false)).thenReturn(classDisplay);
        ListRootDisplay listDisplay = Mockito.mock(ListRootDisplay.class);
        Mockito.when(classDisplay.createListChild(Matchers.anyBoolean())).thenReturn(listDisplay);
        Mockito.when(listDisplay.addElementClickHandler()).thenReturn(hasClickHandlers);
        Mockito.when(listDisplay.createClassChild(false)).thenReturn(classDisplay);
        Mockito.when(listDisplay.createPrimitiveChild(Matchers.anyString(), Matchers.eq(false)))
                .thenReturn(primitiveDisplay);
        StringListDisplay stringListDisplay = Mockito.mock(StringListDisplay.class);
        Mockito.when(listDisplay.createStringListChild(Matchers.anyBoolean())).thenReturn(stringListDisplay);
        MapRootDisplay mapRootDisplay = objectConfigurationMapDisplay;
        Mockito.when(listDisplay.createMapChild(Matchers.anyBoolean())).thenReturn(mapRootDisplay);
        Mockito.when(classDisplay.createMapChild(Matchers.anyBoolean())).thenReturn(mapRootDisplay);
        // Mockito.when(mapNodeDisplay.createMapChild(Matchers.anyBoolean())).thenReturn(mapRootDisplay);
        Mockito.when(mapRootDisplay.addElementClickHandler()).thenReturn(hasClickHandlers);
        Mockito.when(classDisplay.createStringListChild(Matchers.anyBoolean())).thenReturn(stringListDisplay);
        // Mockito.when(mapNodeDisplay.createStringListChild(Matchers.anyBoolean())).thenReturn(stringListDisplay);
        Mockito.when(classDisplay.createPrimitiveChild(Matchers.anyString(), Matchers.eq(false)))
                .thenReturn(primitiveDisplay);
        ImplementingClassDisplay display = Mockito.mock(ImplementingClassDisplay.class);
        Mockito.when(classDisplay.getImplementingClassDisplay()).thenReturn(display);

        ObjectConfigurationMapPresenter objectConfigurationMapPresenter = new SimpleObjectConfigurationMapPresenter(
                gwtReflectionService, fieldName, objectConfigurationMapDisplay, null);

        final String context = "testreflection-config.xml";
        XMLBeanDefinitionParser parser = new XMLBeanDefinitionParser(context);
        ObjectConfigurationMap objectConfigurationMap = parser.parse();

        ObjectConfiguration aCP = objectConfigurationMap.get("accessibleClassProvider");
        Assert.assertNotNull(aCP);
        ElementConfiguration providers = aCP.getFields().get("providers");
        Assert.assertNotNull(providers);
        ListConfiguration providersList = (ListConfiguration) providers;
        ElementConfiguration cacheProvider = providersList.getListConfiguration().get(1);
        Assert.assertTrue(cacheProvider instanceof ObjectConfiguration);

        objectConfigurationMapPresenter.addObjectConfigurationMap(null, objectConfigurationMap);

        ObjectConfigurationMap result = objectConfigurationMapPresenter.getObjectConfigurationMap(null,
                objectConfigurationFactory);
        Assert.assertTrue(result instanceof ObjectConfigurationMapBean);
        Assert.assertNotNull(result);
        Assert.assertEquals(1, result.size());
        Assert.assertTrue(result.containsKey("accessibleClassProvider"));
        Assert.assertEquals(objectConfigurationMap.size(), result.size());

    }
}

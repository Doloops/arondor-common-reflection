package com.arondor.common.reflection.gwt.client.presenter;

import java.util.HashMap;
import java.util.logging.Logger;

import com.arondor.common.reflection.bean.config.ObjectConfigurationFactoryBean;
import com.arondor.common.reflection.gwt.client.service.GWTReflectionService;
import com.arondor.common.reflection.gwt.client.service.GWTReflectionServiceAsync;
import com.arondor.common.reflection.gwt.client.view.HierarchicAccessibleClassView;
import com.arondor.common.reflection.model.config.ElementConfiguration;
import com.arondor.common.reflection.model.config.MapConfiguration;
import com.arondor.common.reflection.model.config.ObjectConfiguration;
import com.arondor.common.reflection.model.config.ObjectConfigurationFactory;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.IsWidget;

public class ClassDesignerPresenter
{
    private static final Logger LOG = Logger.getLogger(ClassDesignerPresenter.class.getName());

    private final ObjectConfigurationFactory objectConfigurationFactory = new ObjectConfigurationFactoryBean();

    public interface Display extends IsWidget
    {
        HasClickHandlers getGetConfigButton();

        HasClickHandlers getSetConfigButton();

        void setAccessibleClassView(HierarchicAccessibleClassPresenter.Display classDisplay);
    }

    private HierarchicAccessibleClassPresenter classPresenter;

    private final Display display;

    public ClassDesignerPresenter(Display view)
    {
        this.display = view;

        bind();
        String baseClassName = "com.arondor.common.reflection.gwt.server.samples.ParentTestClass";
        GWTReflectionServiceAsync rpcService = GWT.create(GWTReflectionService.class);
        classPresenter = new HierarchicAccessibleClassPresenter(rpcService, baseClassName,
                new HierarchicAccessibleClassView());
        display.setAccessibleClassView((HierarchicAccessibleClassView) classPresenter.getDisplay());
    }

    public void bind()
    {

        display.getSetConfigButton().addClickHandler(new ClickHandler()
        {
            public void onClick(ClickEvent event)
            {
                ObjectConfiguration childObjectConfiguration = objectConfigurationFactory.createObjectConfiguration();
                childObjectConfiguration.setClassName("com.arondor.common.reflection.gwt.server.samples.TestClass");

                childObjectConfiguration.setFields(new HashMap<String, ElementConfiguration>());

                childObjectConfiguration.getFields().put("aStringProperty",
                        objectConfigurationFactory.createPrimitiveConfiguration("test"));
                childObjectConfiguration.getFields().put("aLongProperty",
                        objectConfigurationFactory.createPrimitiveConfiguration("123"));

                ObjectConfiguration parentObjectConfiguration = objectConfigurationFactory.createObjectConfiguration();
                parentObjectConfiguration
                        .setClassName("com.arondor.common.reflection.gwt.server.samples.ParentTestClass");
                parentObjectConfiguration.setFields(new HashMap<String, ElementConfiguration>());
                parentObjectConfiguration.getFields().put("primitiveField",
                        objectConfigurationFactory.createPrimitiveConfiguration("My Primitive Field"));
                parentObjectConfiguration.getFields().put("testInterfaceField", childObjectConfiguration);

                classPresenter.setObjectConfiguration(parentObjectConfiguration);

                ObjectConfiguration otherObject = objectConfigurationFactory.createObjectConfiguration();
                otherObject.setClassName("com.arondor.common.reflection.gwt.server.samples.TestClassBis");
                parentObjectConfiguration.getFields().put("testClassBisField", otherObject);

                MapConfiguration stringMapConfiguration = objectConfigurationFactory.createMapConfiguration();
                otherObject.setFields(new HashMap<String, ElementConfiguration>());
                otherObject.getFields().put("stringMapField", stringMapConfiguration);

                stringMapConfiguration.setMapConfiguration(new HashMap<ElementConfiguration, ElementConfiguration>());
                stringMapConfiguration.getMapConfiguration().put(
                        objectConfigurationFactory.createPrimitiveConfiguration("key1"),
                        objectConfigurationFactory.createPrimitiveConfiguration("value1"));
                stringMapConfiguration.getMapConfiguration().put(
                        objectConfigurationFactory.createPrimitiveConfiguration("key2"),
                        objectConfigurationFactory.createPrimitiveConfiguration("value2"));
            }
        });

        display.getGetConfigButton().addClickHandler(new ClickHandler()
        {
            public void onClick(ClickEvent event)
            {
                ObjectConfiguration objectConfiguration = classPresenter
                        .getObjectConfiguration(objectConfigurationFactory);
                LOG.info("GET - " + objectConfiguration);
            }
        });

    }

    public Display getDisplay()
    {
        return display;
    }
}

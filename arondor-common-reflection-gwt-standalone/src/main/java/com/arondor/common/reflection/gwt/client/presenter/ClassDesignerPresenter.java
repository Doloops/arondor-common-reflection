package com.arondor.common.reflection.gwt.client.presenter;

import java.util.HashMap;
import java.util.logging.Logger;

import com.arondor.common.reflection.bean.config.ObjectConfigurationFactoryBean;
import com.arondor.common.reflection.gwt.client.service.GWTReflectionService;
import com.arondor.common.reflection.gwt.client.service.GWTReflectionServiceAsync;
import com.arondor.common.reflection.gwt.client.view.AccessibleClassView;
import com.arondor.common.reflection.model.config.ElementConfiguration;
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

        AccessibleClassView getAccessibleClassView();

        void setAccessibleClassView(AccessibleClassView accessibleClassView);
    }

    private SimpleAccessibleClassPresenter classPresenter;

    private final Display display;

    public ClassDesignerPresenter(Display view)
    {
        this.display = view;

        bind();
        classPresenter = new SimpleAccessibleClassPresenter(
                (GWTReflectionServiceAsync) GWT.create(GWTReflectionService.class), new AccessibleClassView());
        display.setAccessibleClassView((AccessibleClassView) classPresenter.getDisplay());
        classPresenter.setBaseClassName("com.arondor.common.reflection.gwt.server.samples.TestClass");

    }

    public void bind()
    {
        display.getGetConfigButton().addClickHandler(new ClickHandler()
        {
            public void onClick(ClickEvent event)
            {

                ObjectConfigurationFactory objectConfigurationFactory = new ObjectConfigurationFactoryBean();

                ObjectConfiguration objectConfiguration = objectConfigurationFactory.createObjectConfiguration();
                objectConfiguration.setClassName("com.arondor.common.reflection.gwt.server.samples.TestClass");

                objectConfiguration.setFields(new HashMap<String, ElementConfiguration>());

                objectConfiguration.getFields().put("aStringProperty",
                        objectConfigurationFactory.createPrimitiveConfiguration("test"));
                objectConfiguration.getFields().put("aLongProperty",
                        objectConfigurationFactory.createPrimitiveConfiguration("123"));

                classPresenter.setObjectConfiguration(objectConfiguration);
            }
        });

        display.getSetConfigButton().addClickHandler(new ClickHandler()
        {
            public void onClick(ClickEvent event)
            {
                ObjectConfiguration objectConfiguration = classPresenter
                        .getObjectConfiguration(objectConfigurationFactory);
                LOG.info("objectConfiguration=> " + objectConfiguration);
                classPresenter.setObjectConfiguration(objectConfiguration);
            }
        });
    }

    public Display getDisplay()
    {
        return display;
    }
}

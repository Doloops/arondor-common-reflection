package com.arondor.common.reflection.gwt.client.presenter;

import java.util.HashMap;
import java.util.logging.Logger;

import com.arondor.common.reflection.bean.config.ObjectConfigurationFactoryBean;
import com.arondor.common.reflection.gwt.client.AccessibleClassService;
import com.arondor.common.reflection.gwt.client.AccessibleClassServiceAsync;
import com.arondor.common.reflection.gwt.client.view.AccessibleClassView;
import com.arondor.common.reflection.model.config.ElementConfiguration;
import com.arondor.common.reflection.model.config.ObjectConfiguration;
import com.arondor.common.reflection.model.config.ObjectConfigurationFactory;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.RootPanel;

public class ClassDesignerPresenter implements Presenter
{
    private static final Logger LOG = Logger.getLogger(ClassDesignerPresenter.class.getName());

    public interface Display extends IsWidget
    {
        HasClickHandlers getGetConfigButton();

        HasClickHandlers getSetConfigButton();

        AccessibleClassView getAccessibleClassView();

        void setAccessibleClassView(AccessibleClassView accessibleClassView);
    }

    private AccessibleClassPresenter classPresenter;

    private final HandlerManager eventBus;

    private final Display display;

    public ClassDesignerPresenter(HandlerManager eventBus, Display view)
    {
        this.eventBus = eventBus;
        this.display = view;

        bind();
        RootPanel.get().clear();
        RootPanel.get().add(display.asWidget());
        classPresenter = new AccessibleClassPresenter(
                (AccessibleClassServiceAsync) GWT.create(AccessibleClassService.class), eventBus,
                new AccessibleClassView());
        display.setAccessibleClassView((AccessibleClassView) classPresenter.getDisplay());
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
                classPresenter.doSave();
            }
        });
    }

    public Display getDisplay()
    {
        return display;
    }
}

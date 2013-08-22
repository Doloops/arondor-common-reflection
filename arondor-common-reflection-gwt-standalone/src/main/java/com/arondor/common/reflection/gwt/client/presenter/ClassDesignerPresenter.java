package com.arondor.common.reflection.gwt.client.presenter;

import java.util.logging.Logger;

import com.arondor.common.reflection.bean.config.ObjectConfigurationFactoryBean;
import com.arondor.common.reflection.gwt.client.service.GWTReflectionService;
import com.arondor.common.reflection.gwt.client.service.GWTReflectionServiceAsync;
import com.arondor.common.reflection.gwt.client.view.AccessibleClassView;
import com.arondor.common.reflection.model.config.ObjectConfigurationFactory;
import com.google.gwt.core.client.GWT;
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

    private HierarchicAccessibleClassPresenter classPresenter;

    private final Display display;

    public ClassDesignerPresenter(Display view)
    {
        this.display = view;

        bind();
        classPresenter = new HierarchicAccessibleClassPresenter(
                (GWTReflectionServiceAsync) GWT.create(GWTReflectionService.class), new AccessibleClassView());
        display.setAccessibleClassView((AccessibleClassView) classPresenter.getDisplay());
        classPresenter.setBaseClassName("com.arondor.common.reflection.gwt.server.samples.ParentTestClass");

    }

    public void bind()
    {

    }

    public Display getDisplay()
    {
        return display;
    }
}

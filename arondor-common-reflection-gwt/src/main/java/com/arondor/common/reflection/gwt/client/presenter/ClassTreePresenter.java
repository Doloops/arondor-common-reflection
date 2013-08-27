package com.arondor.common.reflection.gwt.client.presenter;

import com.arondor.common.reflection.gwt.client.service.GWTReflectionServiceAsync;
import com.arondor.common.reflection.model.config.ObjectConfigurationMap;
import com.google.gwt.user.client.ui.HasVisibility;
import com.google.gwt.user.client.ui.IsWidget;

public class ClassTreePresenter
{
    public interface Display extends IsWidget
    {
        ClassTreeNodePresenter.ClassDisplay createRootView(String baseClassName);
    }

    private TreeNodePresenter rootNodePresenter;

    private final Display display;

    public ClassTreePresenter(GWTReflectionServiceAsync rpcService, ObjectConfigurationMap objectConfigurationMap,
            String baseClassName, Display view)
    {
        this.display = view;

        setRootNodePresenter(new ClassTreeNodePresenter(rpcService, objectConfigurationMap, baseClassName,
                display.createRootView(baseClassName)));

        if (getRootNodePresenter().getDisplay() instanceof HasVisibility)
        {
            ((HasVisibility) getRootNodePresenter().getDisplay()).setVisible(true);
        }
        bind();
    }

    private void bind()
    {

    }

    public TreeNodePresenter getRootNodePresenter()
    {
        return rootNodePresenter;
    }

    public void setRootNodePresenter(ClassTreeNodePresenter rootNodePresenter)
    {
        this.rootNodePresenter = rootNodePresenter;
    }

    public Display getDisplay()
    {
        return display;
    }

    public IsWidget getDisplayWidget()
    {
        return getDisplay();
    }
}

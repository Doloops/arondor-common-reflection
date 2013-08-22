package com.arondor.common.reflection.gwt.client.presenter;

import java.util.logging.Logger;

import com.arondor.common.reflection.gwt.client.api.AccessibleClassPresenter;
import com.arondor.common.reflection.gwt.client.service.GWTReflectionServiceAsync;
import com.arondor.common.reflection.model.config.ObjectConfiguration;
import com.arondor.common.reflection.model.config.ObjectConfigurationFactory;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.IsWidget;

public class HierarchicAccessibleClassPresenter implements AccessibleClassPresenter
{
    private static final Logger LOG = Logger.getLogger(HierarchicAccessibleClassPresenter.class.getName());

    public interface Display extends IsWidget
    {
        ClassTreePresenter.Display getClassTreeDisplay();

        void setName(String name);

        void setClassname(String classname);

        void setCurrentSelectedNode(ClassTreeNodePresenter.Display nodeDisplay);
    }

    private final Display display;

    private final ClassTreePresenter classTreePresenter;

    public HierarchicAccessibleClassPresenter(GWTReflectionServiceAsync rpcService, String baseClassName, Display view)
    {
        this.display = view;
        this.classTreePresenter = new ClassTreePresenter(rpcService, baseClassName, display.getClassTreeDisplay());
        bind();

        display.setCurrentSelectedNode(classTreePresenter.getRootNodePresenter().getDisplay());
    }

    public void bind()
    {
        classTreePresenter.addSelectionHandler(new SelectionHandler<ClassTreeNodePresenter>()
        {
            public void onSelection(SelectionEvent<ClassTreeNodePresenter> event)
            {
                LOG.info("Selected : " + event.getSelectedItem().getBaseClassName());
                display.setCurrentSelectedNode(event.getSelectedItem().getDisplay());
            }
        });
    }

    public ObjectConfiguration getObjectConfiguration(ObjectConfigurationFactory objectConfigurationFactory)
    {
        return classTreePresenter.getRootNodePresenter().getObjectConfiguration(objectConfigurationFactory);
    }

    public String getBaseClassName()
    {
        return classTreePresenter.getRootNodePresenter().getBaseClassName();
    }

    public void setObjectConfiguration(ObjectConfiguration objectConfiguration)
    {
        classTreePresenter.getRootNodePresenter().setObjectConfiguration(objectConfiguration);
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

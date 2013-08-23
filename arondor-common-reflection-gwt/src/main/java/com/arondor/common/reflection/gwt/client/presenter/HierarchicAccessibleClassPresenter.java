package com.arondor.common.reflection.gwt.client.presenter;

import java.util.logging.Logger;

import com.arondor.common.reflection.gwt.client.api.AccessibleClassPresenter;
import com.arondor.common.reflection.gwt.client.service.GWTReflectionServiceAsync;
import com.arondor.common.reflection.model.config.ElementConfiguration;
import com.arondor.common.reflection.model.config.ObjectConfiguration;
import com.arondor.common.reflection.model.config.ObjectConfigurationFactory;
import com.google.gwt.user.client.ui.IsWidget;

public class HierarchicAccessibleClassPresenter implements AccessibleClassPresenter
{
    private static final Logger LOG = Logger.getLogger(HierarchicAccessibleClassPresenter.class.getName());

    public interface Display extends IsWidget
    {
        ClassTreePresenter.Display getClassTreeDisplay();
    }

    private final Display display;

    private final ClassTreePresenter classTreePresenter;

    public HierarchicAccessibleClassPresenter(GWTReflectionServiceAsync rpcService, String baseClassName, Display view)
    {
        this.display = view;
        this.classTreePresenter = new ClassTreePresenter(rpcService, baseClassName, display.getClassTreeDisplay());
        bind();
    }

    private void bind()
    {

    }

    public ObjectConfiguration getObjectConfiguration(ObjectConfigurationFactory objectConfigurationFactory)
    {
        ElementConfiguration elementConfiguration = classTreePresenter.getRootNodePresenter().getElementConfiguration(
                objectConfigurationFactory);
        LOG.finest("got root elementConfiguration=" + elementConfiguration);
        if (elementConfiguration == null)
        {
            return null;
        }
        if (elementConfiguration instanceof ObjectConfiguration)
        {
            return (ObjectConfiguration) elementConfiguration;
        }
        throw new IllegalArgumentException("Not supported ! elementConfiguration=" + elementConfiguration);
    }

    public void setObjectConfiguration(ObjectConfiguration objectConfiguration)
    {
        classTreePresenter.getRootNodePresenter().setElementConfiguration(objectConfiguration);
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

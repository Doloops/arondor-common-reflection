package com.arondor.common.reflection.gwt.client.api;

import com.arondor.common.reflection.model.config.ObjectConfiguration;
import com.arondor.common.reflection.model.config.ObjectConfigurationFactory;
import com.google.gwt.user.client.ui.IsWidget;

public interface AccessibleClassPresenter
{
    void setBaseClassName(String className);

    String getBaseClassName();

    void setObjectConfiguration(ObjectConfiguration objectConfiguration);

    ObjectConfiguration getObjectConfiguration(ObjectConfigurationFactory objectConfigurationFactory);

    IsWidget getDisplayWidget();
}

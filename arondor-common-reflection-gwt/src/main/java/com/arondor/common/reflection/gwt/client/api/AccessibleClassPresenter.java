package com.arondor.common.reflection.gwt.client.api;

import com.arondor.common.reflection.model.config.ObjectConfiguration;
import com.arondor.common.reflection.model.config.ObjectConfigurationFactory;
import com.google.gwt.user.client.ui.IsWidget;

public interface AccessibleClassPresenter
{
    String getBaseClassName();

    void setObjectConfiguration(ObjectConfiguration objectConfiguration);

    ObjectConfiguration getObjectConfiguration(ObjectConfigurationFactory objectConfigurationFactory);

    IsWidget getDisplayWidget();
}

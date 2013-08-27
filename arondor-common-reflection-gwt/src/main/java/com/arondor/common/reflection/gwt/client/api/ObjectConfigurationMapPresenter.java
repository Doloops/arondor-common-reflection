package com.arondor.common.reflection.gwt.client.api;

import com.arondor.common.reflection.model.config.ObjectConfigurationFactory;
import com.arondor.common.reflection.model.config.ObjectConfigurationMap;
import com.google.gwt.user.client.ui.IsWidget;

public interface ObjectConfigurationMapPresenter
{
    void setObjectConfigurationMap(ObjectConfigurationMap objectConfigurationMap);

    ObjectConfigurationMap getObjectConfigurationMap(ObjectConfigurationFactory objectConfigurationFactory);

    IsWidget getDisplayWidget();
}

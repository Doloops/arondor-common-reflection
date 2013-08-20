package com.arondor.common.reflection.gwt.client.presenter;

import com.arondor.common.reflection.model.config.ObjectConfiguration;
import com.google.gwt.user.client.ui.IsWidget;

public interface AccessibleClassPresenter
{
    void setObjectClassName(String className);

    void setObjectConfiguration(ObjectConfiguration objectConfiguration);

    ObjectConfiguration getObjectConfiguration();

    IsWidget getDisplayWidget();
}

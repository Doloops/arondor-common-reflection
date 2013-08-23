package com.arondor.common.reflection.gwt.client.presenter;

import com.arondor.common.reflection.model.config.ElementConfiguration;
import com.arondor.common.reflection.model.config.ObjectConfigurationFactory;

public interface TreeNodePresenter
{
    public interface Display
    {
        void setNodeName(String name);

        void setNodeDescription(String description);

        void clear();

    }

    String getFieldName();

    ElementConfiguration getElementConfiguration(ObjectConfigurationFactory objectConfigurationFactory);

    void setElementConfiguration(ElementConfiguration elementConfiguration);

    Display getDisplay();
}

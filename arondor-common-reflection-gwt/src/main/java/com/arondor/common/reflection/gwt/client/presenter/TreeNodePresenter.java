package com.arondor.common.reflection.gwt.client.presenter;

import com.arondor.common.reflection.model.config.ElementConfiguration;
import com.arondor.common.reflection.model.config.ObjectConfigurationFactory;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;

public interface TreeNodePresenter
{
    public interface Display
    {
        void setNodeName(String name);

        void setNodeDescription(String description);

        void setActive(boolean active);

        void clear();
    }

    public interface ValueDisplay<T> extends Display
    {
        void setValue(T value);

        HandlerRegistration addValueChangeHandler(ValueChangeHandler<T> valueChangeHandler);
    }

    String getFieldName();

    ElementConfiguration getElementConfiguration(ObjectConfigurationFactory objectConfigurationFactory);

    void setElementConfiguration(ElementConfiguration elementConfiguration);

    Display getDisplay();
}

package com.arondor.common.reflection.gwt.client.presenter;

import com.arondor.common.reflection.gwt.client.event.TreeNodeClearEvent;
import com.arondor.common.reflection.gwt.client.presenter.ClassTreeNodePresenter.ClassDisplay;
import com.arondor.common.reflection.gwt.client.presenter.fields.ListTreeNodePresenter;
import com.arondor.common.reflection.gwt.client.presenter.fields.MapTreeNodePresenter;
import com.arondor.common.reflection.gwt.client.presenter.fields.PrimitiveTreeNodePresenter;
import com.arondor.common.reflection.gwt.client.presenter.fields.StringListTreeNodePresenter;
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

        boolean isActive();

        void clear();

        void addTreeNodeClearHandler(TreeNodeClearEvent.Handler hanlder);
    }

    public interface ValueDisplay<T> extends Display
    {
        void setValue(T value);

        HandlerRegistration addValueChangeHandler(ValueChangeHandler<T> valueChangeHandler);
    }

    public interface ChildCreatorDisplay extends Display
    {
        ClassDisplay createClassChild();

        PrimitiveTreeNodePresenter.PrimitiveDisplay createPrimitiveChild(String fieldClassName);

        StringListTreeNodePresenter.StringListDisplay createStringListChild();

        MapTreeNodePresenter.MapRootDisplay createMapChild();

        ListTreeNodePresenter.ListRootDisplay createListChild();
    }

    String getFieldName();

    ElementConfiguration getElementConfiguration(ObjectConfigurationFactory objectConfigurationFactory);

    void setElementConfiguration(ElementConfiguration elementConfiguration);

    Display getDisplay();
}

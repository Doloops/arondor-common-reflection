/*
 *  Copyright 2013, Arondor
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.arondor.common.reflection.gwt.client.presenter;

import com.arondor.common.reflection.gwt.client.event.TreeNodeClearEvent;
import com.arondor.common.reflection.gwt.client.presenter.ClassTreeNodePresenter.ClassDisplay;
import com.arondor.common.reflection.gwt.client.presenter.fields.EnumTreeNodePresenter;
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

        void addTreeNodeClearHandler(TreeNodeClearEvent.Handler handler);
    }

    public interface ValueDisplay<T> extends Display
    {
        void setValue(T value);

        void setDefaultValue(T value);

        HandlerRegistration addValueChangeHandler(ValueChangeHandler<T> valueChangeHandler);

    }

    public interface ChildCreatorDisplay extends Display
    {
        ClassDisplay createClassChild(boolean isMandatory);

        PrimitiveTreeNodePresenter.PrimitiveDisplay createPrimitiveChild(String fieldClassName, boolean isMandatory);

        EnumTreeNodePresenter.EnumDisplay createEnumListChild();

        StringListTreeNodePresenter.StringListDisplay createStringListChild();

        MapTreeNodePresenter.MapRootDisplay createMapChild();

        ListTreeNodePresenter.ListRootDisplay createListChild();
    }

    String getFieldName();

    ElementConfiguration getElementConfiguration(ObjectConfigurationFactory objectConfigurationFactory);

    void setElementConfiguration(ElementConfiguration elementConfiguration);

    Display getDisplay();
}

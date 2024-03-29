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

import java.util.Collection;

import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.IsWidget;

public interface ImplementingClassPresenter
{
    public interface ImplementingClassDisplay extends IsWidget
    {
        void setImplementingClasses(Collection<ImplementingClass> implementingClasses);

        void selectImplementingClass(ImplementingClass implementingClass);

        HandlerRegistration addValueChangeHandler(ValueChangeHandler<ImplementingClass> valueChangeHandler);

        HandlerRegistration onOpenImplementingClasses(Command command);

        void setNodeDescription(String label);

        void reset();

        void setNodeLongDescription(String longDescription);

        FocusPanel getSharedObjectCreatePanel();

        FocusPanel getSharedObjectForwardPanel();
    }

    HandlerRegistration addValueChangeHandler(ValueChangeHandler<ImplementingClass> valueChangeHandler);

    void setImplementingClass(ImplementingClass nullClass);

    String getBaseClassName();

    ImplementingClass getImplementingClass();

}

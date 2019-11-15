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
package com.arondor.common.reflection.gwt.client.view;

import java.util.Collection;
import java.util.logging.Logger;

import com.arondor.common.reflection.gwt.client.presenter.ImplementingClassPresenter.ImplementingClassDisplay;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ListBox;

public class ImplementingClassView extends Composite implements ImplementingClassDisplay
{
    private static final Logger LOG = Logger.getLogger(ImplementingClassView.class.getName());

    private ListBox implementingListInput = new ListBox();

    private String selectedClass = null;

    public ImplementingClassView()
    {
        initWidget(implementingListInput);
        implementingListInput.getElement().addClassName("implementingClassSelect");
    }

    @Override
    public void setImplementingClasses(Collection<String> implementingClasses)
    {
        LOG.finest("Selected classes : " + implementingClasses);
        implementingListInput.clear();
        for (String implementingClass : implementingClasses)
        {
            implementingListInput.addItem(implementingClass);
            if (selectedClass != null && selectedClass.equals(implementingClass))
            {
                implementingListInput.setSelectedIndex(implementingListInput.getItemCount() - 1);
            }
        }
    }

    private void doSelect(String className)
    {
        selectedClass = className;
        if (className == null)
        {
            className = "null";
        }

        LOG.finest("Selecting class : " + className + " from a choice of " + implementingListInput.getItemCount()
                + " items");
        for (int idx = 0; idx < implementingListInput.getItemCount(); idx++)
        {
            if (implementingListInput.getItemText(idx).equals(className))
            {
                implementingListInput.setSelectedIndex(idx);
                return;
            }
        }

        implementingListInput.addItem(className);
        implementingListInput.setSelectedIndex(implementingListInput.getItemCount() - 1);
        // LOG.warning("Could not select class : " + className);
    }

    @Override
    public HandlerRegistration addValueChangeHandler(final ValueChangeHandler<String> valueChangeHandler)
    {
        return implementingListInput.addChangeHandler(new ChangeHandler()
        {
            @Override
            public void onChange(ChangeEvent event)
            {
                if (implementingListInput.getSelectedIndex() != -1)
                {
                    String value = implementingListInput.getValue(implementingListInput.getSelectedIndex());
                    selectedClass = value;
                    valueChangeHandler.onValueChange(new MyValueChangeEvent<String>(value));
                }
            }
        });
    }

    @Override
    public void setBaseClassName(String baseClassName)
    {
        doSelect(baseClassName);
    }

    @Override
    public void selectImplementingClass(String implementingClassName)
    {
        doSelect(implementingClassName);
    }
}

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

import com.arondor.common.reflection.gwt.client.CssBundle;
import com.arondor.common.reflection.gwt.client.presenter.ImplementingClass;
import com.arondor.common.reflection.gwt.client.presenter.ImplementingClassPresenter.ImplementingClassDisplay;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;

import gwt.material.design.client.ui.MaterialListValueBox;

public class ImplementingClassView extends Composite implements ImplementingClassDisplay
{
    private static final Logger LOG = Logger.getLogger(ImplementingClassView.class.getName());

    // private ListBox implementingListInput = new ListBox();

    private MaterialListValueBox<ImplementingClass> implementingListInput = new MaterialListValueBox<ImplementingClass>();

    private ImplementingClass selectedClass = ImplementingClass.NULL_CLASS;

    public ImplementingClassView()
    {
        initWidget(implementingListInput);

        implementingListInput.setClass("outlined");
        implementingListInput.getLabel().setStyle("top:0px");
        implementingListInput.setStyle("width:100%;margin-top:0px;");
        implementingListInput.getListBox().getElement().addClassName(CssBundle.INSTANCE.css().dropdownItems());
        // implementingListInput.getElement().addClassName(CssBundle.INSTANCE.css().implementingClassView());
    }

    @Override
    public void setImplementingClasses(Collection<ImplementingClass> implementingClasses)
    {
        LOG.finest("Selected classes : " + implementingClasses);
        implementingListInput.clear();
        for (ImplementingClass implementingClass : implementingClasses)
        {
            implementingListInput.addItem(implementingClass, implementingClass.toString());
            if (selectedClass != null && selectedClass.equals(implementingClass))
            {
                implementingListInput.setSelectedIndex(implementingListInput.getItemCount() - 1);
            }
        }
    }

    private void doSelect(ImplementingClass clazz)
    {
        selectedClass = clazz;
        if (clazz == ImplementingClass.NULL_CLASS)
        {
            implementingListInput.setSelectedIndex(-1);
        }
        LOG.finest(
                "Selecting class : " + clazz + " from a choice of " + implementingListInput.getItemCount() + " items");
        int index = implementingListInput.getIndex(clazz);
        if (index == -1)
        {
            implementingListInput.addItem(clazz, clazz.toString());
            implementingListInput.setSelectedIndex(implementingListInput.getItemCount() - 1);
        }
        else
        {
            implementingListInput.setSelectedIndex(index);
        }
        // LOG.warning("Could not select class : " + className);
    }

    @Override
    public HandlerRegistration addValueChangeHandler(final ValueChangeHandler<ImplementingClass> valueChangeHandler)
    {
        return implementingListInput.addValueChangeHandler(new ValueChangeHandler<ImplementingClass>()
        {
            @Override
            public void onValueChange(ValueChangeEvent<ImplementingClass> event)
            {
                selectedClass = event.getValue();
                LOG.finest("&& Selected class : " + selectedClass);
                valueChangeHandler.onValueChange(event);
            }
        });
    }

    @Override
    public void setBaseClassName(String baseClassName)
    {
        // doSelect(baseClassName);
    }

    @Override
    public void selectImplementingClass(ImplementingClass implementingClassName)
    {
        doSelect(implementingClassName);
    }

    @Override
    public void setProperLabel(String label)
    {
        LOG.info("&& setting label :" + label);
        implementingListInput.setPlaceholder(label);
    }
}

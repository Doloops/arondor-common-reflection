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

import java.util.List;
import java.util.logging.Logger;

import com.arondor.common.reflection.gwt.client.presenter.fields.EnumTreeNodePresenter.EnumDisplay;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.UIObject;

public class EnumListView extends AbstractTreeNodeView implements EnumDisplay
{
    private static final Logger LOG = Logger.getLogger(EnumListView.class.getName());

    private ListBox enumListInput = new ListBox();

    private final static String NULL_VALUE = "null";

    public EnumListView(UIObject parentNode)
    {
        super(parentNode);
        setHasRemoveButton(true);
        getContents().add(enumListInput.asWidget());
    }

    @Override
    public void initEnumList(List<String> enumList)
    {
        LOG.finest("Selected enum values : " + enumList);
        enumListInput.clear();
        enumListInput.addItem(NULL_VALUE);
        for (String enumValue : enumList)
        {
            enumListInput.addItem(enumValue);
        }
    }

    private void doSelect(String value)
    {

        if (value == null)
        {
            LOG.finest("No enum value selected, return");
            return;
        }
        LOG.finest("Selecting enum : " + value + " from a choice of " + enumListInput.getItemCount() + " items");
        for (int idx = 0; idx < enumListInput.getItemCount(); idx++)
        {
            if (enumListInput.getItemText(idx).equals(value))
            {
                enumListInput.setSelectedIndex(idx);
                return;
            }
        }
        // enumListInput.addItem(value);
        // enumListInput.setSelectedIndex(enumListInput.getItemCount() - 1);

    }

    @Override
    public HandlerRegistration addValueChangeHandler(final ValueChangeHandler<String> valueChangeHandler)
    {
        return enumListInput.addChangeHandler(new ChangeHandler()
        {
            @Override
            public void onChange(ChangeEvent event)
            {
                if (enumListInput.getSelectedIndex() != -1)
                {
                    String value = enumListInput.getValue(enumListInput.getSelectedIndex());
                    setActive(true);
                    valueChangeHandler.onValueChange(new MyValueChangeEvent<String>(value));
                }
            }
        });
    }

    @Override
    public void setValue(String value)
    {
        doSelect(value);
        if (NULL_VALUE.equals(value))
        {
            setActive(false);
        }
        else
        {
            setActive(true);
        }
    }

    @Override
    public void setDefaultValue(String defaultValue)
    {
        doSelect(defaultValue);
    }

    @Override
    public void clear()
    {
        int idx = enumListInput.getSelectedIndex();
        LOG.finest("Item " + idx + " is selected, deselect it");
        enumListInput.setItemSelected(idx, false);
    }

    @Override
    public void setPlaceholder(String value)
    {
        // TODO Auto-generated method stub

    }
}

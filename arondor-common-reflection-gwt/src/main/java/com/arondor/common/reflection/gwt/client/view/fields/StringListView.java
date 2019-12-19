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
package com.arondor.common.reflection.gwt.client.view.fields;

import java.util.ArrayList;
import java.util.List;

import com.arondor.common.reflection.gwt.client.presenter.fields.StringListTreeNodePresenter.StringListDisplay;
import com.arondor.common.reflection.gwt.client.view.AbstractTreeNodeView;
import com.arondor.common.reflection.gwt.client.view.MyValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.UIObject;

public class StringListView extends AbstractTreeNodeView implements StringListDisplay
{
    private final TextArea textArea = new TextArea();

    public StringListView(UIObject parentNode)
    {
        super(parentNode);
        setHasRemoveButton(true);
        getContents().add(textArea);
        textArea.setWidth("400px");
    }

    @Override
    public void clear()
    {
        textArea.setValue("");
    }

    @Override
    public void setValue(List<String> values)
    {
        StringBuilder stringBuilder = new StringBuilder();
        for (String value : values)
        {
            stringBuilder.append(value);
            stringBuilder.append('\n');
        }
        textArea.setValue(stringBuilder.toString());
        setActive(true);
    }

    @Override
    public HandlerRegistration addValueChangeHandler(final ValueChangeHandler<List<String>> valueChangeHandler)
    {
        return textArea.addValueChangeHandler(new ValueChangeHandler<String>()
        {
            @Override
            public void onValueChange(ValueChangeEvent<String> event)
            {
                setActive(true);
                String[] splitted = event.getValue().split("\n");
                List<String> values = new ArrayList<String>();
                for (String value : splitted)
                {
                    values.add(value);
                }
                valueChangeHandler.onValueChange(new MyValueChangeEvent<List<String>>(values));
            }
        });
    }

    @Override
    public void setDefaultValue(List<String> values)
    {
        StringBuilder stringBuilder = new StringBuilder();
        for (String value : values)
        {
            stringBuilder.append(value);
            stringBuilder.append('\n');
        }
        textArea.setValue(stringBuilder.toString());
    }

    @Override
    public void setPlaceholder(List<String> value)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void setProperLabel(String label)
    {
        // TODO Auto-generated method stub

    }

}

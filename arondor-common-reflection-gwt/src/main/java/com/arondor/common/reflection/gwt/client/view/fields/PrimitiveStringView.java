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

import com.arondor.common.reflection.gwt.client.presenter.fields.PrimitiveTreeNodePresenter;
import com.arondor.common.reflection.gwt.client.view.AbstractTreeNodeView;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.UIObject;

public class PrimitiveStringView extends AbstractTreeNodeView implements PrimitiveTreeNodePresenter.PrimitiveDisplay
{
    private final TextBox textBox = new TextBox();

    public PrimitiveStringView(UIObject parentNode)
    {
        super(parentNode);
        setHasRemoveButton(true);
        getContents().add(textBox.asWidget());
    }

    @Override
    public void clear()
    {
        textBox.setValue("");
    }

    @Override
    public void setValue(String value)
    {
        textBox.setValue(value);
        setActive(true);
    }

    public void setTextWidth(int width)
    {
        textBox.setWidth(width + "px");
    }

    @Override
    public HandlerRegistration addValueChangeHandler(final ValueChangeHandler<String> valueChangeHandler)
    {
        return textBox.addValueChangeHandler(new ValueChangeHandler<String>()
        {
            @Override
            public void onValueChange(ValueChangeEvent<String> event)
            {
                setActive(true);
                valueChangeHandler.onValueChange(event);
            }
        });
    }

    @Override
    public void setDefaultValue(String value)
    {
        textBox.setValue(value);
    }

    @Override
    public void setPlaceholder(String value)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void setProperLabel(String label)
    {
        // TODO Auto-generated method stub

    }

}

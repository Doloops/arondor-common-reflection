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
package com.arondor.common.reflection.gwt.client.presenter.fields;

import java.util.List;

import com.arondor.common.reflection.bean.config.PrimitiveConfigurationBean;
import com.arondor.common.reflection.gwt.client.AccessibleClassPresenterFactory;
import com.arondor.common.reflection.gwt.client.presenter.TreeNodePresenter;
import com.arondor.common.reflection.model.config.ElementConfiguration;
import com.arondor.common.reflection.model.config.ObjectConfiguration;
import com.arondor.common.reflection.model.config.PrimitiveConfiguration;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;

public class EnumTreeNodePresenter implements TreeNodePresenter
{
    private final String fieldName;

    private String fieldValue;

    public interface EnumDisplay extends ValueDisplay<String>
    {

        void initEnumList(List<String> enumList);

    }

    private final EnumDisplay enumDisplay;

    public EnumTreeNodePresenter(String fieldName, EnumDisplay enumDisplay)
    {
        this.fieldName = fieldName;
        this.enumDisplay = enumDisplay;
        bind();
    }

    private void bind()
    {
        enumDisplay.addValueChangeHandler(new ValueChangeHandler<String>()
        {
            @Override
            public void onValueChange(ValueChangeEvent<String> event)
            {
                fieldValue = event.getValue();
            }
        });
    }

    public String getFieldName()
    {
        return fieldName;
    }

    @Override
    public ElementConfiguration getElementConfiguration()
    {
        if (fieldValue != null && !fieldValue.isEmpty())
        {
            return AccessibleClassPresenterFactory.getObjectConfigurationFactory()
                    .createPrimitiveConfiguration(fieldValue);
        }
        return null;
    }

    @Override
    public void setElementConfiguration(ElementConfiguration elementConfiguration)
    {
        if (elementConfiguration instanceof PrimitiveConfiguration)
        {
            PrimitiveConfiguration primitiveConfiguration = (PrimitiveConfiguration) elementConfiguration;
            enumDisplay.setValue(primitiveConfiguration.getValue());
        }
        else if (elementConfiguration instanceof ObjectConfiguration)
        {
            ObjectConfiguration objectConfiguration = (ObjectConfiguration) elementConfiguration;
            if (objectConfiguration.getConstructorArguments() != null
                    && !objectConfiguration.getConstructorArguments().isEmpty())
            {
                ElementConfiguration fieldValue = objectConfiguration.getConstructorArguments().get(0);

                if (fieldValue instanceof PrimitiveConfigurationBean)
                {
                    String value = ((PrimitiveConfigurationBean) fieldValue).getValue();
                    enumDisplay.setValue(value);
                }
            }
        }
    }

    @Override
    public Display getDisplay()
    {
        return enumDisplay;
    }

    public void setEnumValues(List<String> enumValues)
    {
        enumDisplay.initEnumList(enumValues);
    }

}

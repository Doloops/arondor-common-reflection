package com.arondor.common.reflection.gwt.client.presenter.fields;

import com.arondor.common.reflection.gwt.client.presenter.TreeNodePresenter;
import com.arondor.common.reflection.model.config.ElementConfiguration;
import com.arondor.common.reflection.model.config.ObjectConfigurationFactory;
import com.arondor.common.reflection.model.config.PrimitiveConfiguration;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;

public class PrimitiveTreeNodePresenter implements TreeNodePresenter
{
    private final String fieldName;

    private String fieldValue;

    public interface PrimitiveDisplay extends ValueDisplay<String>
    {
    }

    private final PrimitiveDisplay primitiveDisplay;

    public PrimitiveTreeNodePresenter(String fieldName, PrimitiveDisplay primitiveDisplay)
    {
        this.fieldName = fieldName;
        this.primitiveDisplay = primitiveDisplay;
        bind();
    }

    private void bind()
    {
        primitiveDisplay.addValueChangeHandler(new ValueChangeHandler<String>()
        {
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

    public ElementConfiguration getElementConfiguration(ObjectConfigurationFactory objectConfigurationFactory)
    {
        if (fieldValue != null && !fieldValue.isEmpty())
        {
            return objectConfigurationFactory.createPrimitiveConfiguration(fieldValue);
        }
        return null;
    }

    public void setElementConfiguration(ElementConfiguration elementConfiguration)
    {
        if (elementConfiguration instanceof PrimitiveConfiguration)
        {
            PrimitiveConfiguration primitiveConfiguration = (PrimitiveConfiguration) elementConfiguration;
            fieldValue = primitiveConfiguration.getValue();
            primitiveDisplay.setValue(fieldValue);
        }
    }

    public Display getDisplay()
    {
        return primitiveDisplay;
    }

}

package com.arondor.common.reflection.gwt.client.presenter.fields;

import java.util.ArrayList;
import java.util.List;

import com.arondor.common.reflection.gwt.client.presenter.TreeNodePresenter;
import com.arondor.common.reflection.model.config.ElementConfiguration;
import com.arondor.common.reflection.model.config.ListConfiguration;
import com.arondor.common.reflection.model.config.ObjectConfigurationFactory;
import com.arondor.common.reflection.model.config.PrimitiveConfiguration;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;

public class StringListTreeNodePresenter implements TreeNodePresenter
{
    private final String fieldName;

    public interface StringListDisplay extends ValueDisplay<List<String>>
    {
    }

    private final StringListDisplay primitiveDisplay;

    private List<String> values;

    public StringListTreeNodePresenter(String fieldName, StringListDisplay primitiveDisplay)
    {
        this.fieldName = fieldName;
        this.primitiveDisplay = primitiveDisplay;
        bind();
    }

    private void bind()
    {
        primitiveDisplay.addValueChangeHandler(new ValueChangeHandler<List<String>>()
        {
            public void onValueChange(ValueChangeEvent<List<String>> event)
            {
                values = event.getValue();
            }
        });

    }

    public String getFieldName()
    {
        return fieldName;
    }

    public ElementConfiguration getElementConfiguration(ObjectConfigurationFactory objectConfigurationFactory)
    {
        if (values != null)
        {
            ListConfiguration listConfiguration = objectConfigurationFactory.createListConfiguration();
            listConfiguration.setListConfiguration(new ArrayList<ElementConfiguration>());
            for (String value : values)
            {
                listConfiguration.getListConfiguration().add(
                        objectConfigurationFactory.createPrimitiveConfiguration(value));
            }
            return listConfiguration;
        }
        return null;
    }

    public void setElementConfiguration(ElementConfiguration elementConfiguration)
    {
        if (elementConfiguration instanceof ListConfiguration)
        {
            ListConfiguration listConfiguration = (ListConfiguration) elementConfiguration;
            List<String> stringList = new ArrayList<String>();
            for (ElementConfiguration childConfiguration : listConfiguration.getListConfiguration())
            {
                if (childConfiguration instanceof PrimitiveConfiguration)
                {
                    PrimitiveConfiguration primitiveConfiguration = (PrimitiveConfiguration) childConfiguration;
                    stringList.add(primitiveConfiguration.getValue());
                }
            }
            primitiveDisplay.setValue(stringList);
            this.values = stringList;
        }
    }

    public Display getDisplay()
    {
        return primitiveDisplay;
    }

}

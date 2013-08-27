package com.arondor.common.reflection.gwt.client.presenter.fields;

import java.util.ArrayList;
import java.util.List;

import com.arondor.common.reflection.gwt.client.presenter.TreeNodePresenter;
import com.arondor.common.reflection.gwt.client.presenter.TreeNodePresenter.Display;
import com.arondor.common.reflection.gwt.client.presenter.TreeNodePresenter.ValueDisplay;
import com.arondor.common.reflection.model.config.ElementConfiguration;
import com.arondor.common.reflection.model.config.ListConfiguration;
import com.arondor.common.reflection.model.config.ObjectConfigurationFactory;
import com.arondor.common.reflection.model.config.PrimitiveConfiguration;

public class StringListTreeNodePresenter implements TreeNodePresenter
{
    private final String fieldName;

    public interface StringListDisplay extends ValueDisplay<List<String>>
    {
    }

    private final StringListDisplay primitiveDisplay;

    public StringListTreeNodePresenter(String fieldName, StringListDisplay primitiveDisplay)
    {
        this.fieldName = fieldName;
        this.primitiveDisplay = primitiveDisplay;
    }

    public String getFieldName()
    {
        return fieldName;
    }

    public ElementConfiguration getElementConfiguration(ObjectConfigurationFactory objectConfigurationFactory)
    {
        // TODO Auto-generated method stub
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
        }
    }

    public Display getDisplay()
    {
        return primitiveDisplay;
    }

}

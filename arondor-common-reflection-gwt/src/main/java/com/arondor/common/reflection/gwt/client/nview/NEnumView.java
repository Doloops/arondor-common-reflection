package com.arondor.common.reflection.gwt.client.nview;

import java.util.List;

import com.arondor.common.reflection.gwt.client.event.MyValueChangeEvent;
import com.arondor.common.reflection.gwt.client.presenter.fields.EnumTreeNodePresenter.EnumDisplay;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;

import gwt.material.design.addins.client.combobox.MaterialComboBox;

public class NEnumView extends NNodeView implements EnumDisplay
{
    private final MaterialComboBox<String> selectionBox = new MaterialComboBox<String>();

    public NEnumView()
    {
        attachElements();
    }

    private void attachElements()
    {
        add(selectionBox);
        add(getResetFieldBtn());
    }

    @Override
    public void clear()
    {
        super.clear();
        attachElements();
    }

    @Override
    public void setValue(String value)
    {
        int index = selectionBox.getIndexByString(value);

        selectionBox.unselect();
        if (index >= 0)
        {
            selectionBox.setSelectedIndex(index);
        }
    }

    @Override
    public void setDefaultValue(String value)
    {

    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<String> valueChangeHandler)
    {
        return selectionBox.addValueChangeHandler(new ValueChangeHandler<List<String>>()
        {
            @Override
            public void onValueChange(ValueChangeEvent<List<String>> event)
            {
                if (!event.getValue().isEmpty())
                {
                    String first = event.getValue().get(0);
                    valueChangeHandler.onValueChange(new MyValueChangeEvent<String>(first));
                }
            }
        });
    }

    @Override
    public void initEnumList(List<String> enumList)
    {
        for (String value : enumList)
        {
            selectionBox.addItem(value);
        }
        selectionBox.unselect();
    }

    @Override
    public void setPlaceholder(String value)
    {
        selectionBox.setPlaceholder(value);
    }

    @Override
    public void setNodeDescription(String description)
    {
        selectionBox.setLabel(description);
    }

}

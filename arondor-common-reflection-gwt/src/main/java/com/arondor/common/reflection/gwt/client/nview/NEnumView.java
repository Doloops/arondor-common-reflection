package com.arondor.common.reflection.gwt.client.nview;

import java.util.List;

import com.arondor.common.reflection.gwt.client.CssBundle;
import com.arondor.common.reflection.gwt.client.event.MyValueChangeEvent;
import com.arondor.common.reflection.gwt.client.presenter.fields.EnumTreeNodePresenter.EnumDisplay;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.FlowPanel;

import gwt.material.design.addins.client.combobox.MaterialComboBox;

public class NEnumView extends NNodeView implements EnumDisplay
{
    private final MaterialComboBox<String> selectionBox = new MaterialComboBox<String>();

    private final FlowPanel inputGroupPanel = new FlowPanel();

    public NEnumView()
    {
        selectionBox.setClass("outlined");
        selectionBox.setWidth("100%");
        inputGroupPanel.getElement().addClassName("input-group");
        selectionBox.getElement().addClassName(CssBundle.INSTANCE.css().comboBox());

        getResetFieldBtn().addClickHandler(new ClickHandler()
        {
            @Override
            public void onClick(ClickEvent event)
            {
                selectionBox.unselect();
                selectionBox.getLabel().getElement().removeClassName("select2label");
                fireEvent(new MyValueChangeEvent<String>(null));
                setActive(false);
            }
        });

        attachElements();

        Scheduler.get().scheduleDeferred(new ScheduledCommand()
        {
            @Override
            public void execute()
            {
                selectionBox.getLabel().getElement().removeClassName("select2label");
            }
        });
    }

    private void attachElements()
    {
        inputGroupPanel.add(selectionBox);
        inputGroupPanel.add(getResetFieldBtn());

        add(inputGroupPanel);
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
                    setActive(true);
                    selectionBox.getLabel().getElement().addClassName("select2label");
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

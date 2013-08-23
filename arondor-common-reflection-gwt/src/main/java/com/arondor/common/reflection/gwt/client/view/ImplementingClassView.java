package com.arondor.common.reflection.gwt.client.view;

import java.util.Collection;

import com.arondor.common.reflection.gwt.client.presenter.ImplementingClassPresenter.Display;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;

public class ImplementingClassView extends Composite implements Display
{
    private Label baseClassNameLabel = new Label("{Unknown}");

    private ListBox implementingListInput = new ListBox();

    public ImplementingClassView()
    {
        initWidget(implementingListInput);
    }

    public void setImplementingClasses(Collection<String> implementingClasses)
    {
        implementingListInput.clear();
        for (String implementingClass : implementingClasses)
        {
            implementingListInput.addItem(implementingClass);
        }
    }

    public HandlerRegistration addValueChangeHandler(final ValueChangeHandler<String> valueChangeHandler)
    {
        return implementingListInput.addChangeHandler(new ChangeHandler()
        {
            public void onChange(ChangeEvent event)
            {
                valueChangeHandler.onValueChange(new StringValueChangeEvent(implementingListInput
                        .getValue(implementingListInput.getSelectedIndex())));
            }
        });
    }

    public void setBaseClassName(String baseClassName)
    {
        baseClassNameLabel.setText(baseClassName);
        doSelect(baseClassName);
    }

    private void doSelect(String className)
    {
        for (int idx = 0; idx < implementingListInput.getItemCount(); idx++)
        {
            if (implementingListInput.getItemText(idx).equals(className))
            {
                implementingListInput.setSelectedIndex(idx);
                return;
            }
        }
    }

    private static class MyChangeEvent extends ChangeEvent
    {

    }

    public void selectImplementingClass(String implementingClassName)
    {
        doSelect(implementingClassName);
        implementingListInput.fireEvent(new MyChangeEvent());
    }
}

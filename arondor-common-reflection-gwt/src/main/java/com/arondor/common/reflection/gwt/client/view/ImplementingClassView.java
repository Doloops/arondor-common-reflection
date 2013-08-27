package com.arondor.common.reflection.gwt.client.view;

import java.util.Collection;
import java.util.logging.Logger;

import com.arondor.common.reflection.gwt.client.presenter.ImplementingClassPresenter.Display;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ListBox;

public class ImplementingClassView extends Composite implements Display
{
    private static final Logger LOG = Logger.getLogger(ImplementingClassView.class.getName());

    private ListBox implementingListInput = new ListBox();

    public ImplementingClassView()
    {
        initWidget(implementingListInput);
    }

    public void setImplementingClasses(Collection<String> implementingClasses)
    {
        LOG.finest("Selected classes : " + implementingClasses);
        implementingListInput.clear();
        for (String implementingClass : implementingClasses)
        {
            implementingListInput.addItem(implementingClass);
        }
    }

    private void doSelect(String className)
    {
        LOG.finest("Selecting class : " + className + " from a choice of " + implementingListInput.getItemCount()
                + " items");
        for (int idx = 0; idx < implementingListInput.getItemCount(); idx++)
        {
            if (implementingListInput.getItemText(idx).equals(className))
            {
                implementingListInput.setSelectedIndex(idx);
                return;
            }
        }
        LOG.warning("Could not select class : " + className);
    }

    public HandlerRegistration addValueChangeHandler(final ValueChangeHandler<String> valueChangeHandler)
    {
        return implementingListInput.addChangeHandler(new ChangeHandler()
        {
            public void onChange(ChangeEvent event)
            {
                if (implementingListInput.getSelectedIndex() != -1)
                {
                    valueChangeHandler.onValueChange(new MyValueChangeEvent<String>(implementingListInput
                            .getValue(implementingListInput.getSelectedIndex())));
                }
            }
        });
    }

    public void setBaseClassName(String baseClassName)
    {
        doSelect(baseClassName);
    }

    public void selectImplementingClass(String implementingClassName)
    {
        doSelect(implementingClassName);
    }
}

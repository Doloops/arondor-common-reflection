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
package com.arondor.common.reflection.gwt.client.view;

import java.util.Collection;
import java.util.logging.Logger;

import com.arondor.common.reflection.gwt.client.CssBundle;
import com.arondor.common.reflection.gwt.client.event.MyValueChangeEvent;
import com.arondor.common.reflection.gwt.client.presenter.ImplementingClass;
import com.arondor.common.reflection.gwt.client.presenter.ImplementingClassPresenter.ImplementingClassDisplay;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Image;

import gwt.material.design.addins.client.combobox.MaterialComboBox;
import gwt.material.design.addins.client.combobox.events.SelectItemEvent;
import gwt.material.design.addins.client.combobox.events.SelectItemEvent.SelectComboHandler;
import gwt.material.design.client.ui.html.Option;

public class ImplementingClassView extends Composite implements ImplementingClassDisplay
{
    private static final Logger LOG = Logger.getLogger(ImplementingClassView.class.getName());

    private static final String REFERENCE_PREFIX = "Reference : ";

    private MaterialComboBox<ImplementingClass> implementingListInput = new MaterialComboBox<ImplementingClass>();

    private ImplementingClass selectedClass = ImplementingClass.NULL_CLASS;

    private String longDescription;

    private FocusPanel sharedObjectCreatePanel = new FocusPanel();

    private FocusPanel sharedObjectForwardPanel = new FocusPanel();

    private Image sharedObjectImg = new Image("/icons/convert-shared-object.svg");

    private Image sharedObjectViewImg = new Image("/icons/view-shared-object.svg");

    public ImplementingClassView()
    {
        initWidget(implementingListInput);
        implementingListInput.setClass("outlined");
        implementingListInput.getElement().addClassName(CssBundle.INSTANCE.css().comboBox());
        // resetImplementingList();
        sharedObjectCreatePanel.add(sharedObjectImg);
        sharedObjectCreatePanel.getElement().getStyle().setDisplay(Display.NONE);

        sharedObjectForwardPanel.add(sharedObjectViewImg);
        sharedObjectForwardPanel.getElement().getStyle().setDisplay(Display.NONE);

        sharedObjectImg.getElement().addClassName(CssBundle.INSTANCE.css().sharedObjectImg());
        sharedObjectViewImg.getElement().addClassName(CssBundle.INSTANCE.css().sharedObjectImg());

        implementingListInput.setTitle(longDescription);
        implementingListInput.getLabel().addClickHandler(new ClickHandler()
        {
            @Override
            public void onClick(ClickEvent event)
            {
                implementingListInput.open();
            }
        });
        // implementingListInput.addMouseOverHandler(new MouseOverHandler()
        // {
        // @Override
        // public void onMouseOver(MouseOverEvent event)
        // {
        // if (longDescription != null)
        // implementingListInput.setHelperText(longDescription);
        // }
        // });
        // implementingListInput.addMouseOutHandler(new MouseOutHandler()
        // {
        // @Override
        // public void onMouseOut(MouseOutEvent event)
        // {
        // implementingListInput.clearHelperText();
        // }
        // });
        fixLabelStyle();
    }

    @Override
    public HandlerRegistration onOpenImplementingClasses(Command command)
    {
        return implementingListInput.addOpenHandler(new OpenHandler<ImplementingClass>()
        {
            @Override
            public void onOpen(OpenEvent<ImplementingClass> event)
            {
                LOG.info("onOpenImplementingClasses()");
                command.execute();
            }
        });
    }

    @Override
    public void resetImplementingList()
    {
        selectedClass = ImplementingClass.NULL_CLASS;
        selectImplementingClass(selectedClass);
        fireEvent(new MyValueChangeEvent<ImplementingClass>(selectedClass));

        sharedObjectCreatePanel.getElement().getStyle().setDisplay(Display.NONE);

        // to prevent the onLoad() MaterialCombobox call
        fixLabelStyle();
    }

    private void fixLabelStyle()
    {
        Scheduler.get().scheduleDeferred(new ScheduledCommand()
        {
            @Override
            public void execute()
            {
                if (implementingListInput.getSelectedValues() == null
                        || implementingListInput.getSelectedValues().isEmpty()
                        || implementingListInput.getValues().size() < 1)
                {
                    implementingListInput.getLabel().getElement().removeClassName("select2label");
                }
            }
        });
    }

    private String prettyPrint(ImplementingClass implementingClass)
    {
        if (implementingClass == null)
            implementingClass = ImplementingClass.NULL_CLASS;

        if (implementingClass.isReference())
            return REFERENCE_PREFIX + implementingClass.getDisplayName();

        return implementingClass.getDisplayName();
    }

    private void updateSharedObjectPanel(ImplementingClass implementingClass)
    {
        boolean ref = implementingClass.isReference();
        sharedObjectCreatePanel.getElement().getStyle().setDisplay(ref ? Display.NONE : Display.BLOCK);
        sharedObjectForwardPanel.getElement().getStyle().setDisplay(ref ? Display.BLOCK : Display.NONE);
    }

    @Override
    public void setImplementingClasses(Collection<ImplementingClass> implementingClasses)
    {
        LOG.finest("Selected classes : " + implementingClasses);

        for (ImplementingClass implementingClass : implementingClasses)
        {
            if (implementingListInput.getValues().contains(implementingClass))
                continue;
            Option option = implementingListInput.addItem(prettyPrint(implementingClass), implementingClass);
            if (implementingClass.getClassName() != null)
                option.setTitle(implementingClass.getClassName());
        }

        if (selectedClass == ImplementingClass.NULL_CLASS)
            implementingListInput.unselect();
    }

    private void doSelect(ImplementingClass implementingClass)
    {
        LOG.finest("doSelect class=" + implementingClass);
        selectedClass = implementingClass;
        if (implementingClass == ImplementingClass.NULL_CLASS)
        {
            implementingListInput.unselect();
            return;
        }
        LOG.finest("Selecting class : " + implementingClass + " from a choice of "
                + implementingListInput.getValues().size() + " items");
        int index = implementingListInput.getValueIndex(implementingClass);

        if (index == -1)
        {
            LOG.info("Adding item for class " + implementingClass + " => " + prettyPrint(implementingClass));
            implementingListInput.addItem(prettyPrint(implementingClass), implementingClass);
            implementingListInput.setSelectedIndex(implementingListInput.getValues().size() - 1);
        }
        else
        {
            LOG.info("Selected item #" + index + " for clazz " + implementingClass);
            implementingListInput.setSelectedIndex(index);
        }
        updateSharedObjectPanel(implementingClass);
    }

    @Override
    public HandlerRegistration addValueChangeHandler(final ValueChangeHandler<ImplementingClass> valueChangeHandler)
    {
        return implementingListInput.addSelectionHandler(new SelectComboHandler<ImplementingClass>()
        {
            @Override
            public void onSelectItem(SelectItemEvent<ImplementingClass> event)
            {
                LOG.finest("onSelectItem() selected size=" + event.getSelectedValues().size());
                {
                    if (event.getSelectedValues().isEmpty())
                        selectedClass = ImplementingClass.NULL_CLASS;
                    else
                        selectedClass = event.getSelectedValues().get(0);
                    LOG.finest("Selected " + selectedClass);
                    valueChangeHandler.onValueChange(new MyValueChangeEvent<ImplementingClass>(selectedClass));
                    updateSharedObjectPanel(selectedClass);
                }
            }
        });
    }

    @Override
    public void selectImplementingClass(ImplementingClass implementingClassName)
    {
        doSelect(implementingClassName);
        fixLabelStyle();
    }

    @Override
    public void setNodeDescription(String label)
    {
        implementingListInput.setLabel(label);
    }

    @Override
    public void setNodeLongDescription(String longDescription)
    {
        this.longDescription = longDescription;
    }

    @Override
    public FocusPanel getSharedObjectCreatePanel()
    {
        return sharedObjectCreatePanel;
    }

    @Override
    public FocusPanel getSharedObjectForwardPanel()
    {
        return sharedObjectForwardPanel;
    }
}

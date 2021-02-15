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

import java.util.ArrayList;
import java.util.List;

import com.arondor.common.reflection.bean.java.AccessibleFieldBean;
import com.arondor.common.reflection.gwt.client.AccessibleClassPresenterFactory;
import com.arondor.common.reflection.gwt.client.event.TreeNodeClearEvent;
import com.arondor.common.reflection.gwt.client.presenter.ClassTreeNodePresenter;
import com.arondor.common.reflection.gwt.client.presenter.ClassTreeNodePresenter.ClassDisplay;
import com.arondor.common.reflection.gwt.client.presenter.ObjectReferencesProvider;
import com.arondor.common.reflection.gwt.client.presenter.TreeNodePresenter;
import com.arondor.common.reflection.gwt.client.service.GWTReflectionServiceAsync;
import com.arondor.common.reflection.model.config.ElementConfiguration;
import com.arondor.common.reflection.model.config.ListConfiguration;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;

public class ListTreeNodePresenter implements TreeNodePresenter
{
    public interface ListRootDisplay extends Display
    {
        HasClickHandlers addElementClickHandler();

        void removeChild(Display childDisplay);

        ClassDisplay createChildDisplay();

    }

    private final GWTReflectionServiceAsync rpcService;

    private final ObjectReferencesProvider objectReferencesProvider;

    private final ListRootDisplay listDisplay;

    private final String genericType;

    public ListTreeNodePresenter(GWTReflectionServiceAsync rpcService,
            ObjectReferencesProvider objectReferencesProvider, String genericType, ListRootDisplay listDisplay)
    {
        this.rpcService = rpcService;
        this.objectReferencesProvider = objectReferencesProvider;
        this.listDisplay = listDisplay;
        this.genericType = genericType;

        bind();
    }

    private void bind()
    {
        listDisplay.addElementClickHandler().addClickHandler(new ClickHandler()
        {
            @Override
            public void onClick(ClickEvent event)
            {
                addChild();
            }
        });

    }

    private final List<TreeNodePresenter> childPresenters = new ArrayList<TreeNodePresenter>();

    protected TreeNodePresenter addChild()
    {
        listDisplay.setActive(true);
        AccessibleFieldBean entryBean = new AccessibleFieldBean();
        entryBean.setClassName(genericType);
        entryBean.setName("Entry");
        entryBean.setDescription("Entry");

        ClassDisplay display = listDisplay.createChildDisplay();
        // final TreeNodePresenter childPresenter =
        // TreeNodePresenterFactory.getInstance()
        // .createChildNodePresenter(rpcService, objectReferencesProvider,
        // listDisplay, entryBean);
        TreeNodePresenter childPresenter = new ClassTreeNodePresenter(rpcService, objectReferencesProvider, genericType,
                false, display.createClassChild(false));

        childPresenter.getDisplay().addTreeNodeClearHandler(new TreeNodeClearEvent.Handler()
        {
            @Override
            public void onTreeNodeClearEvent(TreeNodeClearEvent treeNodeClearEvent)
            {
                removeChild(childPresenter);
            }
        });
        childPresenters.add(childPresenter);
        return childPresenter;
    }

    protected void removeChild(TreeNodePresenter childPresenter)
    {
        listDisplay.removeChild(childPresenter.getDisplay());
        childPresenters.remove(childPresenter);
    }

    @Override
    public ElementConfiguration getElementConfiguration()
    {
        if (!listDisplay.isActive())
        {
            return null;
        }
        ListConfiguration listConfiguration = AccessibleClassPresenterFactory.getObjectConfigurationFactory()
                .createListConfiguration();
        listConfiguration.setListConfiguration(new ArrayList<ElementConfiguration>());
        for (TreeNodePresenter presenter : childPresenters)
        {
            ElementConfiguration childConfiguration = presenter.getElementConfiguration();
            listConfiguration.getListConfiguration().add(childConfiguration);
        }
        return listConfiguration;
    }

    @Override
    public void setElementConfiguration(ElementConfiguration elementConfiguration)
    {
        if (elementConfiguration instanceof ListConfiguration)
        {
            ListConfiguration listConfiguration = (ListConfiguration) elementConfiguration;
            for (ElementConfiguration childConfiguration : listConfiguration.getListConfiguration())
            {
                TreeNodePresenter childPresenter = addChild();
                childPresenter.setElementConfiguration(childConfiguration);
            }
        }
    }

    @Override
    public Display getDisplay()
    {
        return listDisplay;
    }

}

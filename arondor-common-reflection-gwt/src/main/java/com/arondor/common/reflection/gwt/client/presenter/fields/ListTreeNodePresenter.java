package com.arondor.common.reflection.gwt.client.presenter.fields;

import java.util.ArrayList;
import java.util.List;

import com.arondor.common.reflection.gwt.client.event.TreeNodeClearEvent;
import com.arondor.common.reflection.gwt.client.presenter.TreeNodePresenter;
import com.arondor.common.reflection.gwt.client.presenter.TreeNodePresenterFactory;
import com.arondor.common.reflection.gwt.client.service.GWTReflectionServiceAsync;
import com.arondor.common.reflection.model.config.ElementConfiguration;
import com.arondor.common.reflection.model.config.ListConfiguration;
import com.arondor.common.reflection.model.config.ObjectConfigurationFactory;
import com.arondor.common.reflection.model.config.ObjectConfigurationMap;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;

public class ListTreeNodePresenter implements TreeNodePresenter
{
    public interface ListRootDisplay extends ChildCreatorDisplay
    {
        HasClickHandlers addElementClickHandler();

        void removeChild(Display childDisplay);
    }

    private final GWTReflectionServiceAsync rpcService;

    private final ObjectConfigurationMap objectConfigurationMap;

    private final ListRootDisplay listDisplay;

    private final String fieldName;

    private final String genericType;

    public ListTreeNodePresenter(GWTReflectionServiceAsync rpcService, ObjectConfigurationMap objectConfigurationMap,
            String fieldName, String genericType, ListRootDisplay listDisplay)
    {
        this.rpcService = rpcService;
        this.objectConfigurationMap = objectConfigurationMap;
        this.fieldName = fieldName;
        this.listDisplay = listDisplay;
        this.genericType = genericType;

        bind();
    }

    private void bind()
    {
        listDisplay.addElementClickHandler().addClickHandler(new ClickHandler()
        {
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
        final TreeNodePresenter childPresenter = TreeNodePresenterFactory.getInstance().createChildNodePresenter(
                rpcService, objectConfigurationMap, listDisplay, "Entry", genericType, "Entry", null);

        childPresenter.getDisplay().addTreeNodeClearHandler(new TreeNodeClearEvent.Handler()
        {
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

    public ElementConfiguration getElementConfiguration(ObjectConfigurationFactory objectConfigurationFactory)
    {
        if (!listDisplay.isActive())
        {
            return null;
        }
        ListConfiguration listConfiguration = objectConfigurationFactory.createListConfiguration();
        listConfiguration.setListConfiguration(new ArrayList<ElementConfiguration>());
        for (TreeNodePresenter presenter : childPresenters)
        {
            ElementConfiguration childConfiguration = presenter.getElementConfiguration(objectConfigurationFactory);
            listConfiguration.getListConfiguration().add(childConfiguration);
        }
        return listConfiguration;
    }

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

    public String getFieldName()
    {
        return fieldName;
    }

    public Display getDisplay()
    {
        return listDisplay;
    }

}

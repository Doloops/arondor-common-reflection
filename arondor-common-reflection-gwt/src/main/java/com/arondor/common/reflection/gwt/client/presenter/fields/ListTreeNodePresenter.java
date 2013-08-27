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

    private final ListRootDisplay listDisplay;

    private final String fieldName;

    private final List<String> genericTypes;

    private final GWTReflectionServiceAsync rpcService;

    public ListTreeNodePresenter(GWTReflectionServiceAsync rpcService, String fieldName, List<String> genericTypes,
            ListRootDisplay listDisplay)
    {
        this.rpcService = rpcService;
        this.fieldName = fieldName;
        this.listDisplay = listDisplay;
        this.genericTypes = genericTypes;

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
                rpcService, listDisplay, "Entry", genericTypes.get(0), "Entry", null);

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

package com.arondor.common.reflection.gwt.client.presenter.fields;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.arondor.common.reflection.gwt.client.event.TreeNodeClearEvent;
import com.arondor.common.reflection.gwt.client.presenter.TreeNodePresenter;
import com.arondor.common.reflection.gwt.client.presenter.TreeNodePresenterFactory;
import com.arondor.common.reflection.gwt.client.service.GWTReflectionServiceAsync;
import com.arondor.common.reflection.model.config.ElementConfiguration;
import com.arondor.common.reflection.model.config.MapConfiguration;
import com.arondor.common.reflection.model.config.ObjectConfigurationFactory;
import com.arondor.common.reflection.model.config.ObjectConfigurationMap;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;

public class MapTreeNodePresenter implements TreeNodePresenter
{
    public interface MapRootDisplay extends Display
    {
        HasClickHandlers addElementClickHandler();

        MapNodeDisplay createChildNode();
    }

    public interface MapNodeDisplay extends ChildCreatorDisplay
    {

    }

    private final MapRootDisplay mapRootDisplay;

    private final String fieldName;

    private final List<String> genericTypes;

    private final GWTReflectionServiceAsync rpcService;

    private final ObjectConfigurationMap objectConfigurationMap;

    public String getFieldName()
    {
        return fieldName;
    }

    public MapTreeNodePresenter(GWTReflectionServiceAsync rpcService, ObjectConfigurationMap objectConfigurationMap,
            String fieldName, List<String> genericTypes, MapRootDisplay mapDisplay)
    {
        this.rpcService = rpcService;
        this.objectConfigurationMap = objectConfigurationMap;
        this.fieldName = fieldName;
        this.mapRootDisplay = mapDisplay;
        this.genericTypes = genericTypes;

        bind();
    }

    private void bind()
    {
        mapRootDisplay.addElementClickHandler().addClickHandler(new ClickHandler()
        {
            public void onClick(ClickEvent event)
            {
                addChild();
            }
        });

        mapRootDisplay.addTreeNodeClearHandler(new TreeNodeClearEvent.Handler()
        {
            public void onTreeNodeClearEvent(TreeNodeClearEvent treeNodeClearEvent)
            {
                keyValuePresenters.clear();
            }
        });
    }

    private final List<KeyValuePresenterPair> keyValuePresenters = new ArrayList<KeyValuePresenterPair>();

    protected List<KeyValuePresenterPair> getKeyValuePresenters()
    {
        return keyValuePresenters;
    }

    protected KeyValuePresenterPair addChild()
    {
        mapRootDisplay.setActive(true);
        MapNodeDisplay childNode = mapRootDisplay.createChildNode();
        childNode.setNodeName("Entry");

        TreeNodePresenter keyPresenter = TreeNodePresenterFactory.getInstance().createChildNodePresenter(rpcService,
                objectConfigurationMap, childNode, "Key", genericTypes.get(0), "Key", null);

        TreeNodePresenter valuePresenter = TreeNodePresenterFactory.getInstance().createChildNodePresenter(rpcService,
                objectConfigurationMap, childNode, "Value", genericTypes.get(1), "Value", null);

        final KeyValuePresenterPair keyValuePresenterPair = new KeyValuePresenterPair(keyPresenter, valuePresenter);
        keyValuePresenters.add(keyValuePresenterPair);

        childNode.addTreeNodeClearHandler(new TreeNodeClearEvent.Handler()
        {
            public void onTreeNodeClearEvent(TreeNodeClearEvent treeNodeClearEvent)
            {
                keyValuePresenters.remove(keyValuePresenterPair);
            }
        });
        return keyValuePresenterPair;
    }

    public ElementConfiguration getElementConfiguration(ObjectConfigurationFactory objectConfigurationFactory)
    {
        if (!mapRootDisplay.isActive())
        {
            return null;
        }
        MapConfiguration mapConfiguration = objectConfigurationFactory.createMapConfiguration();
        mapConfiguration.setMapConfiguration(new HashMap<ElementConfiguration, ElementConfiguration>());

        for (KeyValuePresenterPair keyValuePresenterPair : keyValuePresenters)
        {
            ElementConfiguration key = keyValuePresenterPair.getKeyPresenter().getElementConfiguration(
                    objectConfigurationFactory);
            ElementConfiguration value = keyValuePresenterPair.getValuePresenter().getElementConfiguration(
                    objectConfigurationFactory);
            mapConfiguration.getMapConfiguration().put(key, value);
        }
        return mapConfiguration;
    }

    protected void addChild(ElementConfiguration keyConfiguration, ElementConfiguration valueConfiguration)
    {
        KeyValuePresenterPair keyValuePresenterPair = addChild();
        keyValuePresenterPair.getKeyPresenter().setElementConfiguration(keyConfiguration);
        keyValuePresenterPair.getValuePresenter().setElementConfiguration(valueConfiguration);
    }

    public void setElementConfiguration(ElementConfiguration elementConfiguration)
    {
        if (elementConfiguration instanceof MapConfiguration)
        {
            MapConfiguration mapConfiguration = (MapConfiguration) elementConfiguration;
            for (Map.Entry<ElementConfiguration, ElementConfiguration> entry : mapConfiguration.getMapConfiguration()
                    .entrySet())
            {
                addChild(entry.getKey(), entry.getValue());
            }
        }
    }

    public Display getDisplay()
    {
        return mapRootDisplay;
    }

}

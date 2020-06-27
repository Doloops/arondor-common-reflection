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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.arondor.common.reflection.gwt.client.event.TreeNodeClearEvent;
import com.arondor.common.reflection.gwt.client.presenter.ClassTreeNodePresenter;
import com.arondor.common.reflection.gwt.client.presenter.ClassTreeNodePresenter.ClassDisplay;
import com.arondor.common.reflection.gwt.client.presenter.TreeNodePresenter;
import com.arondor.common.reflection.gwt.client.presenter.fields.PrimitiveTreeNodePresenter.PrimitiveDisplay;
import com.arondor.common.reflection.gwt.client.service.GWTReflectionServiceAsync;
import com.arondor.common.reflection.model.config.ElementConfiguration;
import com.arondor.common.reflection.model.config.MapConfiguration;
import com.arondor.common.reflection.model.config.ObjectConfigurationFactory;
import com.arondor.common.reflection.model.config.ObjectConfigurationMap;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Widget;

public class MapTreeNodePresenter implements TreeNodePresenter
{
    private static final Logger LOG = Logger.getLogger(MapTreeNodePresenter.class.getName());

    public interface MapRootDisplay extends Display
    {
        HasClickHandlers addElementClickHandler();

        MapPairDisplay createPair(String keyClass, String valueClass);
    }

    public interface MapPairDisplay
    {
        HasClickHandlers removePairClickHandler();

        PrimitiveDisplay getKeyDisplay();

        Display getValueDisplay();

        Widget asWidget();
    }

    private final MapRootDisplay mapRootDisplay;

    /**
     * First generic type for keys, Second generic type for values
     */
    private final List<String> genericTypes;

    protected final GWTReflectionServiceAsync rpcService;

    private ObjectConfigurationMap objectConfigurationMap;

    public ObjectConfigurationMap getObjectConfigurationMap()
    {
        return objectConfigurationMap;
    }

    public void setObjectConfigurationMap(ObjectConfigurationMap objectConfigurationMap)
    {
        this.objectConfigurationMap = objectConfigurationMap;
    }

    public MapTreeNodePresenter(GWTReflectionServiceAsync rpcService, ObjectConfigurationMap objectConfigurationMap,
            List<String> genericTypes, MapRootDisplay mapDisplay)
    {
        this.rpcService = rpcService;
        this.objectConfigurationMap = objectConfigurationMap;
        this.mapRootDisplay = mapDisplay;
        this.genericTypes = genericTypes;

        bind();
    }

    private void bind()
    {
        mapRootDisplay.addElementClickHandler().addClickHandler(new ClickHandler()
        {
            @Override
            public void onClick(ClickEvent event)
            {
                addChild();
            }
        });

        mapRootDisplay.addTreeNodeClearHandler(new TreeNodeClearEvent.Handler()
        {
            @Override
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
        String keyClass = genericTypes.get(0);
        String valueClass = genericTypes.get(1);
        MapPairDisplay pairView = mapRootDisplay.createPair(keyClass, valueClass);

        TreeNodePresenter keyPresenter = new PrimitiveTreeNodePresenter(pairView.getKeyDisplay());
        TreeNodePresenter valuePresenter = createValuePresenter(pairView.getValueDisplay(), valueClass);

        KeyValuePresenterPair keyValuePresenterPair = new KeyValuePresenterPair(keyPresenter, valuePresenter, pairView);
        keyValuePresenters.add(keyValuePresenterPair);

        pairView.removePairClickHandler().addClickHandler(new ClickHandler()
        {
            @Override
            public void onClick(ClickEvent event)
            {
                keyValuePresenters.remove(keyValuePresenterPair);
            }
        });
        return keyValuePresenterPair;
    }

    private TreeNodePresenter createValuePresenter(Display valueDisplay, String valueClass)
    {
        if (valueDisplay instanceof PrimitiveDisplay)
        {
            return new PrimitiveTreeNodePresenter((PrimitiveDisplay) valueDisplay);
        }
        if (valueDisplay instanceof ClassDisplay)
        {
            return new ClassTreeNodePresenter(rpcService, objectConfigurationMap, valueClass,
                    (ClassDisplay) valueDisplay);
        }
        throw new RuntimeException("Could not instantiate presenter for view : " + valueDisplay.getClass().getName());
    }

    @Override
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
            ElementConfiguration key = keyValuePresenterPair.getKeyPresenter()
                    .getElementConfiguration(objectConfigurationFactory);
            ElementConfiguration value = keyValuePresenterPair.getValuePresenter()
                    .getElementConfiguration(objectConfigurationFactory);
            if (key != null && value != null)
            {
                mapConfiguration.getMapConfiguration().put(key, value);
            }
            else
            {
                LOG.severe("Invalid key/value pair ! key=" + key + ", value=" + value);
            }
        }
        return mapConfiguration;
    }

    private void addChild(ElementConfiguration keyConfiguration, ElementConfiguration valueConfiguration)
    {
        KeyValuePresenterPair keyValuePresenterPair = addChild();
        keyValuePresenterPair.getKeyPresenter().setElementConfiguration(keyConfiguration);
        keyValuePresenterPair.getValuePresenter().setElementConfiguration(valueConfiguration);
    }

    @Override
    public void setElementConfiguration(ElementConfiguration elementConfiguration)
    {
        if (elementConfiguration instanceof MapConfiguration)
        {
            MapConfiguration mapConfiguration = (MapConfiguration) elementConfiguration;
            for (Map.Entry<ElementConfiguration, ElementConfiguration> entry : mapConfiguration.getMapConfiguration()
                    .entrySet())
            {
                addChild(entry.getKey(), entry.getValue());
                LOG.finest("setting elementConfiguration as Map<" + entry.getKey() + ", " + entry.getValue() + ">");
            }
        }
        else
        {
            throw new IllegalArgumentException("Invalid class : " + elementConfiguration.getClass().getName());
        }
    }

    @Override
    public MapRootDisplay getDisplay()
    {
        return mapRootDisplay;
    }

}

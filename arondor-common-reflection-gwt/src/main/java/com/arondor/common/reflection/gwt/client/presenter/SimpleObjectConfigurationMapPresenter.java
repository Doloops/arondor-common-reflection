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
package com.arondor.common.reflection.gwt.client.presenter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.arondor.common.reflection.bean.config.ObjectConfigurationMapBean;
import com.arondor.common.reflection.gwt.client.api.ObjectConfigurationMapPresenter;
import com.arondor.common.reflection.gwt.client.presenter.ClassTreeNodePresenter.ClassDisplay;
import com.arondor.common.reflection.gwt.client.presenter.fields.KeyValuePresenterPair;
import com.arondor.common.reflection.gwt.client.presenter.fields.MapTreeNodePresenter;
import com.arondor.common.reflection.gwt.client.presenter.fields.PrimitiveTreeNodePresenter;
import com.arondor.common.reflection.gwt.client.service.GWTReflectionServiceAsync;
import com.arondor.common.reflection.model.config.ElementConfiguration;
import com.arondor.common.reflection.model.config.ObjectConfiguration;
import com.arondor.common.reflection.model.config.ObjectConfigurationFactory;
import com.arondor.common.reflection.model.config.ObjectConfigurationMap;
import com.arondor.common.reflection.model.config.PrimitiveConfiguration;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.IsWidget;

public class SimpleObjectConfigurationMapPresenter extends MapTreeNodePresenter
        implements ObjectConfigurationMapPresenter
{
    private static final Logger LOG = Logger.getLogger(SimpleObjectConfigurationMapPresenter.class.getName());

    private static final List<String> GENERIC_TYPES = new ArrayList<String>();

    static
    {
        GENERIC_TYPES.add("java.lang.String");
        GENERIC_TYPES.add("java.lang.Object");
    }

    public interface ObjectConfigurationMapDisplay extends MapRootDisplay
    {
        IsWidget getDisplayWidget();
    }

    private final List<String> availableScopes;

    public SimpleObjectConfigurationMapPresenter(GWTReflectionServiceAsync rpcService, String fieldName,
            ObjectConfigurationMapDisplay mapDisplay, List<String> availableScopes)
    {
        super(rpcService, null, GENERIC_TYPES, mapDisplay);
        mapDisplay.setNodeDescription(fieldName);
        super.setObjectConfigurationMap(new ObjectConfigurationMapBean());
        this.availableScopes = availableScopes;
    }

    /**
     * For the sake of robustness, we have our very own bean model for the fake
     * PrimitiveConfiguration used for the Key mapping
     * 
     */
    private static class MyPrimitiveConfiguration implements PrimitiveConfiguration
    {
        /**
         * 
         */
        private static final long serialVersionUID = 9147786493404617175L;

        private String value;

        public MyPrimitiveConfiguration(String value)
        {
            this.value = value;
        }

        @Override
        public ElementConfigurationType getFieldConfigurationType()
        {
            return ElementConfigurationType.Primitive;
        }

        @Override
        public String getValue()
        {
            return value;
        }

        @Override
        public void setValue(String value)
        {
            this.value = value;
        }

    }

    @Override
    public void addObjectConfigurationMap(String scope, ObjectConfigurationMap objectConfigurationMap)
    {
        if (objectConfigurationMap != null)
        {
            super.getObjectConfigurationMap().putAll(objectConfigurationMap);
            for (Map.Entry<String, ObjectConfiguration> entry : objectConfigurationMap.entrySet())
            {
                LOG.info("Adding scope=" + scope + ", object=" + entry.getKey() + ", class="
                        + entry.getValue().getClassName());
                addChild(scope, new MyPrimitiveConfiguration(entry.getKey()), entry.getValue());
            }
        }
    }

    @Override
    public void clearObjectConfigurations()
    {
        super.getObjectConfigurationMap().clear();
        super.getKeyValuePresenters().clear();
        super.getDisplay().clear();
    }

    private KeyValuePresenterPair addChild(String scope, ElementConfiguration keyConfiguration,
            ElementConfiguration valueConfiguration)
    {
        KeyValuePresenterPair childPair = addChild();
        childPair.getKeyPresenter().setElementConfiguration(keyConfiguration);
        childPair.getValuePresenter().setElementConfiguration(valueConfiguration);

        if (childPair.getDisplay() instanceof MapPairDisplayWithScope)
        {
            MapPairDisplayWithScope scopePairView = (MapPairDisplayWithScope) childPair.getDisplay();
            scopePairView.setScope(scope);
        }
        return childPair;
    }

    @Override
    protected KeyValuePresenterPair addChild()
    {
        getDisplay().setActive(true);
        String keyClass = GENERIC_TYPES.get(0);
        String valueClass = GENERIC_TYPES.get(1);
        MapPairDisplay pairView = getDisplay().createPair(keyClass, valueClass);
        if (pairView instanceof MapPairDisplayWithScope)
        {
            MapPairDisplayWithScope scopePairView = (MapPairDisplayWithScope) pairView;
            scopePairView.setAvailableScopes(availableScopes);
        }

        TreeNodePresenter keyPresenter = new PrimitiveTreeNodePresenter(pairView.getKeyDisplay());
        TreeNodePresenter valuePresenter = new ClassTreeNodePresenter(rpcService, getObjectConfigurationMap(),
                valueClass, (ClassDisplay) pairView.getValueDisplay());

        KeyValuePresenterPair keyValuePresenterPair = new KeyValuePresenterPair(keyPresenter, valuePresenter, pairView);
        getKeyValuePresenters().add(keyValuePresenterPair);

        pairView.removePairClickHandler().addClickHandler(new ClickHandler()
        {
            @Override
            public void onClick(ClickEvent event)
            {
                getKeyValuePresenters().remove(keyValuePresenterPair);
            }
        });
        return keyValuePresenterPair;
    }

    @Override
    public ObjectConfigurationMap getObjectConfigurationMap(String forScope,
            ObjectConfigurationFactory objectConfigurationFactory)
    {
        ObjectConfigurationMap objectConfigurationMap = objectConfigurationFactory.createObjectConfigurationMap();
        for (KeyValuePresenterPair presenter : getKeyValuePresenters())
        {
            if (presenter.getDisplay() instanceof MapPairDisplayWithScope)
            {
                String scope = ((MapPairDisplayWithScope) presenter.getDisplay()).getScope();
                if (forScope != null && !forScope.equals(scope))
                {
                    LOG.finest("Skipping presenter bound to scope " + scope + ", only selecting " + forScope);
                    continue;
                }
            }

            ElementConfiguration keyElementConfiguration = presenter.getKeyPresenter()
                    .getElementConfiguration(objectConfigurationFactory);
            if (keyElementConfiguration == null)
            {
                LOG.warning("Skipping element because configuration is null !");
                continue;
            }
            if (!(keyElementConfiguration instanceof PrimitiveConfiguration))
            {
                LOG.warning("Skipping element because key is of class " + keyElementConfiguration.getClass().getName());
                continue;
            }
            PrimitiveConfiguration primitiveKey = (PrimitiveConfiguration) keyElementConfiguration;
            String keyString = primitiveKey.getValue();
            ElementConfiguration valueConfiguration = presenter.getValuePresenter()
                    .getElementConfiguration(objectConfigurationFactory);
            if (!(valueConfiguration instanceof ObjectConfiguration))
            {
                LOG.warning(
                        "Skipping element because value is of class " + keyElementConfiguration.getClass().getName());
                continue;
            }
            ObjectConfiguration objectConfiguration = (ObjectConfiguration) valueConfiguration;
            objectConfigurationMap.put(keyString, objectConfiguration);
        }
        return objectConfigurationMap;
    }

    @Override
    public IsWidget getDisplayWidget()
    {
        return ((ObjectConfigurationMapDisplay) getDisplay()).getDisplayWidget();
    }
}

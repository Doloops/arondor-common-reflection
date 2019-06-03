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
package com.arondor.common.reflection.gwt.client;

import java.util.Collection;
import java.util.logging.Logger;

import com.arondor.common.reflection.api.catalog.AccessibleClassCatalog;
import com.arondor.common.reflection.bean.config.ObjectConfigurationFactoryBean;
import com.arondor.common.reflection.bean.config.ObjectConfigurationMapBean;
import com.arondor.common.reflection.gwt.client.api.AccessibleClassPresenter;
import com.arondor.common.reflection.gwt.client.presenter.ReflectionDesignerPresenter;
import com.arondor.common.reflection.gwt.client.service.GWTReflectionServiceAsync;
import com.arondor.common.reflection.model.config.ObjectConfiguration;
import com.arondor.common.reflection.model.config.ObjectConfigurationFactory;
import com.arondor.common.reflection.model.config.ObjectConfigurationMap;
import com.arondor.common.reflection.model.java.AccessibleClass;
import com.arondor.common.reflection.xstream.GWTObjectConfigurationParser;
import com.arondor.common.reflection.xstream.GWTObjectConfigurationSerializer;
import com.arondor.common.reflection.xstream.catalog.GWTAccessibleClassCatalogParser;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;

public class ReflectionDesignerEntryPoint implements EntryPoint
{
    private static final Logger LOG = Logger.getLogger(ReflectionDesignerEntryPoint.class.getName());

    private final ObjectConfigurationFactory objectConfigurationFactory = new ObjectConfigurationFactoryBean();

    private static ReflectionDesignerEntryPoint INSTANCE;

    @Override
    public void onModuleLoad()
    {
        INSTANCE = this;

        RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.GET, "allclasses.xml");
        requestBuilder.setCallback(new RequestCallback()
        {

            @Override
            public void onResponseReceived(Request request, Response response)
            {
                String xml = response.getText();
                GWTAccessibleClassCatalogParser parser = new GWTAccessibleClassCatalogParser();
                final AccessibleClassCatalog catalog = parser.parse(xml);

                GWTReflectionServiceAsync simpleReflectionService = new GWTReflectionServiceAsync()
                {
                    @Override
                    public void getImplementingAccessibleClasses(final String name,
                            final AsyncCallback<Collection<AccessibleClass>> callback)
                    {
                        Scheduler.get().scheduleDeferred(new ScheduledCommand()
                        {
                            @Override
                            public void execute()
                            {
                                callback.onSuccess(catalog.getImplementingAccessibleClasses(name));
                            }
                        });
                    }

                    @Override
                    public void getAccessibleClass(final String className,
                            final AsyncCallback<AccessibleClass> callback)
                    {
                        Scheduler.get().scheduleDeferred(new ScheduledCommand()
                        {
                            @Override
                            public void execute()
                            {
                                callback.onSuccess(catalog.getAccessibleClass(className));
                            }
                        });
                    }
                };

                continueLoading(simpleReflectionService);
            }

            @Override
            public void onError(Request request, Throwable exception)
            {
                Window.alert("Could not get resources !");
            }
        });

        try
        {
            requestBuilder.send();
        }
        catch (RequestException e)
        {
            LOG.warning("Could not send !" + e.getMessage());
        }
    }

    private GWTObjectConfigurationSerializer serializer = new GWTObjectConfigurationSerializer();

    private GWTObjectConfigurationParser parser = new GWTObjectConfigurationParser();

    private ObjectConfigurationMap objectConfigurationMap = new ObjectConfigurationMapBean();

    private AccessibleClassPresenter rootPresenter;

    private void continueLoading(GWTReflectionServiceAsync reflectionService)
    {

        String baseClassName = Window.Location.getParameter("baseClassName");
        if (baseClassName == null || baseClassName.isEmpty())
        {
            baseClassName = "com.arondor.fast2p8.model.task.Task";
        }
        rootPresenter = AccessibleClassPresenterFactory.createAccessibleClassPresenter(reflectionService,
                objectConfigurationMap, baseClassName);

        RootPanel.get().clear();
        RootPanel.get().add(rootPresenter.getDisplayWidget());

        bindJSNI();

    }

    private native void bindJSNI() /*-{
		$wnd.getConfiguration = $entry(@com.arondor.common.reflection.gwt.client.ReflectionDesignerEntryPoint::getConfigurationStatic());
		$wnd.setConfiguration = $entry(@com.arondor.common.reflection.gwt.client.ReflectionDesignerEntryPoint::setConfigurationStatic(Ljava/lang/String;));
    }-*/;

    public static String getConfigurationStatic()
    {
        return INSTANCE.getConfiguration();
    }

    public String getConfiguration()
    {
        ObjectConfiguration objectConfiguration = rootPresenter.getObjectConfiguration(objectConfigurationFactory);

        String xml = serializer.serialize(objectConfiguration);

        LOG.info("Serialized : " + xml);

        return xml;
    }

    public static void setConfigurationStatic(String xml)
    {
        INSTANCE.setConfiguration(xml);
    }

    public void setConfiguration(String xml)
    {
        LOG.info("XML : " + xml);
        ObjectConfiguration objectConfiguration = parser.parse(xml);
        rootPresenter.setObjectConfiguration(objectConfiguration);
    }

    private void continueLoading__(GWTReflectionServiceAsync reflectionService)
    {
        String baseClassName = java.lang.Object.class.getName();
        ReflectionDesignerPresenter classPresenter = new ReflectionDesignerPresenter(reflectionService, baseClassName);

        RootPanel.get().clear();
        RootPanel.get().add(classPresenter.getDisplay());

        String allTokens = History.getToken();
        if (allTokens != null)
        {
            String tokens[] = allTokens.split("\\|");
            for (String token : tokens)
            {
                String tokenPart[] = token.split("=");
                if (tokenPart.length == 2)
                {
                    String name = tokenPart[0];
                    String value = tokenPart[1];

                    if (name.equals("config"))
                    {
                        classPresenter.getMenuDisplay().setLoadConfigContext(value);
                    }
                    else if (name.equals("libs"))
                    {
                        classPresenter.getMenuDisplay().setLoadLibsContext(value);
                    }
                }
            }
        }
    }

}

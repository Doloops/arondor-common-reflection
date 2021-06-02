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

import java.util.logging.Level;
import java.util.logging.Logger;

import com.arondor.common.reflection.bean.config.ObjectConfigurationFactoryBean;
import com.arondor.common.reflection.gwt.client.api.AccessibleClassPresenter;
import com.arondor.common.reflection.gwt.client.presenter.ObjectReferencesProvider;
import com.arondor.common.reflection.gwt.client.presenter.ReflectionDesignerPresenter;
import com.arondor.common.reflection.gwt.client.service.GWTReflectionServiceAsync;
import com.arondor.common.reflection.gwt.client.service.RestDirectReflectionServiceAsync;
import com.arondor.common.reflection.model.config.ObjectConfiguration;
import com.arondor.common.reflection.model.config.ObjectConfigurationFactory;
import com.arondor.common.reflection.xstream.GWTObjectConfigurationParser;
import com.arondor.common.reflection.xstream.GWTObjectConfigurationSerializer;
import com.google.gwt.core.client.EntryPoint;
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

        RestDirectReflectionServiceAsync.fetchReflection("allclasses.xml",
                new AsyncCallback<RestDirectReflectionServiceAsync>()
                {

                    @Override
                    public void onFailure(Throwable caught)
                    {
                        LOG.log(Level.SEVERE, "Could not get refelction !", caught);
                        Window.alert("Could not get reflection : " + caught.getMessage());
                    }

                    @Override
                    public void onSuccess(RestDirectReflectionServiceAsync reflectionService)
                    {
                        continueLoading(reflectionService);
                    }
                });

    }

    private GWTObjectConfigurationSerializer serializer = new GWTObjectConfigurationSerializer();

    private GWTObjectConfigurationParser parser = new GWTObjectConfigurationParser();

    private final ObjectReferencesProvider objectReferencesProvider = null;

    private AccessibleClassPresenter rootPresenter;

    private void continueLoading(GWTReflectionServiceAsync reflectionService)
    {

        String baseClassName = Window.Location.getParameter("baseClassName");
        if (baseClassName == null || baseClassName.isEmpty())
        {
            baseClassName = "com.arondor.fast2p8.model.task.Task";
        }
        rootPresenter = AccessibleClassPresenterFactory.createAccessibleClassPresenter(reflectionService,
                objectReferencesProvider, baseClassName, null);

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
        ObjectConfiguration objectConfiguration = rootPresenter.getObjectConfiguration();

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

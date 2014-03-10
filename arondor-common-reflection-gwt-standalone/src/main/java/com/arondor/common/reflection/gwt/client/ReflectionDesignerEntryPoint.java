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

import com.arondor.common.reflection.gwt.client.presenter.ReflectionDesignerPresenter;
import com.arondor.common.reflection.gwt.client.service.CacheGWTReflectionServiceAsync;
import com.arondor.common.reflection.gwt.client.service.GWTReflectionService;
import com.arondor.common.reflection.gwt.client.service.GWTReflectionServiceAsync;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.RootPanel;

public class ReflectionDesignerEntryPoint implements EntryPoint
{

    public void onModuleLoad()
    {
        GWTReflectionServiceAsync reflectionService = GWT.create(GWTReflectionService.class);
        GWTReflectionServiceAsync cachedReflectionService = new CacheGWTReflectionServiceAsync(reflectionService);

        bind();
        String baseClassName = java.lang.Object.class.getName();
        ReflectionDesignerPresenter classPresenter = new ReflectionDesignerPresenter(cachedReflectionService,
                baseClassName);

        RootPanel.get().clear();
        RootPanel.get().add(classPresenter.getDisplay());

        // String Window.Location.getParameter("config");
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

    private void bind()
    {
    }
}

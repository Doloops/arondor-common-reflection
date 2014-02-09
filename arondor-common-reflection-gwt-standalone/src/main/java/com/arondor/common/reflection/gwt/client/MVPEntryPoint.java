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

import com.arondor.common.reflection.gwt.client.presenter.ClassDesignerPresenter;
import com.arondor.common.reflection.gwt.client.view.ClassDesignerView;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;

public class MVPEntryPoint implements EntryPoint
{

    public void onModuleLoad()
    {
        bind();
        String baseClassName = java.lang.Object.class.getName();
        ClassDesignerPresenter classPresenter = new ClassDesignerPresenter(new ClassDesignerView(), baseClassName);

        RootPanel.get().clear();
        RootPanel.get().add(classPresenter.getDisplay());
    }

    private void bind()
    {
    }
}

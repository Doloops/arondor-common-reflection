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
package com.arondor.common.reflection.gwt.client.view;

import com.arondor.common.reflection.gwt.client.presenter.ReflectionDesignerPresenter;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class ReflectionDesignerMenuView extends Composite implements ReflectionDesignerPresenter.MenuDisplay
{
    private Button getConfigButton = new Button("Get");

    private Button setConfigButton = new Button("Set");

    private final AbsolutePanel content = new AbsolutePanel();

    public ReflectionDesignerMenuView()
    {
        content.add(getConfigButton);
        content.add(setConfigButton);
        initWidget(content);
    }

    public Widget asWidget()
    {
        return this;
    }

    public HasClickHandlers getGetConfigButton()
    {
        return getConfigButton;
    }

    public HasClickHandlers getSetConfigButton()
    {
        return setConfigButton;
    }

    // public void
    // setAccessibleClassView(HierarchicAccessibleClassPresenter.Display
    // accessibleClassView)
    // {
    // this.content.add(accessibleClassView);
    // }

}

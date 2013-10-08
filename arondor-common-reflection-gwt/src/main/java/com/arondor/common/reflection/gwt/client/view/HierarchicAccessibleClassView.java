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

import com.arondor.common.reflection.gwt.client.presenter.ClassTreePresenter;
import com.arondor.common.reflection.gwt.client.presenter.ClassTreePresenter.Display;
import com.arondor.common.reflection.gwt.client.presenter.HierarchicAccessibleClassPresenter;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

public class HierarchicAccessibleClassView extends Composite implements HierarchicAccessibleClassPresenter.Display
{
    private ClassTreePresenter.Display classTreeDisplay = new ClassTreeView();

    public HierarchicAccessibleClassView()
    {
        AbsolutePanel content = new AbsolutePanel();

        initWidget(content);

        content.add(classTreeDisplay);
    }

    public Display getClassTreeDisplay()
    {
        return classTreeDisplay;
    }

    public IsWidget getClassTreeDisplayWidget()
    {
        return classTreeDisplay;
    }

    public Widget asWidget()
    {
        return this;
    }

}

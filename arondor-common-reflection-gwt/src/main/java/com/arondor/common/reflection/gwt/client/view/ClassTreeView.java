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

import com.arondor.common.reflection.gwt.client.presenter.ClassTreeNodePresenter;
import com.arondor.common.reflection.gwt.client.presenter.ClassTreePresenter;
import com.arondor.common.reflection.gwt.client.presenter.ClassTreePresenter.Display;
import com.arondor.common.reflection.gwt.client.presenter.HierarchicAccessibleClassPresenter;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.Widget;

public class ClassTreeView extends Composite
        implements ClassTreePresenter.Display, HierarchicAccessibleClassPresenter.Display
{
    private Tree tree = new Tree()
    {
        @Override
        public void onBrowserEvent(Event event)
        {
            int eventType = DOM.eventGetType(event);
            switch (eventType)
            {
            case Event.ONKEYDOWN:
            case Event.ONKEYUP:
            {
                if (KeyCodes.isArrowKey(event.getKeyCode()))
                {
                    return;
                }
            }
            }
            super.onBrowserEvent(event);
        }
    };

    public ClassTreeView()
    {
        AbsolutePanel content = new AbsolutePanel();
        initWidget(content);

        content.add(tree);
    }

    private Tree getTree()
    {
        return tree;
    }

    @Override
    public ClassTreeNodePresenter.ClassDisplay createRootView(String baseClassName)
    {
        return new ClassTreeNodeView(getTree());
    }

    @Override
    public Widget asWidget()
    {
        return this;
    }

    @Override
    public Display getClassTreeDisplay()
    {
        return this;
    }
}

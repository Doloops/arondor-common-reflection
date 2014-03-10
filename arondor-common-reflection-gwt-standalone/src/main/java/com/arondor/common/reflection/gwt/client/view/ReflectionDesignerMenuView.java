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
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ReflectionDesignerMenuView extends Composite implements ReflectionDesignerPresenter.MenuDisplay
{
    private Button loadConfigButton = new Button("Load Config");

    private TextBox loadConfigContext = new TextBox();

    private Button loadLibsButton = new Button("Load Libs");

    private TextBox loadLibsContext = new TextBox();

    private Button saveConfigButton = new Button("Save");

    public ReflectionDesignerMenuView()
    {
        if (false)
        {
            loadConfigContext
                    .setText("file:///home/francois/ARender-Rendition-2.2.0/conf/arender-rendition-alterdocumentcontent.xml");
            loadLibsContext.setText("/home/francois/ARender-Rendition-2.2.0/lib");
        }
        loadConfigContext.setWidth("300px");
        loadLibsContext.setWidth("300px");
        VerticalPanel verticalPanel = new VerticalPanel();
        {
            HorizontalPanel hz = new HorizontalPanel();
            hz.add(loadLibsContext);
            hz.add(loadLibsButton);
            verticalPanel.add(hz);
        }
        {
            HorizontalPanel hz = new HorizontalPanel();
            hz.add(loadConfigContext);
            hz.add(loadConfigButton);
            verticalPanel.add(hz);
        }
        verticalPanel.add(saveConfigButton);
        initWidget(verticalPanel);
    }

    public Widget asWidget()
    {
        return this;
    }

    public HasClickHandlers getLoadConfigButton()
    {
        return loadConfigButton;
    }

    public HasClickHandlers getSaveButton()
    {
        return saveConfigButton;
    }

    public String getLoadConfigContext()
    {
        return loadConfigContext.getText();
    }

    public void setLoadConfigContext(String context)
    {
        loadConfigContext.setText(context);
    }

    public String getLoadLibsContext()
    {
        return loadLibsContext.getText();
    }

    public void setLoadLibsContext(String context)
    {
        loadLibsContext.setText(context);
    }

    public HasClickHandlers getLoadLibsButton()
    {
        return loadLibsButton;
    }

}

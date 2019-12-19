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
package com.arondor.common.reflection.gwt.client.view.fields;

import com.arondor.common.reflection.gwt.client.presenter.fields.MapTreeNodePresenter;
import com.arondor.common.reflection.gwt.client.presenter.fields.MapTreeNodePresenter.MapNodeDisplay;
import com.arondor.common.reflection.gwt.client.view.AbstractTreeNodeView;
import com.arondor.common.reflection.gwt.client.view.resources.Images;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.UIObject;

public class MapRootView extends AbstractTreeNodeView implements MapTreeNodePresenter.MapRootDisplay
{
    private static final Images IMAGES = GWT.create(Images.class);

    private HasClickHandlers addElement;

    public MapRootView(UIObject parentNode)
    {
        super(parentNode);
        Image addButton = new Image(IMAGES.add16());
        addButton.getElement().getStyle().setCursor(Cursor.POINTER);
        addButton.getElement().getStyle().setMarginLeft(5, Unit.PX);
        getContents().add(addButton);
        addElement = addButton;
        setHasRemoveButton(true);
    }

    @Override
    public void clear()
    {
        removeItems();
    }

    @Override
    public HasClickHandlers addElementClickHandler()
    {
        return addElement;
    }

    @Override
    public MapNodeDisplay createChildNode()
    {
        setActive(true);
        MapNodeView mapNodeDisplay = new MapNodeView(this);

        /**
         * Enforce element visibility
         */
        mapNodeDisplay.setVisible(true);
        return mapNodeDisplay;
    }

    @Override
    public void setProperLabel(String label)
    {
        // TODO Auto-generated method stub

    }
}

package com.arondor.common.reflection.gwt.client.view.fields;

import com.arondor.common.reflection.gwt.client.presenter.fields.MapTreeNodePresenter;
import com.arondor.common.reflection.gwt.client.presenter.fields.MapTreeNodePresenter.MapNodeDisplay;
import com.arondor.common.reflection.gwt.client.view.AbstractTreeNodeView;
import com.arondor.common.reflection.gwt.client.view.resources.Images;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.HasVisibility;
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

    public void clear()
    {
        removeItems();
    }

    public HasClickHandlers addElementClickHandler()
    {
        return addElement;
    }

    public MapNodeDisplay createChildNode()
    {
        setActive(true);
        MapNodeDisplay mapNodeDisplay = new MapNodeView(this);

        /**
         * Enforce element visibility
         */
        if (mapNodeDisplay instanceof HasVisibility)
        {
            ((HasVisibility) mapNodeDisplay).setVisible(true);
        }
        return mapNodeDisplay;
    }
}

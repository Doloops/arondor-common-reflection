package com.arondor.common.reflection.gwt.client.view.fields;

import com.arondor.common.reflection.gwt.client.presenter.TreeNodePresenter.Display;
import com.arondor.common.reflection.gwt.client.presenter.fields.ListTreeNodePresenter.ListRootDisplay;
import com.arondor.common.reflection.gwt.client.view.AbstractChildCreatorNodeView;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.UIObject;

public class ListRootView extends AbstractChildCreatorNodeView implements ListRootDisplay
{
    private HasClickHandlers addElement;

    public ListRootView(UIObject parentNode)
    {
        super(parentNode);
        Button addButton = new Button("(+)");
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

    public void removeChild(Display childDisplay)
    {
        if (childDisplay instanceof TreeItem)
        {
            removeItem((TreeItem) childDisplay);
        }
    }
}

package com.arondor.common.reflection.gwt.client.view.fields;

import com.arondor.common.reflection.gwt.client.presenter.fields.MapTreeNodePresenter;
import com.arondor.common.reflection.gwt.client.view.AbstractChildCreatorNodeView;
import com.google.gwt.user.client.ui.UIObject;

public class MapNodeView extends AbstractChildCreatorNodeView implements MapTreeNodePresenter.MapNodeDisplay
{

    protected MapNodeView(UIObject parentNode)
    {
        super(parentNode);
        setHasRemoveButton(true);
        setActive(true);
    }

    public void clear()
    {
        getParentItem().removeItem(this);
    }

}

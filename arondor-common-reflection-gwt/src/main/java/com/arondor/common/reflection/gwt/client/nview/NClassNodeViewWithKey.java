package com.arondor.common.reflection.gwt.client.nview;

import com.arondor.common.reflection.gwt.client.presenter.TreeNodePresenter.Display;
import com.arondor.common.reflection.gwt.client.presenter.fields.MapTreeNodePresenter.MapPairDisplay;
import com.arondor.common.reflection.gwt.client.presenter.fields.PrimitiveTreeNodePresenter.PrimitiveDisplay;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

import gwt.material.design.client.ui.MaterialTextBox;

public class NClassNodeViewWithKey extends NClassNodeView implements MapPairDisplay
{
    private final MaterialTextBox keyTextBox = new MaterialTextBox();

    public NClassNodeViewWithKey(String keyClass, String valueClass)
    {

    }

    @Override
    public void bind()
    {
        if (keyTextBox != null)
        {
            add(keyTextBox);
        }
        super.bind();
    }

    @Override
    public HasClickHandlers removePairClickHandler()
    {
        return new HasClickHandlers()
        {

            @Override
            public void fireEvent(GwtEvent<?> event)
            {
                // TODO Auto-generated method stub
            }

            @Override
            public HandlerRegistration addClickHandler(ClickHandler handler)
            {
                // TODO Auto-generated method stub
                return null;
            }
        };
    }

    @Override
    public PrimitiveDisplay getKeyDisplay()
    {
        /*
         * Force adding the Key TextBox if is has not been added at bind() time
         */
        if (keyTextBox.getParent() == null)
        {
            insert(keyTextBox, 0);
        }
        return new PrimitiveMaterialDisplay(keyTextBox);
    }

    @Override
    public Display getValueDisplay()
    {
        return this;
    }
}

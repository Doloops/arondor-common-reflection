package com.arondor.common.reflection.gwt.client.nview;

import com.arondor.common.reflection.gwt.client.presenter.fields.MapTreeNodePresenter.MapNodeDisplay;
import com.arondor.common.reflection.gwt.client.presenter.fields.MapTreeNodePresenter.MapRootDisplay;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

import gwt.material.design.client.ui.html.Label;

public class NMapNodeView extends NNodeView implements MapRootDisplay
{

    public NMapNodeView()
    {
        add(new Label("Map View !"));
    }

    @Override
    public void setNodeDescription(String description)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public HasClickHandlers addElementClickHandler()
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
                return new HandlerRegistration()
                {
                    @Override
                    public void removeHandler()
                    {
                    }
                };
            }
        };
    }

    @Override
    public MapNodeDisplay createChildNode()
    {
        // TODO Auto-generated method stub
        return null;
    }

}

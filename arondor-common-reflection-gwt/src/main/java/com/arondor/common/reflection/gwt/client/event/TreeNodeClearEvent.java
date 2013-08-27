package com.arondor.common.reflection.gwt.client.event;

import com.google.gwt.event.shared.GwtEvent;

public class TreeNodeClearEvent extends GwtEvent<TreeNodeClearEvent.Handler>
{
    public static final Type<Handler> TYPE = new Type<Handler>();

    public interface Handler extends com.google.gwt.event.shared.EventHandler
    {
        void onTreeNodeClearEvent(TreeNodeClearEvent treeNodeClearEvent);
    }

    public com.google.gwt.event.shared.GwtEvent.Type<TreeNodeClearEvent.Handler> getAssociatedType()
    {
        return TYPE;
    }

    @Override
    protected void dispatch(TreeNodeClearEvent.Handler handler)
    {
        handler.onTreeNodeClearEvent(this);
    }
}

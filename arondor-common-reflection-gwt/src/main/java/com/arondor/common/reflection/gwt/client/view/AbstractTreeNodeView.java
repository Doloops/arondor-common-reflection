package com.arondor.common.reflection.gwt.client.view;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.RepeatingCommand;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.UIObject;

public class AbstractTreeNodeView extends TreeItem
{
    private final Label nodeLabel = new Label("");

    private static final int NODE_LABEL_MARGIN_WIDTH = 140;

    private HorizontalPanel contents = new HorizontalPanel();

    protected AbstractTreeNodeView(UIObject parentNode)
    {
        addItemToParent(parentNode);
        nodeLabel.addStyleDependentName("classTreeNodeView");
        contents.add(nodeLabel);

        setWidget(contents);

    }

    private void addItemToParent(UIObject parentNode)
    {
        if (parentNode instanceof Tree)
        {
            ((Tree) parentNode).addItem(this);
        }
        else if (parentNode instanceof TreeItem)
        {
            ((TreeItem) parentNode).addItem(this);
        }
    }

    public void setNodeName(String name)
    {
        nodeLabel.setText(name);
    }

    @Override
    public void setState(boolean state, boolean fireEvents)
    {
        super.setState(state, fireEvents);
        if (state)
        {
            for (int idx = 0; idx < getChildCount(); idx++)
            {
                getChild(idx).setVisible(true);
            }
        }
    }

    @Override
    public void setVisible(boolean visible)
    {
        Scheduler.get().scheduleFixedPeriod(new RepeatingCommand()
        {
            public boolean execute()
            {
                int nodeLabelWidth = nodeLabel.getElement().getClientWidth();
                int diff = NODE_LABEL_MARGIN_WIDTH - nodeLabelWidth;
                if (diff > 0)
                {
                    nodeLabel.getElement().getStyle().setMarginRight(diff, Unit.PX);
                }
                return false;
            }
        }, 100);
    }

    public HasWidgets getContents()
    {
        return contents;
    }

    public void setNodeDescription(String description)
    {
        nodeLabel.setTitle(description);
    }
}

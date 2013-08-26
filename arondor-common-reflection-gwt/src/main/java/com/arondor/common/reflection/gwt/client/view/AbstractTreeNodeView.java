package com.arondor.common.reflection.gwt.client.view;

import com.arondor.common.reflection.gwt.client.presenter.TreeNodePresenter;
import com.arondor.common.reflection.gwt.client.view.resources.Images;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.RepeatingCommand;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

public abstract class AbstractTreeNodeView extends TreeItem implements TreeNodePresenter.Display
{
    private final Label nodeLabel = new Label("");

    private static final int NODE_LABEL_MARGIN_WIDTH = 300;

    private static final int NODE_LABEL_MINIMUM_MARGIN_WIDTH = 10;

    private boolean hasRemoveButton = false;

    private HorizontalPanel contents = new HorizontalPanel()
    {
        @Override
        public void add(Widget w)
        {
            if (clearButton.getParent() != null)
            {
                clearButton.removeFromParent();
            }
            super.add(w);
            super.add(clearButton);
        }
    };

    private static final Images IMAGE_RESOURCES = GWT.create(Images.class);

    private final Image clearButton = new Image(IMAGE_RESOURCES.remove16());

    protected AbstractTreeNodeView(UIObject parentNode)
    {
        addItemToParent(parentNode);
        nodeLabel.addStyleDependentName("classTreeNodeView");

        contents.add(clearButton);
        contents.add(nodeLabel);

        clearButton.getElement().getStyle().setCursor(Cursor.POINTER);
        clearButton.setVisible(false);
        clearButton.getElement().getStyle().setMarginLeft(5, Unit.PX);
        clearButton.getElement().getStyle().setMarginRight(5, Unit.PX);
        clearButton.setTitle("Reset field value (use implementation default value)");
        setWidget(contents);

        bind();
    }

    private void bind()
    {
        clearButton.addClickHandler(new ClickHandler()
        {
            public void onClick(ClickEvent event)
            {
                clearValue();
            }
        });
    }

    private void addItemToParent(UIObject parentNode)
    {
        if (parentNode instanceof Tree)
        {
            ((Tree) parentNode).addItem(this);
        }
        else if (parentNode instanceof TreeItem)
        {
            TreeItem parentNodeItem = (TreeItem) parentNode;
            int childCount = parentNodeItem.getChildCount();
            int nodeDepth = 0;
            for (TreeItem ancestor = parentNodeItem; ancestor != null; ancestor = ancestor.getParentItem())
            {
                nodeDepth++;
            }
            parentNodeItem.addItem(this);

            String colors[] = { "#F5F5F2", "#DBDAD8", "#F9F8F6", "#E7E7E3" };
            int colorIndex = ((nodeDepth % 2) + (childCount % 2)) % 2;
            getElement().getStyle().setBackgroundColor(colors[colorIndex]);
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

    /**
     * Overriden setVisible to setup widget layout
     */
    @Override
    public void setVisible(boolean visible)
    {
        Scheduler.get().scheduleFixedPeriod(new RepeatingCommand()
        {
            public boolean execute()
            {
                int nodeLabelWidth = nodeLabel.getElement().getClientWidth();
                int diff = NODE_LABEL_MARGIN_WIDTH - nodeLabelWidth;
                diff = Math.max(diff, NODE_LABEL_MINIMUM_MARGIN_WIDTH);
                nodeLabel.getElement().getStyle().setMarginRight(diff, Unit.PX);
                return false;
            }
        }, 50);
    }

    public HasWidgets getContents()
    {
        return contents;
    }

    public void setNodeDescription(String description)
    {
        nodeLabel.setTitle(description);
    }

    public void setActive(boolean active)
    {
        nodeLabel.getElement().getStyle().setFontWeight(active ? FontWeight.BOLD : FontWeight.NORMAL);
        if (hasRemoveButton)
        {
            clearButton.setVisible(active);
        }
    }

    private void clearValue()
    {
        clear();
        setActive(false);
    }

    public boolean isHasRemoveButton()
    {
        return hasRemoveButton;
    }

    public void setHasRemoveButton(boolean hasRemoveButton)
    {
        this.hasRemoveButton = hasRemoveButton;
    }

}

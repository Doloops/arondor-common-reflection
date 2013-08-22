package com.arondor.common.reflection.gwt.client.presenter;

import java.util.logging.Logger;

import com.arondor.common.reflection.gwt.client.service.GWTReflectionServiceAsync;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Tree;

public class ClassTreePresenter
{

    @SuppressWarnings("unused")
    private static final Logger LOG = Logger.getLogger(ClassTreePresenter.class.getName());

    public interface Display extends IsWidget
    {
        ClassTreeNodePresenter.Display createRootView();

        HandlerRegistration addSelectionHandler(SelectionHandler<ClassTreeNodePresenter.Display> selectionHandler);

        Tree getTree();
    }

    private ClassTreeNodePresenter rootNodePresenter;

    private final String accessibleClassName;

    private final Display display;

    public ClassTreePresenter(GWTReflectionServiceAsync rpcService, String accessibleClassName, Display view)
    {
        this.accessibleClassName = accessibleClassName;
        this.display = view;

        setRootNodePresenter(new ClassTreeNodePresenter(rpcService, null, accessibleClassName, display.createRootView()));

        bind();
    }

    private void bind()
    {

    }

    private static class ClassTreeNodePresenterSelectionEvent extends SelectionEvent<ClassTreeNodePresenter>
    {
        protected ClassTreeNodePresenterSelectionEvent(ClassTreeNodePresenter selectedItem)
        {
            super(selectedItem);
        }
    }

    public HandlerRegistration addSelectionHandler(final SelectionHandler<ClassTreeNodePresenter> selectionHandler)
    {
        return display.addSelectionHandler(new SelectionHandler<ClassTreeNodePresenter.Display>()
        {
            public void onSelection(SelectionEvent<ClassTreeNodePresenter.Display> event)
            {
                selectionHandler.onSelection(new ClassTreeNodePresenterSelectionEvent(event.getSelectedItem()
                        .getClassTreeNodePresenter()));
            }
        });
    }

    public ClassTreeNodePresenter findNode(String classType)
    {
        if (rootNodePresenter.getBaseClassName().equals(classType))
        {
            return rootNodePresenter;
        }
        else
        {
            ClassTreeNodePresenter result = rootNodePresenter.findNode(classType);
            if (result != null)
            {
                return result;
            }
        }
        return null;
    }

    public ClassTreeNodePresenter getRootNodePresenter()
    {
        return rootNodePresenter;
    }

    public void setRootNodePresenter(ClassTreeNodePresenter rootNodePresenter)
    {
        this.rootNodePresenter = rootNodePresenter;
    }

    public String getAccessibleClassName()
    {
        return accessibleClassName;
    }

    public Display getDisplay()
    {
        return display;
    }

    public IsWidget getDisplayWidget()
    {
        return getDisplay();
    }
}

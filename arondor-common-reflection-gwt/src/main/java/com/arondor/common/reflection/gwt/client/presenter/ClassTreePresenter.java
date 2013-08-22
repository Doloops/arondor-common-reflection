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

    private static final Logger LOG = Logger.getLogger(ClassTreePresenter.class.getName());

    public interface Display extends IsWidget
    {
        ClassTreeNodePresenter.Display createRootView();

        HandlerRegistration addSelectionHandler(SelectionHandler<ClassTreeNodePresenter.Display> selectionHandler);

        Tree getTree();
    }

    private final Display display;

    private final String accessibleClassName;

    private final GWTReflectionServiceAsync rpcService;

    private ClassTreeNodePresenter rootNodePresenter;

    public ClassTreePresenter(String accessibleClassName, GWTReflectionServiceAsync rpcService, Display view)
    {
        this.accessibleClassName = accessibleClassName;
        this.rpcService = rpcService;
        this.display = view;

        setRootNodePresenter(new ClassTreeNodePresenter(rpcService, null, accessibleClassName, display.createRootView()));

        bind();
    }

    private void bind()
    {

    }

    public Display getDisplay()
    {
        return display;
    }

    public IsWidget getDisplayWidget()
    {
        return getDisplay();
    }

    public ClassTreeNodePresenter getRootNodePresenter()
    {
        return rootNodePresenter;
    }

    public void setRootNodePresenter(ClassTreeNodePresenter rootNodePresenter)
    {
        this.rootNodePresenter = rootNodePresenter;
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
}

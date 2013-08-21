package com.arondor.common.reflection.gwt.client.presenter;

import java.util.logging.Logger;

import com.arondor.common.reflection.gwt.client.service.GWTReflectionServiceAsync;
import com.arondor.common.reflection.gwt.client.view.ClassTreeNodeView;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Tree;

public class ClassTreePresenter
{

    private static final Logger LOG = Logger.getLogger(ClassTreePresenter.class.getName());

    public interface Display extends IsWidget
    {

        Tree getTree();
    }

    private final Display display;

    private final String accessibleClassName;

    private final GWTReflectionServiceAsync rpcService;

    private ClassTreeNodePresenter parentNodePresenter;

    public ClassTreePresenter(String accessibleClassName, GWTReflectionServiceAsync rpcService, Display view)
    {
        this.accessibleClassName = accessibleClassName;
        this.rpcService = rpcService;
        this.display = view;

        setParentNodePresenter(new ClassTreeNodePresenter(accessibleClassName, rpcService, new ClassTreeNodeView(
                display.getTree())));

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

    public ClassTreeNodePresenter getParentNodePresenter()
    {
        return parentNodePresenter;
    }

    public void setParentNodePresenter(ClassTreeNodePresenter parentNodePresenter)
    {
        this.parentNodePresenter = parentNodePresenter;
    }
}

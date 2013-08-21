package com.arondor.common.reflection.gwt.client.presenter;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import com.arondor.common.reflection.gwt.client.service.GWTReflectionServiceAsync;
import com.arondor.common.reflection.gwt.client.view.ClassTreeNodeView;
import com.arondor.common.reflection.model.java.AccessibleClass;
import com.arondor.common.reflection.model.java.AccessibleField;
import com.arondor.common.reflection.util.PrimitiveTypeUtil;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.TreeItem;

public class ClassTreeNodePresenter
{

    private static final Logger LOG = Logger.getLogger(ClassTreeNodePresenter.class.getName());

    public interface Display extends IsWidget
    {
        TreeItem getTreeItem();

        void addItem(String child);

        void setNodeName(String name);
    }

    private Map<String, ClassTreeNodePresenter> classTreeNodePresenterList;

    private final Display display;

    private final String accessibleClassName;

    private final GWTReflectionServiceAsync rpcService;

    public ClassTreeNodePresenter(String accessibleClassName, GWTReflectionServiceAsync rpcService, Display view)
    {
        this.accessibleClassName = accessibleClassName;
        this.rpcService = rpcService;
        this.display = view;

        classTreeNodePresenterList = new HashMap<String, ClassTreeNodePresenter>();

        bind();

        display.setNodeName(accessibleClassName);
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

    public void addClassTreePresenter(String accessibleClassName)
    {
        fetchAccessibleClass(accessibleClassName);
    }

    private void fetchAccessibleClass(String className)
    {
        rpcService.getAccessibleClass(className, new AsyncCallback<AccessibleClass>()
        {

            public void onSuccess(AccessibleClass result)
            {
                buildTree(result);
            }

            public void onFailure(Throwable caught)
            {
                Window.alert("Error fetching Accessible Class");
            }
        });
    }

    public void buildTree(AccessibleClass accessibleClass)
    {
        for (AccessibleField accessibleField : accessibleClass.getAccessibleFields().values())
        {
            String fieldClassName = accessibleField.getClassName();
            if (!PrimitiveTypeUtil.isPrimitiveType(fieldClassName))
            {
                classTreeNodePresenterList.put(fieldClassName, new ClassTreeNodePresenter(fieldClassName, rpcService,
                        new ClassTreeNodeView(display.getTreeItem())));
                classTreeNodePresenterList.get(fieldClassName).addClassTreePresenter(fieldClassName);

            }
        }
    }
}

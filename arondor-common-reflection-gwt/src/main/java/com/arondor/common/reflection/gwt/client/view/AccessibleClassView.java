package com.arondor.common.reflection.gwt.client.view;

import com.arondor.common.reflection.gwt.client.presenter.AccessibleFieldMapPresenter;
import com.arondor.common.reflection.gwt.client.presenter.ClassTreePresenter;
import com.arondor.common.reflection.gwt.client.presenter.ClassTreePresenter.Display;
import com.arondor.common.reflection.gwt.client.presenter.SimpleAccessibleClassPresenter;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.Widget;

public class AccessibleClassView extends Composite implements SimpleAccessibleClassPresenter.Display
{
    private HTML name;

    private HTML className;

    private ListBox implListInput;

    private AccessibleFieldMapPresenter.Display fieldMapDisplay = new AccessibleFieldMapView();

    TreeItem treeItem = new TreeItem();

    private ClassTreePresenter.Display classTreeDisplay = new ClassTreeView();

    public AccessibleClassView()
    {
        AbsolutePanel content = new AbsolutePanel();

        initWidget(content);

        content.add(new Label("Accessible Class :"));

        name = new HTML();
        className = new HTML();

        FormPanel form = new FormPanel();
        form.setStyleName("form-inline");

        implListInput = new ListBox();
        implListInput.setStyleName("form-control");
        implListInput.addItem("");
        implListInput.addItem("Impl 1");
        implListInput.addItem("Impl 2");

        form.add(implListInput);

        content.add(name);
        content.add(className);
        content.add(form);

        content.add(classTreeDisplay);

        content.add(fieldMapDisplay);
    }

    public Widget asWidget()
    {
        return this;
    }

    public void setName(String name)
    {
        this.name.setText("Name : " + name);
    }

    public void setClassname(String classname)
    {
        className.setText("Classname : " + classname);
    }

    public AccessibleFieldMapPresenter.Display getFieldMapDisplay()
    {
        return fieldMapDisplay;
    }

    public Display getClassTreeDisplay()
    {
        return classTreeDisplay;
    }

    public IsWidget getClassTreeDisplayWidget()
    {
        return classTreeDisplay;
    }

    public ListBox getImplListInput()
    {
        return implListInput;
    }

    public void addImplementation(String implementationClassName)
    {
        implListInput.addItem(implementationClassName);
    }

}

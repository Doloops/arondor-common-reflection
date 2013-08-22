package com.arondor.common.reflection.gwt.client.view;

import java.util.logging.Logger;

import com.arondor.common.reflection.gwt.client.presenter.ClassTreePresenter;
import com.arondor.common.reflection.gwt.client.presenter.ClassTreePresenter.Display;
import com.arondor.common.reflection.gwt.client.presenter.HierarchicAccessibleClassPresenter;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class HierarchicAccessibleClassView extends Composite implements HierarchicAccessibleClassPresenter.Display
{
    private static final Logger LOG = Logger.getLogger(HierarchicAccessibleClassView.class.getName());

    private ClassTreePresenter.Display classTreeDisplay = new ClassTreeView();

    private HTML name;

    private HTML className;

    // private AccessibleFieldMapPresenter.Display fieldMapDisplay = new
    // AccessibleFieldMapView();

    private SimplePanel implementingClassPlaceHolder = new SimplePanel();

    private SimplePanel fieldMapPlaceHolder = new SimplePanel();

    public HierarchicAccessibleClassView()
    {
        AbsolutePanel content = new AbsolutePanel();

        initWidget(content);

        AbsolutePanel btnGroup = new AbsolutePanel();
        btnGroup.setStyleName("btn-group");

        content.add(new HTML("<h1>Class Designer</h1>"));

        // setConfigButton = new Button("Get");
        // setConfigButton.setStyleName("btn");
        // setConfigButton.addStyleName("btn-primary");
        //
        // btnGroup.add(setConfigButton);
        //
        // saveConfigButton = new Button("Save");
        // saveConfigButton.setStyleName("btn");
        // saveConfigButton.addStyleName("btn-primary");
        //
        // btnGroup.add(saveConfigButton);

        content.add(btnGroup);

        content.add(new Label("Accessible Class :"));

        name = new HTML();
        className = new HTML();

        FormPanel form = new FormPanel();
        form.setStyleName("form-inline");

        content.add(name);
        content.add(className);
        content.add(form);

        content.add(implementingClassPlaceHolder);

        content.add(classTreeDisplay);

        content.add(fieldMapPlaceHolder);
    }

    public Display getClassTreeDisplay()
    {
        return classTreeDisplay;
    }

    public IsWidget getClassTreeDisplayWidget()
    {
        return classTreeDisplay;
    }

    public void setName(String name)
    {
        this.name.setText("Name : " + name);
    }

    public void setClassname(String classname)
    {
        className.setText("Classname : " + classname);
    }

    public Widget asWidget()
    {
        return this;
    }

    public void setCurrentSelectedNode(
            com.arondor.common.reflection.gwt.client.presenter.ClassTreeNodePresenter.Display nodeDisplay)
    {
        // implementingClassPlaceHolder.clear();
        // implementingClassPlaceHolder.add(nodeDisplay.getImplementingClassDisplay());
        //
        fieldMapPlaceHolder.clear();
        fieldMapPlaceHolder.add(nodeDisplay.getFieldMapDisplay());
    }
}

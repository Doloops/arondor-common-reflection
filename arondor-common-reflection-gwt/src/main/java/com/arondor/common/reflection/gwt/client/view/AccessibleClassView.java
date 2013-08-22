package com.arondor.common.reflection.gwt.client.view;

import java.util.logging.Logger;

import com.arondor.common.reflection.gwt.client.presenter.AccessibleFieldMapPresenter;
import com.arondor.common.reflection.gwt.client.presenter.ClassTreePresenter;
import com.arondor.common.reflection.gwt.client.presenter.ClassTreePresenter.Display;
import com.arondor.common.reflection.gwt.client.presenter.SimpleAccessibleClassPresenter;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
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
    private static final Logger LOG = Logger.getLogger(AccessibleClassView.class.getName());

    private HTML name;

    private HTML className;

    private Button setConfigButton;

    private Button saveConfigButton;

    private ListBox implListInput;

    private AccessibleFieldMapPresenter.Display fieldMapDisplay = new AccessibleFieldMapView();

    public AccessibleClassView()
    {
        AbsolutePanel content = new AbsolutePanel();

        initWidget(content);

        AbsolutePanel btnGroup = new AbsolutePanel();
        btnGroup.setStyleName("btn-group");

        content.add(new HTML("<h1>Class Designer</h1>"));

        setConfigButton = new Button("Get");
        setConfigButton.setStyleName("btn");
        setConfigButton.addStyleName("btn-primary");

        btnGroup.add(setConfigButton);

        saveConfigButton = new Button("Save");
        saveConfigButton.setStyleName("btn");
        saveConfigButton.addStyleName("btn-primary");

        btnGroup.add(saveConfigButton);

        content.add(btnGroup);

        content.add(new Label("Accessible Class :"));

        name = new HTML();
        className = new HTML();

        FormPanel form = new FormPanel();
        form.setStyleName("form-inline");

        implListInput = new ListBox();
        implListInput.setStyleName("form-control");

        form.add(implListInput);

        content.add(name);
        content.add(className);
        content.add(form);

        content.add(classTreeDisplay);

        content.add(fieldMapDisplay);
    }

    TreeItem treeItem = new TreeItem();

    private ClassTreePresenter.Display classTreeDisplay = new ClassTreeView();

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

    public HasClickHandlers getImplListInput()
    {
        return implListInput;
    }

    public String getImplListInputValue()
    {
        return implListInput.getItemText(implListInput.getSelectedIndex());
    }

    public void addImplementation(String implementationClassName)
    {
        implListInput.addItem(implementationClassName);
    }

    public HasClickHandlers getSetConfigButton()
    {
        return setConfigButton;
    }

    public HasClickHandlers getSaveConfigButton()
    {
        return saveConfigButton;
    }

    public void clearImpl()
    {
        for (int i = implListInput.getItemCount() - 1; i >= 0; i--)
        {
            implListInput.removeItem(i);
        }
    }

    public void setSelectedImplementation(String implementation)
    {
        for (int i = 0; i < implListInput.getItemCount(); i++)
        {
            if (implListInput.getItemText(i).equals(implementation))
            {
                implListInput.setSelectedIndex(i);
            }
        }
    }
}

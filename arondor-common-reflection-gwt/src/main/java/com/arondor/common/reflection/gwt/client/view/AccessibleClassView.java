package com.arondor.common.reflection.gwt.client.view;

import com.arondor.common.reflection.gwt.client.presenter.SimpleAccessibleClassPresenter;
import com.arondor.common.reflection.model.config.ObjectConfiguration;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class AccessibleClassView extends Composite implements SimpleAccessibleClassPresenter.Display
{
    private AccessibleFieldListView accessibleFieldListView;

    private HTML name;

    private HTML className;

    private AbsolutePanel content = new AbsolutePanel();

    public AccessibleClassView()
    {
        initWidget(content);

        content.add(new Label("Accessible Class :"));

        name = new HTML();
        className = new HTML();

        content.add(name);
        content.add(className);
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

    public AccessibleFieldListView getAccessibleFieldListView()
    {
        return accessibleFieldListView;
    }

    public void setAccessibleFieldListView(AccessibleFieldListView accessibleFieldListView)
    {
        this.accessibleFieldListView = accessibleFieldListView;
        content.add(accessibleFieldListView);
    }

    public void setObjectConfiguration(ObjectConfiguration objectConfiguration)
    {
        // TODO Auto-generated method stub

    }

}

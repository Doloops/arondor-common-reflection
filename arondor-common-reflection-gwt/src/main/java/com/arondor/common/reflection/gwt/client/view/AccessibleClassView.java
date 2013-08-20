package com.arondor.common.reflection.gwt.client.view;

import com.arondor.common.reflection.gwt.client.presenter.AccessibleFieldMapPresenter;
import com.arondor.common.reflection.gwt.client.presenter.SimpleAccessibleClassPresenter;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class AccessibleClassView extends Composite implements SimpleAccessibleClassPresenter.Display
{
    private HTML name;

    private HTML className;

    private AccessibleFieldMapPresenter.Display fieldMapDisplay = new AccessibleFieldMapView();

    public AccessibleClassView()
    {
        AbsolutePanel content = new AbsolutePanel();

        initWidget(content);

        content.add(new Label("Accessible Class :"));

        name = new HTML();
        className = new HTML();

        content.add(name);
        content.add(className);
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

}

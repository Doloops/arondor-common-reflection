package com.arondor.common.reflection.gwt.client.view;

import java.util.logging.Logger;

import com.arondor.common.reflection.gwt.client.presenter.AccessibleFieldPresenter;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.TextBoxBase;
import com.google.gwt.user.client.ui.Widget;

public class AccessibleFieldView extends Composite implements AccessibleFieldPresenter.Display
{
    private static final Logger LOG = Logger.getLogger(AccessibleFieldView.class.getName());

    private HTML name;

    private HTML className;

    private HTML description;

    private TextBoxBase inputValue;

    public AccessibleFieldView(FlexTable fields)
    {
        AbsolutePanel content = new AbsolutePanel();
        initWidget(content);

        name = new HTML();
        className = new HTML();
        description = new HTML();

        inputValue = new TextBox();
        inputValue.setStyleName("form-control");

        int row = fields.getRowCount();
        fields.setWidget(row, 0, name);
        fields.setWidget(row, 1, className);
        fields.setWidget(row, 2, description);
        fields.setWidget(row, 3, inputValue);

    }

    public Widget asWidget()
    {
        return this;
    }

    public void setName(String name)
    {
        this.name.setText(name);
    }

    public void setClassName(String className)
    {
        this.className.setText(className);
    }

    public void setDescription(String description)
    {
        this.description.setText(description);
    }

    public HasValue<String> getInputValue()
    {
        return inputValue;
    }

    public void setInputValue(String value)
    {
        inputValue.setValue(value);
    }
}

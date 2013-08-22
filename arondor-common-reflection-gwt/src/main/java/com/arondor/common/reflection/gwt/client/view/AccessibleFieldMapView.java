package com.arondor.common.reflection.gwt.client.view;

import java.util.HashMap;
import java.util.Map;

import com.arondor.common.reflection.gwt.client.presenter.AccessibleFieldMapPresenter;
import com.arondor.common.reflection.gwt.client.presenter.AccessibleFieldPresenter;
import com.arondor.common.reflection.model.java.AccessibleField;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class AccessibleFieldMapView extends Composite implements AccessibleFieldMapPresenter.Display
{

    private Map<AccessibleField, AccessibleFieldView> accessibleFieldViewList;

    private FlexTable fields;

    private FormPanel form;

    public AccessibleFieldMapView()
    {
        AbsolutePanel content = new AbsolutePanel();
        initWidget(content);

        form = new FormPanel();
        form.setStyleName("form-horizontal");

        content.add(new Label("Accessible Fields :"));

        fields = new FlexTable();
        fields.setStyleName("table");
        fields.addStyleName("table-striped");
        fields.addStyleName("table-bordered");

        int row = 0;
        fields.setText(row, 0, "Name");
        fields.setText(row, 1, "Classname");
        fields.setText(row, 2, "Description");
        fields.setText(row, 3, "Value");
        fields.getRowFormatter().setStyleName(0, "text-center");

        form.add(fields);
        content.add(form);
    }

    public Map<AccessibleField, AccessibleFieldView> getAccessibleFieldViewList()
    {
        return accessibleFieldViewList;
    }

    public void addAccessibleFieldView(AccessibleField accessibleField, AccessibleFieldView accessibleFieldView)
    {
        if (accessibleFieldViewList == null)
        {
            accessibleFieldViewList = new HashMap<AccessibleField, AccessibleFieldView>();
        }
        accessibleFieldViewList.put(accessibleField, accessibleFieldView);
    }

    public AccessibleFieldPresenter.Display createAccessibleFieldDisplay()
    {
        return new AccessibleFieldView(getFields());
    }

    public FlexTable getFields()
    {
        return fields;
    }

    public void clearList()
    {
        for (int row = fields.getRowCount() - 1; row > 0; row--)
        {
            fields.removeRow(row);
        }
    }

    public Widget asWidget()
    {
        return this;
    }

}

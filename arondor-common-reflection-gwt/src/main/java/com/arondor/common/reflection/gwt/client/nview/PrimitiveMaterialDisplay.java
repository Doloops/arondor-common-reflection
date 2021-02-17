package com.arondor.common.reflection.gwt.client.nview;

import com.arondor.common.reflection.gwt.client.CssBundle;
import com.arondor.common.reflection.gwt.client.presenter.TreeNodePresenter.ErrorDisplay;
import com.arondor.common.reflection.gwt.client.presenter.fields.PrimitiveTreeNodePresenter.PrimitiveDisplay;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;

import gwt.material.design.client.ui.MaterialTextBox;

public class PrimitiveMaterialDisplay implements PrimitiveDisplay, ErrorDisplay
{
    private final MaterialTextBox textBox;

    public PrimitiveMaterialDisplay(MaterialTextBox textBox)
    {
        this.textBox = textBox;
    }

    @Override
    public void setValue(String value)
    {
        textBox.setValue(value);
    }

    @Override
    public void setDefaultValue(String value)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<String> valueChangeHandler)
    {
        return textBox.addValueChangeHandler(valueChangeHandler);
    }

    @Override
    public void setPlaceholder(String value)
    {
        textBox.setPlaceholder(value);
    }

    @Override
    public void setNodeName(String name)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void setNodeDescription(String description)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void setNodeLongDescription(String longDescription)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void setActive(boolean active)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean isActive()
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void clear()
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void setIsPassword()
    {
        // TODO Auto-generated method stub

    }

    private String initialLabel;

    @Override
    public void displayError(String message)
    {
        if (initialLabel == null)
        {
            initialLabel = textBox.getLabel().getText();
        }
        // MaterialToast.fireToast(message, 2_500);
        textBox.getElement().addClassName(CssBundle.INSTANCE.css().keyError());
        textBox.setPlaceholder("Fill in the key ");
        textBox.setLabel(initialLabel + " - " + message);
    }

    @Override
    public void displayValid()
    {
        textBox.getElement().removeClassName(CssBundle.INSTANCE.css().keyError());
        if (initialLabel != null)
            textBox.setLabel(initialLabel);
        else
            initialLabel = textBox.getLabel().getText();
    }

}
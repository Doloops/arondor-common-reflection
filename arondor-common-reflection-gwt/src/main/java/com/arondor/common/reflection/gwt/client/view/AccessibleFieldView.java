package com.arondor.common.reflection.gwt.client.view;

import java.util.logging.Logger;

import com.arondor.common.reflection.gwt.client.presenter.AccessibleFieldPresenter;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FocusWidget;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.TextBoxBase;
import com.google.gwt.user.client.ui.ValueBoxBase;
import com.google.gwt.user.client.ui.Widget;

public class AccessibleFieldView extends Composite implements AccessibleFieldPresenter.Display
{
    @SuppressWarnings("unused")
    private static final Logger LOG = Logger.getLogger(AccessibleFieldView.class.getName());

    private HTML name;

    private HTML className;

    private HTML description;

    private FocusWidget inputValue;

    private final int row;

    private final FlexTable fields;

    private CheckBox configurableCheckBox = new CheckBox();

    public AccessibleFieldView(FlexTable fields)
    {
        AbsolutePanel content = new AbsolutePanel();
        initWidget(content);

        this.fields = fields;
        row = fields.getRowCount();

        name = new HTML();
        className = new HTML();
        description = new HTML();

        configurableCheckBox.addValueChangeHandler(new ValueChangeHandler<Boolean>()
        {
            @Override
            public void onValueChange(ValueChangeEvent<Boolean> event)
            {
                if (inputValue != null)
                {
                    inputValue.setEnabled(event.getValue());
                    initInput();
                }
            }
        });

        fields.setWidget(row, 0, configurableCheckBox);
        fields.setWidget(row, 1, name);
        fields.setWidget(row, 2, className);
        fields.setWidget(row, 3, description);

    }

    public void setName(String name)
    {
        this.name.setText(name);
    }

    public void setClassName(String className)
    {
        this.className.setText(className);
        initInput();
    }

    public void setDescription(String description)
    {
        this.description.setText(description);
    }

    public HasValue<Object> getInputValue()
    {
        return (HasValue<Object>) inputValue;
    }

    public void setInputValue(Object value)
    {
        String className = this.className.getHTML();
        if (className.equals("boolean"))
        {
            ((CheckBox) inputValue).setValue((Boolean) value);
        }
        else if (className.equals("long"))
        {
            ((TextBox) inputValue).setValue(value.toString());
        }
        else if (className.equals("java.lang.String"))
        {
            ((TextBoxBase) inputValue).setValue(value.toString());
        }
        else if (className.equals("java.util.List"))
        {
            ((TextArea) inputValue).setValue(value.toString());
        }

    }

    public Widget asWidget()
    {
        return this;
    }

    private void initInput()
    {
        String className = this.className.getHTML();
        if (className.equals("boolean") || className.equals("java.lang.Boolean"))
        {
            inputValue = new CheckBox();
        }
        else if (className.equals("int") || className.equals("java.lang.Integer") || className.equals("long")
                || className.equals("java.lang.Long") || className.equals("short")
                || className.equals("java.lang.Short") || className.equals("byte")
                || className.equals("java.lang.Byte"))
        {
            inputValue = new TextBox();
            inputValue.setStyleName("form-control");
            inputValue.addKeyPressHandler(new KeyPressHandler()
            {
                public void onKeyPress(KeyPressEvent event)
                {
                    switch (event.getNativeEvent().getKeyCode())
                    {
                    case KeyCodes.KEY_LEFT:
                    case KeyCodes.KEY_RIGHT:
                    case KeyCodes.KEY_BACKSPACE:
                    case KeyCodes.KEY_DELETE:
                    case KeyCodes.KEY_TAB:
                    case KeyCodes.KEY_UP:
                    case KeyCodes.KEY_DOWN:
                        return;
                    }

                    char keyCode = event.getCharCode();
                    if (!Character.isDigit(keyCode))
                    {
                        ((ValueBoxBase<String>) inputValue).cancelKey();
                    }
                }
            });

        }
        else if (className.equals("float") || className.equals("java.lang.Float") || className.equals("double")
                || className.equals("java.lang.Double"))
        {
            inputValue = new TextBox();
            inputValue.setStyleName("form-control");
            inputValue.addKeyPressHandler(new KeyPressHandler()
            {
                public void onKeyPress(KeyPressEvent event)
                {
                    switch (event.getNativeEvent().getKeyCode())
                    {
                    case KeyCodes.KEY_LEFT:
                    case KeyCodes.KEY_RIGHT:
                    case KeyCodes.KEY_BACKSPACE:
                    case KeyCodes.KEY_DELETE:
                    case KeyCodes.KEY_TAB:
                    case KeyCodes.KEY_UP:
                    case KeyCodes.KEY_DOWN:
                        return;
                    }

                    char keyCode = event.getCharCode();
                    if ((!Character.isDigit(keyCode) && keyCode != '.')
                            || (keyCode == '.' && ((TextBox) inputValue).getText().contains(".")))
                    {
                        ((ValueBoxBase<String>) inputValue).cancelKey();
                    }
                }
            });

        }
        else if (className.equals("char") || className.equals("java.lang.Character"))
        {
            inputValue = new TextBox();
            inputValue.setStyleName("form-control");
            inputValue.addKeyPressHandler(new KeyPressHandler()
            {
                public void onKeyPress(KeyPressEvent event)
                {
                    switch (event.getNativeEvent().getKeyCode())
                    {
                    case KeyCodes.KEY_LEFT:
                    case KeyCodes.KEY_RIGHT:
                    case KeyCodes.KEY_BACKSPACE:
                    case KeyCodes.KEY_DELETE:
                    case KeyCodes.KEY_TAB:
                    case KeyCodes.KEY_UP:
                    case KeyCodes.KEY_DOWN:
                        return;
                    }

                    char keyCode = event.getCharCode();
                    if (((TextBox) inputValue).getText().length() >= 1)
                    {
                        ((ValueBoxBase<String>) inputValue).cancelKey();
                    }
                }
            });

        }
        else if (className.equals("java.lang.String"))
        {
            inputValue = new TextBox();
            inputValue.setStyleName("form-control");
        }
        else if (className.equals("java.util.List"))
        {
            inputValue = new TextArea();
            inputValue.setStyleName("form-control");
        }

        if (inputValue != null)
        {
            // inputValue.setEnabled(configurableCheckBox.getValue());

            inputValue.addClickHandler(new ClickHandler()
            {

                @Override
                public void onClick(ClickEvent event)
                {
                    configurableCheckBox.setValue(true);
                }
            });
        }
        fields.setWidget(row, 4, inputValue);
    }

    public boolean getToConfig()
    {
        return configurableCheckBox.getValue();
    }
}

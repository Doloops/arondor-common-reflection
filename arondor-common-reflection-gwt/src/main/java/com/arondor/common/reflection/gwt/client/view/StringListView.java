package com.arondor.common.reflection.gwt.client.view;

import java.util.List;

import com.arondor.common.reflection.gwt.client.presenter.StringListTreeNodePresenter.StringListDisplay;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.UIObject;

public class StringListView extends AbstractTreeNodeView implements StringListDisplay
{
    private final TextArea textArea = new TextArea();

    public StringListView(UIObject parentNode)
    {
        super(parentNode);
        getContents().add(textArea);
    }

    public void clear()
    {
        textArea.setValue("");
    }

    public void setValues(List<String> values)
    {
        StringBuilder stringBuilder = new StringBuilder();
        for (String value : values)
        {
            stringBuilder.append(value);
        }
        textArea.setValue(stringBuilder.toString());
    }

}

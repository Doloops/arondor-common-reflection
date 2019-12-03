package com.arondor.common.reflection.gwt.client.nview.prim;

public class NIntView extends NStringView
{
    public NIntView()
    {
        getTextBox().getElement().setAttribute("type", "number");
    }
}

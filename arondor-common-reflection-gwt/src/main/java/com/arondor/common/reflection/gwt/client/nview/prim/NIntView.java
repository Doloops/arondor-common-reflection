package com.arondor.common.reflection.gwt.client.nview.prim;

public class NIntView extends NStringView
{
    public NIntView()
    {
        getTextBox().getElement().getElementsByTagName("input").getItem(0).setAttribute("type", "number");
    }
}

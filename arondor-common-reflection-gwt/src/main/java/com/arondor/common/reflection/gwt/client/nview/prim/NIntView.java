package com.arondor.common.reflection.gwt.client.nview.prim;

import gwt.material.design.client.constants.InputType;

public class NIntView extends NStringView
{
    public NIntView()
    {
        getTextBox().setType(InputType.NUMBER);
        getTextBox().getElement().getElementsByTagName("input").getItem(0).setAttribute("min", "0");
    }
}

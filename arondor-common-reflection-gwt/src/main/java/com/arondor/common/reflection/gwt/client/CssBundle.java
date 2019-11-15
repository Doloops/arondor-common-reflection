package com.arondor.common.reflection.gwt.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;

public interface CssBundle extends ClientBundle
{
    interface Css extends CssResource
    {
        @ClassName("classNode")
        String classNode();

        @ClassName("rootTreeNode")
        String rootTreeNode();
    }

    static final CssBundle INSTANCE = GWT.create(CssBundle.class);

    @Source("reflection.css")
    Css css();
}

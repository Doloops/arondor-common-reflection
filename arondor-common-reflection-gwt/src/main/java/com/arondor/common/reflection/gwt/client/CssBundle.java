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

        @ClassName("implementingClassView")
        String implementingClassView();

        @ClassName("nodeField")
        String nodeField();

        @ClassName("booleanField")
        String booleanField();

        @ClassName("stringField")
        String stringField();

        @ClassName("nodeName")
        String nodeName();

        @ClassName("mandatoryChildren")
        String classMandatoryChildren();

        @ClassName("optionalChildren")
        String classOptionalChildren();

        @ClassName("hideAdvancedSettings")
        String hideAdvancedSettings();

        @ClassName("advancedSettingsBtn")
        String advancedSettingsBtn();

        @ClassName("hidden")
        String hidden();

        @ClassName("resetFieldBtn")
        String resetFieldBtn();
    }

    static final CssBundle INSTANCE = GWT.create(CssBundle.class);

    @Source("reflection.css")
    Css css();
}
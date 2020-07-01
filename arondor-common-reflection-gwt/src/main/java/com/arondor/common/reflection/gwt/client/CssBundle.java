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

        @ClassName("scopeSelector")
        String scopeSelector();

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

        @ClassName("resetBtn")
        String resetBtn();

        @ClassName("helperText")
        String helperText();

        @ClassName("comboBox")
        String comboBox();

        @ClassName("mappingTable")
        String mappingTable();

        @ClassName("mappingCell")
        String mappingCell();

        @ClassName("newPairBtn")
        String newPairBtn();

        @ClassName("mappingTablePair")
        String mappingTablePair();

        @ClassName("mappingField")
        String mappingField();

        @ClassName("deleteRowBtn")
        String deleteRowBtn();

        @ClassName("contentGroup")
        String classContentGroup();

<<<<<<< HEAD
        @ClassName("keyError")
        String keyError();
=======
        @ClassName("description")
        String description();
>>>>>>> master
    }

    static final CssBundle INSTANCE = GWT.create(CssBundle.class);

    @Source("reflection.css")
    Css css();
}

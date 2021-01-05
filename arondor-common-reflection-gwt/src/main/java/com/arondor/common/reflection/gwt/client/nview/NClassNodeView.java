package com.arondor.common.reflection.gwt.client.nview;

import com.arondor.common.reflection.gwt.client.CssBundle;
import com.arondor.common.reflection.gwt.client.nview.prim.NBooleanView;
import com.arondor.common.reflection.gwt.client.nview.prim.NIntView;
import com.arondor.common.reflection.gwt.client.nview.prim.NStringListView;
import com.arondor.common.reflection.gwt.client.nview.prim.NStringView;
import com.arondor.common.reflection.gwt.client.presenter.ClassTreeNodePresenter;
import com.arondor.common.reflection.gwt.client.presenter.ImplementingClassPresenter.ImplementingClassDisplay;
import com.arondor.common.reflection.gwt.client.presenter.fields.EnumTreeNodePresenter.EnumDisplay;
import com.arondor.common.reflection.gwt.client.presenter.fields.ListTreeNodePresenter.ListRootDisplay;
import com.arondor.common.reflection.gwt.client.presenter.fields.MapTreeNodePresenter.MapRootDisplay;
import com.arondor.common.reflection.gwt.client.presenter.fields.PrimitiveTreeNodePresenter.PrimitiveDisplay;
import com.arondor.common.reflection.gwt.client.presenter.fields.StringListTreeNodePresenter.StringListDisplay;
import com.arondor.common.reflection.gwt.client.view.ImplementingClassView;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.FlowPanel;

import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialDialog;
import gwt.material.design.client.ui.MaterialDialogContent;
import gwt.material.design.client.ui.MaterialDialogFooter;
import gwt.material.design.client.ui.MaterialTextBox;
import gwt.material.design.client.ui.MaterialTitle;

public class NClassNodeView extends NNodeView implements ClassTreeNodePresenter.ClassDisplay
{
    private final ImplementingClassDisplay implementingClassView = new ImplementingClassView();

    private final FlowPanel selectGroup = new FlowPanel();

    private final FlowPanel contentGroup = new FlowPanel();

    private final FlowPanel optionsArea = new FlowPanel();

    private final FlowPanel mandatoryChildren = new FlowPanel();

    private final FlowPanel optionalChildren = new FlowPanel();

    private final FlowPanel advancedSettings = new FlowPanel();

    private final FlowPanel sharePanel = new FlowPanel();

    private final MaterialButton shareButton = new MaterialButton();

    private final MaterialDialog convertTaskDialog = new MaterialDialog();

    private final MaterialButton btnCancelConversion = new MaterialButton(), btnConvertTask = new MaterialButton();

    private final MaterialTextBox keyNameTextBox = new MaterialTextBox();

    public NClassNodeView()
    {
        getElement().addClassName(CssBundle.INSTANCE.css().classNode());

        selectGroup.getElement().addClassName("input-group");

        // implementingClassView.asWidget().getElement().addClassName("form-control");

        getResetFieldBtn().getElement().addClassName("input-group-append");
        getResetFieldBtn().getElement().addClassName(CssBundle.INSTANCE.css().resetBtn());
        getResetFieldBtn().getElement().setInnerHTML("<i></i>");

        getResetFieldBtn().addClickHandler(new ClickHandler()
        {

            @Override
            public void onClick(ClickEvent event)
            {
                implementingClassView.resetImplementingList();
                setActive(false);
                clear();
            }
        });

        convertTaskDialog.getElement().addClassName(CssBundle.INSTANCE.css().convertTaskDialog());

        MaterialDialogContent dialogContent = new MaterialDialogContent();
        dialogContent.getElement().addClassName(CssBundle.INSTANCE.css().dialogContent());

        MaterialTitle title = new MaterialTitle();
        title.setTitle("Convert a task");

        keyNameTextBox.setClass("outlined");
        keyNameTextBox.setLabel("Shared object name");
        keyNameTextBox.setFocus(true);

        dialogContent.add(title);
        dialogContent.add(keyNameTextBox);

        convertTaskDialog.add(dialogContent);

        MaterialDialogFooter dialogFooter = new MaterialDialogFooter();

        btnCancelConversion.getElement().addClassName(CssBundle.INSTANCE.css().cancelConversionBtn());
        btnCancelConversion.setText("Cancel conversion");

        btnConvertTask.getElement().addClassName(CssBundle.INSTANCE.css().doConversionBtn());
        btnConvertTask.setText("Convert to map shared object");

        dialogFooter.add(btnCancelConversion);
        dialogFooter.add(btnConvertTask);

        convertTaskDialog.add(dialogFooter);

        contentGroup.getElement().addClassName(CssBundle.INSTANCE.css().classContentGroup());
        contentGroup.getElement().addClassName("col-12");

        sharePanel.getElement().addClassName(CssBundle.INSTANCE.css().sharePanel());

        shareButton.getElement().addClassName(CssBundle.INSTANCE.css().shareButton());
        shareButton.setText("Convert to Shared Object");

        mandatoryChildren.getElement().addClassName(CssBundle.INSTANCE.css().classMandatoryChildren());
        optionalChildren.getElement().addClassName(CssBundle.INSTANCE.css().classOptionalChildren());
        advancedSettings.getElement().addClassName(CssBundle.INSTANCE.css().advancedSettingsBtn());

        String rnd = String.valueOf(Math.random()).substring(2);
        advancedSettings.getElement().setInnerHTML(
                "<a data-toggle=\"collapse\" aria-expanded=\"false\" href=\"#advancedSettings" + rnd + "\"></a>");

        advancedSettings.getElement().addClassName(CssBundle.INSTANCE.css().hideAdvancedSettings());

        optionalChildren.getElement().setId("advancedSettings" + rnd);
        optionalChildren.getElement().addClassName("collapse");

        bind();
    }

    protected FlowPanel getSelectGroup()
    {
        return selectGroup;
    }

    protected void bind()
    {
        selectGroup.add(implementingClassView);
        selectGroup.add(getResetFieldBtn());

        add(selectGroup);

        add(convertTaskDialog);

        sharePanel.add(shareButton);

        contentGroup.add(sharePanel);
        contentGroup.add(mandatoryChildren);

        optionsArea.add(advancedSettings);
        optionsArea.add(optionalChildren);

        contentGroup.add(optionsArea);

        add(contentGroup);
    }

    protected FlowPanel getOptionsArea()
    {
        return optionsArea;
    }

    /**
     * @return name fill in the pop-up
     */
    @Override
    public String getKeyName()
    {
        return keyNameTextBox.getText();
    }

    /*
     * Clear pop-up name field 
     */
    @Override
    public void clearKeyName()
    {
        keyNameTextBox.setText("");
    }

    @Override
    public MaterialDialog getConvertTaskDialog()
    {
        return convertTaskDialog;
    }

    /**
     * Remove the share button from a shared object display
     */
    @Override
    public void removeSharedButton()
    {
        sharePanel.remove(shareButton);
    }

    private void addChildView(boolean isMandatory, NNodeView childView)
    {
        if (isMandatory)
        {
            mandatoryChildren.add(childView);
        }
        else
        {
            advancedSettings.getElement().removeClassName(CssBundle.INSTANCE.css().hideAdvancedSettings());
            optionalChildren.add(childView);
        }
    }

    @Override
    public ImplementingClassDisplay getImplementingClassDisplay()
    {
        return implementingClassView;
    }

    @Override
    public ClassTreeNodePresenter.ClassDisplay createClassChild(boolean isMandatory)
    {
        NClassNodeView childView = new NClassNodeView();
        if (isMandatory)
        {
            childView.disableReset();
        }
        addChildView(isMandatory, childView);
        return childView;
    }

    @Override
    public PrimitiveDisplay createPrimitiveChild(String fieldClassName, boolean isMandatory)
    {
        NNodeView view;
        if (fieldClassName.equals("boolean"))
        {
            view = new NBooleanView();
        }
        else if (fieldClassName.equals("int"))
        {
            view = new NIntView();
        }
        else if (fieldClassName.equals("java.lang.String"))
        {
            view = new NStringView();
        }
        // TODO long
        // TODO java.util.Map
        // TODO java.util.List
        // TODO java.lang.Class
        // TODO java.io.InputStream
        // TODO java.math.BigInteger

        // TODO com.arondor.fast2p8.model.manager.Manager
        // TODO com.arondor.fast2p8.alfresco.PropertyHelper
        // TODO com.arondor.fast2p8.alfresco.AlfrescoConnection
        // TODO com.arondor.fast2p8.model.namingscheme.NamingScheme
        // TODO com.arondor.fast2p8.alfresco.AlfrescoCMISConnectionProvider
        // TODO com.arondor.fast2p8.model.namingscheme.PunnetPatternResolver
        // TODO org.apache.chemistry.opencmis.client.api.Session
        else
        {
            view = new NStringView();
        }

        addChildView(isMandatory, view);
        return (PrimitiveDisplay) view;
    }

    @Override
    public EnumDisplay createEnumListChild(boolean isMandatory)
    {
        return null;
    }

    @Override
    public StringListDisplay createStringListChild(boolean isMandatory)
    {
        NStringListView view = new NStringListView();
        addChildView(isMandatory, view);
        return view;
    }

    @Override
    public MapRootDisplay createMapChild(boolean isMandatory)
    {
        NMapNodeView mapView = new NMapNodeView();
        addChildView(isMandatory, mapView);
        return mapView;
    }

    @Override
    public ListRootDisplay createListChild(boolean isMandatory)
    {
        return null;
    }

    @Override
    public void clear()
    {
        super.clear();
        mandatoryChildren.clear();
        optionalChildren.clear();
        optionalChildren.getElement().removeClassName("show");
        bind();
    }

    @Override
    public void setNodeDescription(String description)
    {
        implementingClassView.setNodeDescription(description);
    }

    /**
     * Add the click handler on share button
     */
    @Override
    public HandlerRegistration onShare(ClickHandler handler)
    {
        return shareButton.addClickHandler(handler);
    }

    /**
     * Add the click handler on the pop-up cancel sharing button
     */
    @Override
    public HandlerRegistration onCancelShare(ClickHandler handler)
    {
        return btnCancelConversion.addClickHandler(handler);
    }

    /**
     * Add the click handler on the pop-up share button
     */
    @Override
    public HandlerRegistration onDoShare(ClickHandler handler)
    {
        return btnConvertTask.addClickHandler(handler);
    }

}

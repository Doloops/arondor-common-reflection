package com.arondor.common.reflection.gwt.client.nview;

import com.arondor.common.reflection.gwt.client.CssBundle;
import com.arondor.common.reflection.gwt.client.nview.prim.NBooleanView;
import com.arondor.common.reflection.gwt.client.nview.prim.NIntView;
import com.arondor.common.reflection.gwt.client.nview.prim.NScriptView;
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
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.RootPanel;

import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialDialog;
import gwt.material.design.client.ui.MaterialTextBox;
import gwt.material.design.client.ui.MaterialTitle;

public class NClassNodeView extends NNodeView implements ClassTreeNodePresenter.ClassDisplay
{
    protected final ImplementingClassDisplay implementingClassView = new ImplementingClassView();

    private final FlowPanel selectGroup = new FlowPanel();

    private final FlowPanel contentGroup = new FlowPanel();

    private final FlowPanel optionsArea = new FlowPanel();

    private final FlowPanel mandatoryChildren = new FlowPanel();

    private final FlowPanel optionalChildren = new FlowPanel();

    private final FlowPanel advancedSettings = new FlowPanel();

    private final MaterialDialog convertTaskDialog = new MaterialDialog();

    private final MaterialButton btnCancelConversion = new MaterialButton(), btnConvertTask = new MaterialButton();

    private final MaterialTextBox keyNameTextBox = new MaterialTextBox();

    private static final String ALLOWED_FOR_NAME = "^[a-zA-Z0-9-_]+$";

    public NClassNodeView()
    {
        getElement().addClassName(CssBundle.INSTANCE.css().classNode());

        selectGroup.getElement().addClassName("input-group");
        selectGroup.add(implementingClassView.getSharedObjectCreatePanel());
        selectGroup.add(implementingClassView.getSharedObjectForwardPanel());

        getResetFieldBtn().getElement().addClassName("input-group-append");
        getResetFieldBtn().getElement().addClassName(CssBundle.INSTANCE.css().resetBtn());
        getResetFieldBtn().getElement().setInnerHTML("<i></i>");

        getResetFieldBtn().addClickHandler(new ClickHandler()
        {
            @Override
            public void onClick(ClickEvent event)
            {
                implementingClassView.reset();
                setActive(false);
                clear();
            }
        });

        buildConvertTaskDialog();

        contentGroup.getElement().addClassName(CssBundle.INSTANCE.css().classContentGroup());
        contentGroup.getElement().addClassName("col-12");

        mandatoryChildren.getElement().addClassName(CssBundle.INSTANCE.css().classMandatoryChildren());
        optionalChildren.getElement().addClassName(CssBundle.INSTANCE.css().classOptionalChildren());

        advancedSettings.getElement().addClassName(CssBundle.INSTANCE.css().advancedSettingsBtn());

        String rnd = String.valueOf(Math.random()).substring(2);
        advancedSettings.getElement()
                .setInnerHTML("<a data-toggle=\"collapse\" aria-expanded=\"false\" href=\"#advancedSettings" + rnd
                        + "\" class=\"collapsed\"></a>");

        advancedSettings.getElement().addClassName(CssBundle.INSTANCE.css().hideAdvancedSettings());

        optionalChildren.getElement().setId("advancedSettings" + rnd);
        optionalChildren.getElement().addClassName("collapse");

        bind();
    }

    private void buildConvertTaskDialog()
    {
        convertTaskDialog.getElement().addClassName(CssBundle.INSTANCE.css().convertTaskDialog());
        convertTaskDialog.getElement().addClassName("popupDialog");

        MaterialTitle title = new MaterialTitle();
        title.setTitle("Convert to shared object");
        title.setMarginTop(20);
        title.setMarginLeft(10);

        keyNameTextBox.setClass("outlined");
        keyNameTextBox.setMargin(10);
        keyNameTextBox.setLabel("Name");

        btnCancelConversion.getElement().addClassName("dismissPopup");

        // prevent bootstrap overstyle
        btnCancelConversion.getElement().removeClassName("btn");
        btnCancelConversion.setIconType(IconType.CANCEL);

        btnConvertTask.getElement().addClassName(CssBundle.INSTANCE.css().doConversionBtn());
        btnConvertTask.setText("Convert to map shared object");
        btnConvertTask.setEnabled(false);

        convertTaskDialog.add(btnCancelConversion);
        convertTaskDialog.add(title);
        convertTaskDialog.add(keyNameTextBox);
        convertTaskDialog.add(btnConvertTask);
    }

    protected FlowPanel getSelectGroup()
    {
        return selectGroup;
    }

    protected void bind()
    {
        RootPanel.get().add(convertTaskDialog);

        convertTaskDialog.addOpenHandler(new OpenHandler<MaterialDialog>()
        {
            @Override
            public void onOpen(OpenEvent<MaterialDialog> event)
            {
                keyNameTextBox.setFocus(true);
            }
        });

        keyNameTextBox.addKeyUpHandler(new KeyUpHandler()
        {
            @Override
            public void onKeyUp(KeyUpEvent event)
            {
                String newName = keyNameTextBox.getText();
                boolean nameIsAuthorized;

                if (!RegExp.compile(ALLOWED_FOR_NAME).test(newName))
                {
                    keyNameTextBox.setErrorText("Allowed : a-z, A-Z, 0-9, -, _");
                    nameIsAuthorized = false;
                }
                else
                {
                    keyNameTextBox.clearErrorText();
                    nameIsAuthorized = true;
                }
                btnConvertTask.setEnabled(!newName.isEmpty() && nameIsAuthorized);
            }
        });

        selectGroup.add(implementingClassView);
        selectGroup.add(getResetFieldBtn());
        add(selectGroup);

        optionsArea.add(advancedSettings);
        optionsArea.add(optionalChildren);
        contentGroup.add(mandatoryChildren);
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
        childView.enableReset(!isMandatory);
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
        // else if (fieldClassName.equals("java.util.List"))
        // {
        // view = new NListView();
        // }
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
    public PrimitiveDisplay createScriptChild(String scriptType, boolean isMandatory)
    {
        NScriptView view = new NScriptView();
        addChildView(isMandatory, view);
        return view;
    }

    @Override
    public EnumDisplay createEnumListChild(boolean isMandatory)
    {
        NEnumView view = new NEnumView();
        addChildView(isMandatory, view);
        return view;
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
        NListView listView = new NListView();
        addChildView(isMandatory, listView);
        return listView;
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
        return implementingClassView.getSharedObjectCreatePanel().addClickHandler(handler);
    }

    /**
     * Add the click handler
     */
    @Override
    public HandlerRegistration forwardToSharedObject(ClickHandler handler)
    {
        return implementingClassView.getSharedObjectForwardPanel().addClickHandler(handler);
    }

    /**
     * Add the click handler on the pop-up cancel sharing button
     */
    @Override
    public HandlerRegistration onCancelShare(ClickHandler handler)
    {
        return btnCancelConversion.addClickHandler(handler);
    }

    @Override
    public HandlerRegistration onReset(ClickHandler handler)
    {
        return getResetFieldBtn().addClickHandler(handler);
    }

    /**
     * Add the click handler on the pop-up share button
     */
    @Override
    public HandlerRegistration onDoShare(ClickHandler handler)
    {
        return btnConvertTask.addClickHandler(handler);
    }

    @Override
    public void setActive(boolean active)
    {
        super.setActive(active);
        if (active)
        {
            advancedSettings.getElement().removeClassName(CssBundle.INSTANCE.css().hideAdvancedSettings());
            getElement().addClassName(CssBundle.INSTANCE.css().active());
        }
        else
        {
            getElement().removeClassName(CssBundle.INSTANCE.css().active());
            advancedSettings.getElement().getElementsByTagName("a").getItem(0).setClassName("collapsed");
            advancedSettings.getElement().addClassName(CssBundle.INSTANCE.css().hideAdvancedSettings());
        }
    }

}

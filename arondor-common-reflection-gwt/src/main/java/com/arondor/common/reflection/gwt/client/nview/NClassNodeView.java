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
import com.google.gwt.user.client.ui.FlowPanel;

public class NClassNodeView extends NNodeView implements ClassTreeNodePresenter.ClassDisplay
{
    private final ImplementingClassDisplay implementingClassView = new ImplementingClassView();

    private final FlowPanel selectGroup = new FlowPanel();

    private final FlowPanel mandatoryChildren = new FlowPanel();

    private final FlowPanel optionalChildren = new FlowPanel();

    private final FlowPanel advancedSettings = new FlowPanel();

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
                implementingClassView.resetComboBox();
                setActive(false);
                clear();
            }
        });

        mandatoryChildren.getElement().addClassName(CssBundle.INSTANCE.css().classMandatoryChildren());
        optionalChildren.getElement().addClassName(CssBundle.INSTANCE.css().classOptionalChildren());
        advancedSettings.getElement().addClassName(CssBundle.INSTANCE.css().advancedSettingsBtn());

        String rnd = String.valueOf(Math.random()).substring(2);
        advancedSettings.getElement().setInnerHTML(
                "<a data-toggle=\"collapse\"  href=\"#advancedSettings" + rnd + "\">> Advanced settings</a>");
        optionalChildren.getElement().setId("advancedSettings" + rnd);

        optionalChildren.getElement().addClassName("collapse");

        // implementingClassView.setProperLabel("blabla");

        bind();
    }

    private void bind()
    {
        selectGroup.add(implementingClassView);
        selectGroup.add(getResetFieldBtn());

        add(selectGroup);
        add(mandatoryChildren);
        add(advancedSettings);
        add(optionalChildren);

        advancedSettings.getElement().addClassName(CssBundle.INSTANCE.css().hideAdvancedSettings());

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
        // TODO Auto-generated method stub
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
        // TODO Auto-generated method stub
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

}

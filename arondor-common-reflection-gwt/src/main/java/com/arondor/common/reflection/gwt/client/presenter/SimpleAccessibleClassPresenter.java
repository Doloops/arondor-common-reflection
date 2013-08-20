package com.arondor.common.reflection.gwt.client.presenter;

import java.util.HashMap;
import java.util.logging.Logger;

import com.arondor.common.reflection.bean.config.ObjectConfigurationFactoryBean;
import com.arondor.common.reflection.gwt.client.service.AccessibleClassServiceAsync;
import com.arondor.common.reflection.gwt.client.view.AccessibleFieldListView;
import com.arondor.common.reflection.gwt.client.view.AccessibleFieldView;
import com.arondor.common.reflection.model.config.ElementConfiguration;
import com.arondor.common.reflection.model.config.ObjectConfiguration;
import com.arondor.common.reflection.model.config.ObjectConfigurationFactory;
import com.arondor.common.reflection.model.java.AccessibleClass;
import com.arondor.common.reflection.model.java.AccessibleField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;

public class SimpleAccessibleClassPresenter implements AccessibleClassPresenter
{
    private static final Logger LOG = Logger.getLogger(SimpleAccessibleClassPresenter.class.getName());

    private AccessibleClass accessibleClass;

    private ObjectConfiguration objectConfiguration;

    public interface Display extends IsWidget
    {
        void setName(String name);

        void setClassname(String classname);

        void setObjectConfiguration(ObjectConfiguration objectConfiguration);

        AccessibleFieldListView getAccessibleFieldListView();

        void setAccessibleFieldListView(AccessibleFieldListView accessibleFieldView);
    }

    private final AccessibleClassServiceAsync rpcService;

    private final Display display;

    private AccessibleFieldListPresenter fieldListPresenter;

    public SimpleAccessibleClassPresenter(AccessibleClassServiceAsync rpcService, Display view)
    {
        this.rpcService = rpcService;
        this.display = view;

        bind();
        // RootPanel.get().add(display.asWidget());

        fieldListPresenter = new AccessibleFieldListPresenter(new AccessibleFieldListView());
        display.setAccessibleFieldListView((AccessibleFieldListView) fieldListPresenter.getDisplay());
        display.setObjectConfiguration(objectConfiguration);
    }

    public void bind()
    {
    }

    public Display getDisplay()
    {
        return display;
    }

    protected void setAccessibleClass(AccessibleClass accessibleClass)
    {
        this.accessibleClass = accessibleClass;
    }

    public void setObjectClassName(String className)
    {
        fetchAccessibleClass(className);
    }

    private void fetchAccessibleClass(String className)
    {
        rpcService.getAccessibleClass(className, new AsyncCallback<AccessibleClass>()
        {

            public void onSuccess(AccessibleClass result)
            {
                setAccessibleClass(result);
                accessibleClass = result;
                display.setName(result.getName());
                display.setClassname(result.getClassBaseName());
                fieldListPresenter.setAccessibleFieldList(result.getAccessibleFields());
            }

            public void onFailure(Throwable caught)
            {
                Window.alert("Error fetching Accessible Classes");
            }
        });
    }

    public void doSave()
    {

        ObjectConfigurationFactory objectConfigurationFactory = new ObjectConfigurationFactoryBean();

        ObjectConfiguration configuration = objectConfigurationFactory.createObjectConfiguration();
        configuration.setClassName(accessibleClass.getName());

        configuration.setFields(new HashMap<String, ElementConfiguration>());

        for (AccessibleField accessibleField : accessibleClass.getAccessibleFields().values())
        {
            AccessibleFieldView accessibleFieldView = display.getAccessibleFieldListView().getAccessibleFieldViewList()
                    .get(accessibleField);
            String value = accessibleFieldView.getInputValue().getValue();
            configuration.getFields().put(accessibleField.getName(),
                    objectConfigurationFactory.createPrimitiveConfiguration(value));
            LOG.finest(accessibleField.getName() + " : " + value);
        }

        setObjectConfiguration(configuration);
    }

    public ObjectConfiguration getObjectConfiguration()
    {
        return objectConfiguration;
    }

    public void setObjectConfiguration(ObjectConfiguration objectConfiguration)
    {
        this.objectConfiguration = objectConfiguration;
        for (int i = display.getAccessibleFieldListView().getFields().getRowCount() - 1; i > 0; i--)
        {
            display.getAccessibleFieldListView().getFields().removeRow(i);
        }
        fetchAccessibleClass(objectConfiguration.getClassName());
        fieldListPresenter.setObjectConfiguration(objectConfiguration);
    }

    public IsWidget getDisplayWidget()
    {
        return getDisplay();
    }

}

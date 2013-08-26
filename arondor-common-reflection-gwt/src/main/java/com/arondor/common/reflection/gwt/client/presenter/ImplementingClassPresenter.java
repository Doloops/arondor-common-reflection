package com.arondor.common.reflection.gwt.client.presenter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

import com.arondor.common.reflection.gwt.client.service.GWTReflectionServiceAsync;
import com.arondor.common.reflection.gwt.client.view.MyValueChangeEvent;
import com.arondor.common.reflection.model.java.AccessibleClass;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;

public class ImplementingClassPresenter
{
    private static final Logger LOG = Logger.getLogger(ImplementingClassPresenter.class.getName());

    public interface Display extends IsWidget
    {
        void setBaseClassName(String baseClassName);

        void setImplementingClasses(Collection<String> implementingClasses);

        void selectImplementingClass(String implementingClassName);

        HandlerRegistration addValueChangeHandler(ValueChangeHandler<String> valueChangeHandler);
    }

    private final String baseClassName;

    private String implementClassName;

    private final Display display;

    private final GWTReflectionServiceAsync rpcService;

    public static final String NULL_VALUE = "null";

    public ImplementingClassPresenter(GWTReflectionServiceAsync rpcService, String baseClassName, Display display)
    {
        this.baseClassName = baseClassName;
        this.display = display;
        this.rpcService = rpcService;
        this.display.setBaseClassName(baseClassName);
        bind();

        fetchBaseClass();
    }

    public HandlerRegistration addValueChangeHandler(final ValueChangeHandler<String> valueChangeHandler)
    {
        return this.display.addValueChangeHandler(new ValueChangeHandler<String>()
        {
            public void onValueChange(ValueChangeEvent<String> event)
            {
                String implementClassName = event.getValue();
                if (implementClassName.equals(NULL_VALUE))
                {
                    implementClassName = null;
                }
                LOG.finest("Changed implementClassName to " + implementClassName);
                valueChangeHandler.onValueChange(new MyValueChangeEvent<String>(implementClassName));
            }
        });
    }

    private void fetchBaseClass()
    {
        final List<String> implementingClasses = new ArrayList<String>();
        implementingClasses.add(NULL_VALUE);
        rpcService.getAccessibleClass(baseClassName, new AsyncCallback<AccessibleClass>()
        {
            public void onFailure(Throwable caught)
            {
            }

            public void onSuccess(AccessibleClass result)
            {
                if (isInstantiatable(result))
                {
                    implementingClasses.add(result.getName());
                }
                fetchImplementations(implementingClasses);
            }
        });
    }

    protected boolean isInstantiatable(AccessibleClass result)
    {
        return result.getSuperclass() != null;
    }

    private void fetchImplementations(final List<String> implementingClasses)
    {
        rpcService.getImplementingAccessibleClasses(baseClassName, new AsyncCallback<Collection<AccessibleClass>>()
        {
            public void onSuccess(Collection<AccessibleClass> result)
            {
                for (AccessibleClass classes : result)
                {
                    implementingClasses.add(classes.getName());
                }
                java.util.Collections.sort(implementingClasses, new ClassNameComparator());
                display.setImplementingClasses(implementingClasses);
                LOG.finest("Set implementing classes (" + implementingClasses.size() + " items), implement="
                        + implementClassName);
                Scheduler.get().scheduleDeferred(new ScheduledCommand()
                {
                    public void execute()
                    {
                        if (implementClassName != null)
                        {
                            display.selectImplementingClass(implementClassName);
                        }
                    }
                });
            }

            public void onFailure(Throwable caught)
            {
            }
        });
    }

    private void bind()
    {
        display.addValueChangeHandler(new ValueChangeHandler<String>()
        {
            public void onValueChange(ValueChangeEvent<String> event)
            {
                implementClassName = event.getValue();
                if (implementClassName.equals(NULL_VALUE))
                {
                    implementClassName = null;
                }
                LOG.finest("Changed implementClassName=" + implementClassName);
            }
        });
    }

    public String getBaseClassName()
    {
        return baseClassName;
    }

    public String getImplementClassName()
    {
        return implementClassName;
    }

    public void setImplementClassName(final String implementClassName)
    {
        this.implementClassName = implementClassName;
        LOG.finest("setImplementClassName(" + implementClassName + ")");
        display.selectImplementingClass(implementClassName);
    }
}

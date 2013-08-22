package com.arondor.common.reflection.gwt.client.presenter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.arondor.common.reflection.gwt.client.service.GWTReflectionServiceAsync;
import com.arondor.common.reflection.model.java.AccessibleClass;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;

public class ImplementingClassPresenter
{
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

    private final String NULL_VALUE = "null";

    public ImplementingClassPresenter(GWTReflectionServiceAsync rpcService, String baseClassName, Display display)
    {
        this.baseClassName = baseClassName;
        this.display = display;
        this.rpcService = rpcService;
        this.display.setBaseClassName(baseClassName);
        bind();

        fetchBaseClass(null);
    }

    private static class AccessibleClassChangeEvent extends ValueChangeEvent<AccessibleClass>
    {
        protected AccessibleClassChangeEvent(AccessibleClass value)
        {
            super(value);
        }
    }

    public HandlerRegistration addValueChangeHandler(final ValueChangeHandler<AccessibleClass> valueChangeHandler)
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
                if (implementClassName != null)
                {
                    changeAccessibleClass(valueChangeHandler, implementClassName);
                }
                else
                {
                    valueChangeHandler.onValueChange(new AccessibleClassChangeEvent(null));
                }
            }

        });
    }

    private void changeAccessibleClass(final ValueChangeHandler<AccessibleClass> valueChangeHandler,
            String implementClassName)
    {
        rpcService.getAccessibleClass(implementClassName, new AsyncCallback<AccessibleClass>()
        {
            public void onFailure(Throwable caught)
            {
            }

            public void onSuccess(AccessibleClass result)
            {
                valueChangeHandler.onValueChange(new AccessibleClassChangeEvent(result));
            }
        });
    }

    private void fetchBaseClass(final Runnable afterFetch)
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
                fetchImplementations(implementingClasses, afterFetch);
            }
        });
    }

    protected boolean isInstantiatable(AccessibleClass result)
    {
        return result.getSuperclass() != null;
    }

    private void fetchImplementations(final List<String> implementingClasses, final Runnable afterFetch)
    {
        rpcService.getImplementingAccessibleClasses(baseClassName, new AsyncCallback<Collection<AccessibleClass>>()
        {
            public void onSuccess(Collection<AccessibleClass> result)
            {
                for (AccessibleClass classes : result)
                {
                    implementingClasses.add(classes.getName());
                }
                display.setImplementingClasses(implementingClasses);

                if (afterFetch != null)
                {
                    afterFetch.run();
                }
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

    public void setImplementClassName(final String implementClassName, final AsyncCallback<AccessibleClass> callback)
    {
        this.implementClassName = implementClassName;

        final List<HandlerRegistration> handlers = new ArrayList<HandlerRegistration>();
        fetchBaseClass(new Runnable()
        {
            public void run()
            {
                if (callback != null)
                {
                    handlers.add(addValueChangeHandler(new ValueChangeHandler<AccessibleClass>()
                    {
                        public void onValueChange(ValueChangeEvent<AccessibleClass> event)
                        {
                            callback.onSuccess(event.getValue());
                            for (HandlerRegistration handler : handlers)
                            {
                                handler.removeHandler();
                            }
                        }
                    }));
                }
                display.selectImplementingClass(implementClassName);
            }
        });
    }
}

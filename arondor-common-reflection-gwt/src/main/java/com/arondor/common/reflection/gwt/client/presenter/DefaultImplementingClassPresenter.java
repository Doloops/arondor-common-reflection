package com.arondor.common.reflection.gwt.client.presenter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.arondor.common.reflection.gwt.client.event.MyValueChangeEvent;
import com.arondor.common.reflection.gwt.client.service.GWTReflectionServiceAsync;
import com.arondor.common.reflection.model.config.ObjectConfiguration;
import com.arondor.common.reflection.model.config.ObjectConfigurationMap;
import com.arondor.common.reflection.model.java.AccessibleClass;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.RepeatingCommand;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class DefaultImplementingClassPresenter implements ImplementingClassPresenter
{
    private static final Logger LOG = Logger.getLogger(ImplementingClassPresenter.class.getName());

    private final String baseClassName;

    private ImplementingClass currentImplementingClass = ImplementingClass.NULL_CLASS;

    private final ImplementingClassDisplay display;

    private final GWTReflectionServiceAsync rpcService;

    private final boolean isMandatory;

    private final List<ImplementingClass> implementingClasses = new ArrayList<ImplementingClass>();

    public DefaultImplementingClassPresenter(GWTReflectionServiceAsync rpcService,
            ObjectConfigurationMap objectConfigurationMap, String baseClassName, boolean isMandatory,
            ImplementingClassDisplay display)
    {
        this.baseClassName = baseClassName;
        this.isMandatory = isMandatory;
        this.display = display;
        this.rpcService = rpcService;
        this.display.setBaseClassName(baseClassName);
        bind();

        // if (!isMandatory)
        // {
        // addImplementingClass(ImplementingClass.NULL_CLASS);
        // }

        fetchBaseClass();
        fetchImplementations();
        if (objectConfigurationMap != null)
        {
            fetchObjectConfigurations(objectConfigurationMap);
        }
    }

    private void fetchObjectConfigurations(ObjectConfigurationMap objectConfigurationMap)
    {
        for (Map.Entry<String, ObjectConfiguration> entry : objectConfigurationMap.entrySet())
        {
            final String referenceName = entry.getKey();
            final String referenceClassName = entry.getValue().getClassName();
            final ImplementingClass implementingClass = new ImplementingClass(true, null);

            if (referenceClassName.equals(baseClassName))
            {
                addImplementingClass(implementingClass);
            }
            else
            {
                rpcService.getAccessibleClass(referenceClassName, new AsyncCallback<AccessibleClass>()
                {
                    @Override
                    public void onFailure(Throwable caught)
                    {
                    }

                    @Override
                    public void onSuccess(AccessibleClass result)
                    {
                        for (String interfaceName : result.getAllInterfaces())
                        {
                            if (interfaceName.equals(baseClassName))
                            {
                                addImplementingClass(implementingClass);
                            }
                        }
                    }
                });
            }
        }
    }

    private final List<ValueChangeHandler<ImplementingClass>> valueChangeHandlerRegistrations = new ArrayList<ValueChangeHandler<ImplementingClass>>();

    @Override
    public HandlerRegistration addValueChangeHandler(final ValueChangeHandler<ImplementingClass> valueChangeHandler)
    {
        valueChangeHandlerRegistrations.add(valueChangeHandler);
        final HandlerRegistration registration = this.display
                .addValueChangeHandler(new ValueChangeHandler<ImplementingClass>()
                {
                    @Override
                    public void onValueChange(ValueChangeEvent<ImplementingClass> event)
                    {
                        valueChangeHandler.onValueChange(event);
                    }
                });

        return new HandlerRegistration()
        {
            @Override
            public void removeHandler()
            {
                registration.removeHandler();
                valueChangeHandlerRegistrations.remove(valueChangeHandler);
            }
        };

    }

    private void bind()
    {
        display.addValueChangeHandler(new ValueChangeHandler<ImplementingClass>()
        {
            @Override
            public void onValueChange(ValueChangeEvent<ImplementingClass> event)
            {
                currentImplementingClass = event.getValue();
                LOG.finest("Changed implementClassName=" + currentImplementingClass);
            }
        });
    }

    private void addImplementingClass(ImplementingClass implementingClass)
    {
        if (implementingClasses.contains(implementingClass))
        {
            return;
        }
        implementingClasses.add(implementingClass);

        updateDisplay();
    }

    private boolean updateDisplayScheduled = false;

    private void updateDisplay()
    {
        if (updateDisplayScheduled)
        {
            return;
        }
        updateDisplayScheduled = true;

        if (GWT.isClient())
        {
            Scheduler.get().scheduleFixedDelay(new RepeatingCommand()
            {

                @Override
                public boolean execute()
                {
                    doUpdateDisplay();
                    return false;
                }
            }, 20);
        }
        else
        {
            doUpdateDisplay();
        }
    }

    private void doUpdateDisplay()
    {
        Collections.sort(implementingClasses);

        display.setImplementingClasses(implementingClasses);

        LOG.finest("currentImplementingClass=" + currentImplementingClass);

        if (implementingClasses.contains(currentImplementingClass))
        {
            display.selectImplementingClass(currentImplementingClass);
        }

        updateDisplayScheduled = false;
    }

    private void fetchBaseClass()
    {
        rpcService.getAccessibleClass(baseClassName, new AsyncCallback<AccessibleClass>()
        {
            @Override
            public void onFailure(Throwable caught)
            {
            }

            @Override
            public void onSuccess(AccessibleClass result)
            {
                if (isInstantiatable(result))
                {
                    addImplementingClass(new ImplementingClass(false, result));
                }
            }
        });
    }

    protected boolean isInstantiatable(AccessibleClass result)
    {
        return result != null && result.getSuperclass() != null && !result.isAbstract();
    }

    private void fetchImplementations()
    {
        rpcService.getImplementingAccessibleClasses(baseClassName, new AsyncCallback<Collection<AccessibleClass>>()
        {
            @Override
            public void onSuccess(Collection<AccessibleClass> result)
            {
                for (AccessibleClass clazz : result)
                {
                    if (isInstantiatable(clazz))
                    {
                        addImplementingClass(new ImplementingClass(false, clazz));
                    }
                }
                LOG.info("fetchImplementations() : Base class " + baseClassName + ", results=" + result.size()
                        + ", current=" + currentImplementingClass);
                if (result.size() == 1 && isMandatory && currentImplementingClass == ImplementingClass.NULL_CLASS)
                {
                    /**
                     * There is only one result.
                     */
                    AccessibleClass firstClass = result.iterator().next();
                    LOG.info("Selecting default class :" + firstClass.getName() + " as implementation of "
                            + baseClassName);
                    ImplementingClass implementingClass = new ImplementingClass(false, firstClass);
                    setImplementingClass(implementingClass);

                    ValueChangeEvent<ImplementingClass> event = new MyValueChangeEvent<ImplementingClass>(
                            implementingClass);
                    for (ValueChangeHandler<ImplementingClass> handler : valueChangeHandlerRegistrations)
                    {
                        handler.onValueChange(event);
                    }
                }
            }

            @Override
            public void onFailure(Throwable caught)
            {
            }
        });
    }

    @Override
    public String getBaseClassName()
    {
        return baseClassName;
    }

    @Override
    public void setImplementingClass(ImplementingClass implementingClass)
    {
        this.currentImplementingClass = implementingClass;
        display.selectImplementingClass(currentImplementingClass);
    }

    @Override
    public ImplementingClass getImplementingClass()
    {
        return currentImplementingClass;
    }

}

package com.arondor.common.reflection.gwt.client.presenter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import com.arondor.common.reflection.gwt.client.event.MyValueChangeEvent;
import com.arondor.common.reflection.gwt.client.service.GWTReflectionServiceAsync;
import com.arondor.common.reflection.model.java.AccessibleClass;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.RepeatingCommand;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Command;
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

    private final ObjectReferencesProvider objectReferencesProvider;

    public DefaultImplementingClassPresenter(GWTReflectionServiceAsync rpcService,
            ObjectReferencesProvider objectReferencesProvider, String baseClassName, boolean isMandatory,
            ImplementingClassDisplay display)
    {
        this.baseClassName = baseClassName;
        this.isMandatory = isMandatory;
        this.display = display;
        this.rpcService = rpcService;
        this.objectReferencesProvider = objectReferencesProvider;
        bind();

        // if (!isMandatory)
        // {
        // addImplementingClass(ImplementingClass.NULL_CLASS);
        // }

        fetchBaseClass();
        populateObjectReferences();

        display.onOpenImplementingClasses(new Command()
        {
            @Override
            public void execute()
            {
                LOG.info("onOpenImplementingClasses()");
                populateObjectReferences();
            }
        });
    }

    private void populateObjectReferences()
    {
        if (objectReferencesProvider == null)
            return;
        objectReferencesProvider.provide(new AsyncCallback<Collection<ImplementingClass>>()
        {
            @Override
            public void onFailure(Throwable caught)
            {
                LOG.severe("Could not provide object references ! " + caught.getMessage());
            }

            @Override
            public void onSuccess(Collection<ImplementingClass> result)
            {
                removeImplementClassReferences();

                for (ImplementingClass implementingClass : result)
                {
                    final String referenceClassName = implementingClass.getClassName();

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
                display.setImplementingClasses(implementingClasses);
            }
        });
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

    private void removeImplementClassReferences()
    {
        /**
         * Cleanup all references
         */
        for (Iterator<ImplementingClass> iterator = implementingClasses.iterator(); iterator.hasNext();)
        {
            if (iterator.next().isReference())
                iterator.remove();
        }
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
            }, 50);
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

        if (currentImplementingClass != ImplementingClass.NULL_CLASS
                && implementingClasses.contains(currentImplementingClass))
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
                    addImplementingClass(new ImplementingClass(result));
                }
                fetchImplementations();
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
                        addImplementingClass(new ImplementingClass(clazz));
                    }
                }
                LOG.info("fetchImplementations() : Base class " + baseClassName + ", results=" + result.size()
                        + ", current=" + currentImplementingClass);
                
                if (getBaseClassName().equals("com.arondor.fast2p8.model.taskflow.design.TaskLinkCondition"))
                {
                    display.getSharedObjectCreatePanel().removeFromParent();
                    display.getSharedObjectForwardPanel().removeFromParent();
                }
                
                if (result.size() == 1 && isMandatory && currentImplementingClass == null)
                {
                    /**
                     * There is only one result.
                     */
                    AccessibleClass firstClass = result.iterator().next();
                    LOG.info("Selecting default class :" + firstClass.getName() + " as implementation of "
                            + baseClassName);
                    ImplementingClass implementingClass = new ImplementingClass(firstClass);
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

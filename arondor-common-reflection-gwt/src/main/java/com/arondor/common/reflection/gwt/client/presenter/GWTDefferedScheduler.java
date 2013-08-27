package com.arondor.common.reflection.gwt.client.presenter;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;

public class GWTDefferedScheduler
{

    public void scheduleDeffered(final Runnable runnable)
    {
        Scheduler.get().scheduleDeferred(new ScheduledCommand()
        {
            public void execute()
            {
                runnable.run();
            }
        });

    }

}

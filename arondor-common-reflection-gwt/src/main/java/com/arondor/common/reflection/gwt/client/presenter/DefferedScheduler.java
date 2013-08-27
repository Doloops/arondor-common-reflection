package com.arondor.common.reflection.gwt.client.presenter;

import com.google.gwt.core.shared.GWT;

public class DefferedScheduler
{
    public void scheduleDeffered(Runnable runnable)
    {
        if (GWT.isClient())
        {
            GWTDefferedScheduler defferedScheduler = new GWTDefferedScheduler();
            defferedScheduler.scheduleDeffered(runnable);
        }
        else
        {
            runnable.run();
        }
    }
}

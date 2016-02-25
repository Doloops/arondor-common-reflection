package com.arondor.common.management.threadpool;

import java.util.concurrent.ThreadFactory;

/**
 * Simple Thread-naming factory
 * 
 * @author Francois Barre
 *
 */
public class NamedThreadFactory implements ThreadFactory
{
    /**
     * The base name to use for thread naming
     */
    private final String name;

    /**
     * The thread index for this naming
     */
    private int threadIndex = 0;

    public String getName()
    {
        return name;
    }

    public NamedThreadFactory(String name)
    {
        this.name = name;
    }

    public synchronized Thread newThread(Runnable r)
    {
        return new Thread(r, getName() + "#" + threadIndex++);
    }

}

package com.arondor.common.management.threadpool;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;

import org.apache.log4j.Logger;

/**
 * Scheduled Thread Factory with timeout monitoring
 */
public class MonitoredScheduledThreadPoolExecutor extends ScheduledThreadPoolExecutor
{
    private static final Logger LOG = Logger.getLogger(MonitoredScheduledThreadPoolExecutor.class);

    public MonitoredScheduledThreadPoolExecutor(int corePoolSize)
    {
        super(corePoolSize);
    }

    public MonitoredScheduledThreadPoolExecutor(int corePoolSize, RejectedExecutionHandler handler)
    {
        super(corePoolSize, handler);
    }

    public MonitoredScheduledThreadPoolExecutor(int corePoolSize, ThreadFactory threadFactory)
    {
        super(corePoolSize, threadFactory);
    }

    public MonitoredScheduledThreadPoolExecutor(int corePoolSize, ThreadFactory threadFactory,
            RejectedExecutionHandler handler)
    {
        super(corePoolSize, threadFactory, handler);
    }

    private int timeout = 0;

    public int getTimeout()
    {
        return timeout;
    }

    public void setTimeout(int timeout)
    {
        this.timeout = timeout;
        startTaskMonitoring();
    }

    private Thread taskMonitoringThread = null;

    protected synchronized void startTaskMonitoring()
    {
        if (taskMonitoringThread != null)
        {
            return;
        }
        taskMonitoringThread = new Thread()
        {
            @Override
            public void run()
            {
                doMonitorLoop();
            }
        };
        taskMonitoringThread.setDaemon(true);
        taskMonitoringThread.start();
    }

    protected void doMonitorLoop()
    {
        while (true)
        {
            if (isTerminated())
            {
                LOG.info("Stopping monitor : shutdown !");
                return;
            }

            doMonitor();
            try
            {
                Thread.sleep(10);
            }
            catch (InterruptedException e)
            {
            }
        }
    }

    private synchronized void doMonitor()
    {
        for (Map.Entry<Thread, Long> threadStartEntry : runningThreads.entrySet())
        {
            Long startTime = threadStartEntry.getValue();
            if (startTime == null)
            {
                continue;
            }
            long now = System.currentTimeMillis();

            if (now - startTime >= timeout)
            {
                Thread th = threadStartEntry.getKey();
                LOG.info("Thread " + threadName(th) + " is timedout !");
                try
                {
                    th.interrupt();
                }
                catch (SecurityException e)
                {
                    LOG.error("Could not interrupt !", e);
                }
            }
        }
    }

    private final Map<Thread, Long> runningThreads = new HashMap<Thread, Long>();
    
    private static final String threadName(Thread th)
    {
        return th.getName() + " [id:" + th.getId() + "]";
    }

    protected synchronized void setTaskStart()
    {
        Thread th = Thread.currentThread();
        if (runningThreads.get(th) != null)
        {
            LOG.error("Thread " + threadName(th) + " has an unfinished task !");
            throw new IllegalStateException("Thread " + threadName(th) + " has an unfinished task !");
        }
        LOG.info("Thread : " + threadName(th) + ", Task Start");
        runningThreads.put(th, System.currentTimeMillis());
    }

    protected synchronized void setTaskEnd()
    {
        Thread th = Thread.currentThread();
        LOG.info("Thread : " + threadName(th) + ", Task End");
        if (runningThreads.get(th) == null)
        {
            LOG.error("Thread " + threadName(th) + " was not declared as running !");
        }
        runningThreads.remove(th);
    }

    private Runnable decorateRunnable(final Runnable task)
    {
        return new Runnable()
        {
            public void run()
            {
                LOG.debug("Decorate runnable " + task);
                try
                {
                    setTaskStart();
                    task.run();
                }
                finally
                {
                    setTaskEnd();
                }
            }

        };
    }


    private <T> Callable<T> decorateCallable(final Callable<T> task)
    {
        return new Callable<T>()
        {
            public T call() throws Exception
            {
                LOG.debug("Decorate callable " + task);
                try
                {
                    setTaskStart();
                    return task.call();
                }
                finally
                {
                    setTaskEnd();
                }
            }
        };
    }

    @Override
    public <T> Future<T> submit(Callable<T> task)
    {
        return super.submit(task);
    }

    @Override
    public Future<?> submit(Runnable task)
    {
        return super.submit(decorateRunnable(task));
    }

    @Override
    public <T> Future<T> submit(Runnable task, T result)
    {
        return super.submit(decorateRunnable(task), result);
    }
}

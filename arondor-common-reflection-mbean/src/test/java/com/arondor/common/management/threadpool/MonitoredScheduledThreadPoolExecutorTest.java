package com.arondor.common.management.threadpool;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;
import org.junit.Test;

import junit.framework.Assert;

public class MonitoredScheduledThreadPoolExecutorTest
{
    private static final Logger LOG = Logger.getLogger(MonitoredScheduledThreadPoolExecutorTest.class);

    @Test
    public void testMonitoring_1Thread() throws InterruptedException
    {
        MonitoredScheduledThreadPoolExecutor executor = new MonitoredScheduledThreadPoolExecutor(1);

        executor.setTimeout(100);

        final AtomicInteger atm = new AtomicInteger(0);

        final CountDownLatch cdl = new CountDownLatch(1);

        executor.submit(new Runnable()
        {
            public void run()
            {
                try
                {
                    LOG.info("Entering sleep");
                    Thread.sleep(1000);
                    LOG.info("Woke up !");
                    atm.incrementAndGet();
                }
                catch (InterruptedException e)
                {
                    LOG.info("Interrupted", e);
                }
                finally
                {
                    cdl.countDown();
                }
            }
        });

        cdl.await(1000, TimeUnit.SECONDS);
        Assert.assertEquals(0, atm.get());
    }

    @Test
    public void testMonitoring_8Threads_16Tasks() throws InterruptedException
    {
        MonitoredScheduledThreadPoolExecutor executor = new MonitoredScheduledThreadPoolExecutor(8);

        executor.setTimeout(100);

        final int nbTasks = 16;
        final List<Integer> someList = new ArrayList<Integer>();

        for (int tst = 0; tst < nbTasks; tst++)
        {
            someList.add(0);
        }

        for (int tst = 0; tst < nbTasks; tst++)
        {
            final int tstIdx = tst;
            executor.submit(new Runnable()
            {
                public void run()
                {
                    try
                    {
                        synchronized (someList)
                        {
                            someList.set(tstIdx, 1);
                        }
                        LOG.info("Entering sleep tst=" + tstIdx);
                        Thread.sleep(tstIdx % 2 == 0 ? 1000 : 10);
                        LOG.info("Woke up ! tst=" + tstIdx);
                        synchronized (someList)
                        {
                            someList.set(tstIdx, 2);
                        }
                    }
                    catch (InterruptedException e)
                    {
                        LOG.info("Interrupted for tst=" + tstIdx);
                    }
                }
            });
        }
        executor.shutdown();

        executor.awaitTermination(30, TimeUnit.SECONDS);

        for (int tst = 0; tst < nbTasks; tst++)
        {
            if (tst % 2 == 0)
            {
                Assert.assertEquals(1, someList.get(tst).intValue());
            }
            else
            {
                Assert.assertEquals(2, someList.get(tst).intValue());
            }
        }
    }

    @Test
    public void testCmdline() throws InterruptedException
    {
        MonitoredScheduledThreadPoolExecutor executor = new MonitoredScheduledThreadPoolExecutor(1);

        executor.setTimeout(500);

        String scriptOutFile = "target/script.out";
        new File(scriptOutFile).delete();

        final AtomicInteger atm = new AtomicInteger(0);

        executor.submit(new Runnable()
        {
            public void run()
            {
                try
                {
                    String cmd = "src/test/resources/waitscript.bat";
                    LOG.info("Starting : " + cmd);
                    Process pth = Runtime.getRuntime().exec(cmd);
                    int res = pth.waitFor();
                    LOG.info("res=" + res);
                    atm.incrementAndGet();
                }
                catch (InterruptedException e)
                {
                    LOG.info("Interrupted", e);
                }
                catch (IOException e)
                {
                    LOG.info("Interrupted", e);
                }
                finally
                {
                }
            }
        });
        executor.shutdown();

        executor.awaitTermination(30, TimeUnit.SECONDS);

        Assert.assertFalse(new File(scriptOutFile).exists());

    }
}

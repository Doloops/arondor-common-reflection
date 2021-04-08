package com.arondor.common.management.statistics;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import org.apache.log4j.Logger;

import com.arondor.common.management.mbean.MBeanObject;

/**
 * Statistics class aggregator
 * 
 * @author Francois Barre
 * 
 */
public class Statistics extends MBeanObject implements Serializable
{
    private static final long serialVersionUID = -6655570213872337647L;

    /**
     * Log4J logger
     */
    private static final Logger LOG = Logger.getLogger(Statistics.class);

    private static final Statistics SINGLETON = new Statistics();

    /**
     * Statistics : time when Statistics started
     */
    private long startTime = System.currentTimeMillis();

    /**
     * Set the number of values used for instantSpeed duration
     */
    private int instantSpeedNumber = 32;

    public static Statistics getInstance()
    {
        return SINGLETON;
    }

    private Statistics()
    {
        super("Statistics");
    }

    public static String prettyPrint(long numberMS)
    {
        if (numberMS < 1000)
        {
            return String.valueOf(numberMS + "ms");
        }
        if (numberMS < 60 * 1000)
        {
            return String.valueOf(((float) numberMS) / 1000 + "s");
        }
        long s = numberMS / 1000;
        long m = (s / 60) % 60;
        long h = (s / 3600) % 24;
        long d = (s / 86400);
        s = s % 60;
        return (d > 0 ? (d + "d") : "") + ((h > 0) ? (h + "h") : "") + m + "m" + s + "s";
    }

    /**
     * Simple class to store stat information
     * 
     */
    public class StatInfo extends MBeanObject
    {
        /**
         * Class of the StatInfo
         */
        private final Class<?> clazz;

        private String name;

        private long number = 0;

        private long total = 0;

        private long min = Long.MAX_VALUE;

        private long max = Long.MIN_VALUE;

        private Queue<Long> instantValues = new LinkedList<Long>();

        private Queue<Long> instantStartPoints = new LinkedList<Long>();

        /**
         * Simple Constructor
         * 
         * @param name
         *            the name for this stat info
         */
        public StatInfo(Class<?> clazz, String name)
        {
            super(clazz, name);
            this.clazz = clazz;
            this.name = name;
        }

        /**
         * Update stat
         * 
         * @param statPoint
         *            time spent, in milliseconds
         */
        protected synchronized void update(StatPoint statPoint)
        {
            number += statPoint.getNumber();
            long duration = statPoint.getDuration();
            total += duration;
            min = Math.min(min, duration);
            max = Math.max(max, duration);
            while (instantValues.size() >= getInstantSpeedNumber())
            {
                instantValues.remove();
                instantStartPoints.remove();
            }
            instantValues.add(duration);
            instantStartPoints.add(System.currentTimeMillis());
        }

        public float getTheoreticalRate()
        {
            return (total > 0 ? ((float) number * 1000 / total) : 0);
        }

        public float getEffectiveRate()
        {
            long runtime = (System.currentTimeMillis() - startTime);
            return (runtime > 0 ? ((float) number * 1000 / runtime) : 0);
        }

        public synchronized float getInstantDuration()
        {
            int number = instantValues.size();
            if (number == 0)
                return 0;
            long total = 0;
            for (Long value : instantValues)
            {
                total += value;
            }
            return ((float) total) / (float) number;
        }

        public float getInstantTheoreticalRate()
        {
            return (1000) / getInstantDuration();
        }

        public synchronized float getInstantEffectiveRate()
        {
            if (instantStartPoints.isEmpty())
                return 0f;
            long globalInstantDuration = System.currentTimeMillis() - instantStartPoints.peek();
            if (globalInstantDuration == 0)
                return 0f;
            int instantNumbers = instantStartPoints.size();
            return ((float) (1000 * instantNumbers)) / (float) globalInstantDuration;
        }

        public synchronized int getInstantValueNumber()
        {
            return instantValues.size();
        }

        public long getTotalCalls()
        {
            return number;
        }

        public long getMinDuration()
        {
            return min;
        }

        public long getMaxDuration()
        {
            return max;
        }

        public float getAverageDuration()
        {
            return (number > 0 ? ((float) total / (float) number) : 0f);
        }

        public String getTotalSpent()
        {
            return prettyPrint(total);
        }

        public long getTotal()
        {
            return total;
        }

        public Class<?> getClazz()
        {
            return clazz;
        }

        /**
         * To String
         */
        @Override
        public String toString()
        {
            return clazz.getName() + "." + name + "[total=" + prettyPrint(total) + ",calls=" + number + ",min="
                    + prettyPrint(min) + ",max=" + prettyPrint(max) + ",avg="
                    + prettyPrint((number > 0 ? (total / number) : 0)) + ",theo=" + getTheoreticalRate() + " c/s"
                    + ",rate=" + getEffectiveRate() + " c/s" + "]";
        }

        public String getName()
        {
            return name;
        }
    }

    /**
     * Statistics info map
     */
    private Map<String, StatInfo> statInfoMap = new HashMap<String, StatInfo>();

    /**
     * 
     * @param statsInterval
     *            At which interval shall we dump statistics
     */
    public void startStatThread(final int statsInterval)
    {
        LOG.warn("No longer supported: Statistics.startStatThread()");
    }

    public void stopStatThread()
    {
        LOG.warn("No longer supported: Statistics.stopStatThread()");
    }

    public static void stopStatistics()
    {
        getInstance().showStatistics();
    }

    public void updateStat(StatPoint statPoint, String name)
    {
        String fullname = statPoint.getFullName();
        if (name != null)
            fullname += "." + name;
        StatInfo statInfo;
        synchronized (statInfoMap)
        {

            statInfo = statInfoMap.get(fullname);
            if (statInfo == null)
            {
                statInfo = new StatInfo(statPoint.getClazz(), statPoint.getName() + (name != null ? name : ""));
                statInfoMap.put(fullname, statInfo);
            }
        }
        statInfo.update(statPoint);

    }

    public void updateStat(StatPoint statPoint)
    {
        updateStat(statPoint, null);
    }

    /**
    * 
    */
    public synchronized void showStatistics()
    {
        String msg = "Stats : (running for " + getRunningTime() + "ms)";
        List<String> keys = new ArrayList<String>(statInfoMap.keySet());
        Collections.sort(keys);
        for (String key : keys)
        {
            msg += "\n" + statInfoMap.get(key);
        }
        LOG.info(msg);
    }

    public synchronized void writeStatistics2CSV(BufferedWriter writer) throws IOException
    {
        writer.write("class;name;total;calls;minimum;maximum;average;theoreticalRate;effectiveRate\n");
        for (StatInfo statInfo : statInfoMap.values())
        {
            writer.write(statInfo.getClazz().getName() + ";" + statInfo.getName() + ";" + statInfo.getTotal() + ";"
                    + statInfo.getTotalCalls() + ";" + statInfo.getMinDuration() + ";" + statInfo.getMaxDuration() + ";"
                    + statInfo.getAverageDuration() + ";" + statInfo.getTheoreticalRate() + ";"
                    + statInfo.getEffectiveRate() + "\n");
        }
    }

    public Collection<StatInfo> getAllStats()
    {
        return statInfoMap.values();
    }

    public Long getRunningTime()
    {
        return System.currentTimeMillis() - startTime;
    }

    public String getRunningTimeStr()
    {
        return prettyPrint(System.currentTimeMillis() - startTime);
    }

    public void setInstantSpeedNumber(int instantSpeedNumber)
    {
        this.instantSpeedNumber = instantSpeedNumber;
    }

    public int getInstantSpeedNumber()
    {
        return instantSpeedNumber;
    }

}

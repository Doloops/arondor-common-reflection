package com.arondor.common.management.statistics;

/**
 * Statistics start point
 * @author Francois Barre
 *
 */
public class StatPoint
{
  private final Class<?> clazz;
  private String name;
  public void setName(String name)
  {
    this.name = name;
  }

  private long startPoint;
  private long number = 1;
  
  public StatPoint(Class<?> clazz, String name)
  {
    this.clazz = clazz;
    this.name = name;
    this.setStartPoint(System.currentTimeMillis());
  }
  
  public StatPoint(Object o, String name)
  {
    this.clazz = o.getClass();
    this.name = name;
    this.setStartPoint(System.currentTimeMillis());
  }

  public void setStartPoint(long startPoint)
  {
    this.startPoint = startPoint;
  }

  public long getStartPoint()
  {
    return startPoint;
  }

  public long getDuration()
  {
    return (System.currentTimeMillis() - getStartPoint());
  }
  
  public Class<?> getClazz()
  {
    return clazz;
  }

  public String getName()
  {
    return name;
  }

  public String getFullName()
  {
    return clazz.getName() + "@" + getName();
  }

  public String toString()
  {
    return getFullName() + " : " + Statistics.prettyPrint(getDuration());
  }

  public void setNumber(long number)
  {
    this.number = number;
  }

  public long getNumber()
  {
    return number;
  }
  
  public void update()
  {
      Statistics.getInstance().updateStat(this);
  }

  public void update(String name)
  {
      Statistics.getInstance().updateStat(this, name);
  }
}

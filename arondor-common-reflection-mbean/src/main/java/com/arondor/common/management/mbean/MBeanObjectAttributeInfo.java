package com.arondor.common.management.mbean;

import javax.management.MBeanAttributeInfo;

import org.apache.log4j.Logger;

/**
 * Exposed object attribute information, used in MbeanObject to describe an
 * attribute
 * 
 * @author Francois Barre
 * 
 */
public class MBeanObjectAttributeInfo
{
    /**
     * Logger stuff
     */
    private static final Logger log = Logger.getLogger(MBeanObjectAttributeInfo.class);

    /**
     * Attribute name
     */
    protected String name;

    /**
     * Attribute description
     */
    protected String description;

    /**
     * Attribute class
     */
    protected Class<?> clazz;

    /**
     * Is this attribute readable : does it have a getter
     */
    protected boolean readable;

    /**
     * Is this attribute writable : does it have a setter
     */
    protected boolean writable;

    /**
     * If this attribute is a boolean, does its getter have a 'is' prefix
     * instead of a 'get' prefix
     */
    protected boolean is = false;

    /**
     * Attribute description constructor
     * 
     * @param name
     *            attribute name
     * @param description
     *            attribute description
     * @param clazz
     *            attribute class
     * @param readable
     *            is this attribute readable
     * @param writable
     *            is this attribute writable
     */
    public MBeanObjectAttributeInfo(String name, String description, Class<?> clazz, boolean readable, boolean writable)
    {
        this.name = name;
        this.description = description;
        this.clazz = clazz;
        this.readable = readable;
        this.writable = writable;
    }

    public void setReadable()
    {
        this.readable = true;
    }

    public void setWritable()
    {
        this.writable = true;
    }

    /**
     * Transform this attribute description to a MBeanAttributeInfo
     * 
     * @return a MBeanAttributeInfo corresponding to this attribute information
     */
    public MBeanAttributeInfo toMBeanAttributeInfo()
    {
        log.debug("Generate MBeanAttributeInfo(name=" + name + ", class=" + clazz.getName() + ", desc=" + description
                + ", readable=" + readable + ", writable=" + writable + ", is=" + is + ")");
        return new MBeanAttributeInfo(name, clazz.getName(), description, readable, writable, is);
    }

    public String getName()
    {
        return name;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public boolean isIs()
    {
        return is;
    }

    public void setIs(boolean is)
    {
        // this.is = is;
    }

}

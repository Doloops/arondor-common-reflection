package com.arondor.common.reflection.xstream;

import org.apache.log4j.Logger;

import com.arondor.common.reflection.api.parser.AccessibleClassParser;
import com.arondor.common.reflection.model.config.ObjectConfiguration;
import com.arondor.common.reflection.model.java.AccessibleClass;
import com.arondor.common.reflection.model.java.AccessibleField;
import com.arondor.common.reflection.parser.java.JavaAccessibleClassParser;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class AbstractTestXStream
{
    private static final Logger LOG = Logger.getLogger(AbstractTestXStream.class);

    private final ObjectConfigurationReader converter = new GWTObjectConfigurationParserJava();

    private final XStream xstream = new XStream(new DomDriver());

    private final AccessibleClassParser classParser = new JavaAccessibleClassParser();

    protected AbstractTestXStream()
    {
        if (true)
        {
            converter.setTypeOracle(new TypeOracle()
            {
                @Override
                public String guessType(String className, String fieldName)
                {
                    LOG.debug("guessType(className=" + className + ", fieldName=" + fieldName + ")");
                    try
                    {
                        Class<?> jClass = Class.forName(className);
                        AccessibleClass axsClazz = classParser.parseAccessibleClass(jClass);

                        AccessibleField axsField = axsClazz.getAccessibleFields().get(fieldName);

                        if (axsField == null)
                        {
                            LOG.error("guessType(className=" + className + ", fieldName=" + fieldName
                                    + ") => NO FIELD OF THAT NAME !");
                        }
                        String guessedType = axsField.getClassName();
                        LOG.debug("guessType(className=" + className + ", fieldName=" + fieldName + ") => "
                                + guessedType);
                        return guessedType;
                    }
                    catch (ClassNotFoundException e)
                    {
                        LOG.error("Error", e);
                    }
                    return null;
                }
            });
        }

        xstream.aliasSystemAttribute("class", "class");
    }

    protected ObjectConfiguration serializeAndParse(Object o)
    {
        String xml = xstream.toXML(o);

        LOG.info("Result xml :" + xml);

        ObjectConfiguration oc = converter.parse(xml);
        return oc;
    }
}

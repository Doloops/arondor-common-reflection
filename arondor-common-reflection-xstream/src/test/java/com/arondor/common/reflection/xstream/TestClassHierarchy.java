package com.arondor.common.reflection.xstream;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.arondor.common.reflection.model.config.ElementConfiguration;
import com.arondor.common.reflection.model.config.ElementConfiguration.ElementConfigurationType;
import com.arondor.common.reflection.xstream.testing.ChildClass;
import com.arondor.common.reflection.xstream.testing.OtherChildClass;
import com.arondor.common.reflection.xstream.testing.ParentClass;
import com.arondor.common.reflection.xstream.testing.orchard.Apple;
import com.arondor.common.reflection.xstream.testing.orchard.Banana;
import com.arondor.common.reflection.xstream.testing.orchard.Fruit;
import com.arondor.common.reflection.xstream.testing.orchard.FruitBasket;
import com.arondor.common.reflection.xstream.testing.orchard.SingleFruitBasket;
import com.arondor.common.reflection.model.config.ListConfiguration;
import com.arondor.common.reflection.model.config.ObjectConfiguration;
import com.arondor.common.reflection.model.config.PrimitiveConfiguration;

public class TestClassHierarchy extends AbstractTestXStream
{

    @Test
    public void testHierarchy0_Parse()
    {
        ParentClass pc = new ParentClass();
        ChildClass cc = new ChildClass();
        cc.setSomeValue("Some string value");
        pc.setChild(cc);

        ObjectConfiguration oc = serializeAndParse(pc);

        Assert.assertEquals(pc.getClass().getName(), oc.getClassName());

        ElementConfiguration ec = oc.getFields().get("child");
        Assert.assertEquals(ElementConfigurationType.Object, ec.getFieldConfigurationType());
        Assert.assertTrue(ec instanceof ObjectConfiguration);

        ObjectConfiguration ocChild = (ObjectConfiguration) ec;
        Assert.assertEquals(cc.getClass().getName(), ocChild.getClassName());

        Assert.assertEquals("Some string value",
                ((PrimitiveConfiguration) ocChild.getFields().get("someValue")).getValue());
    }

    @Test
    public void testHierarchy1_Parse()
    {
        ParentClass pc = new ParentClass();
        OtherChildClass cc = new OtherChildClass();
        cc.setSomeValue("Some string value");
        cc.setOtherValue("Some other string value");
        pc.setChild(cc);

        ObjectConfiguration oc = serializeAndParse(pc);

        Assert.assertEquals(pc.getClass().getName(), oc.getClassName());

        ElementConfiguration ec = oc.getFields().get("child");
        Assert.assertEquals(ElementConfigurationType.Object, ec.getFieldConfigurationType());
        Assert.assertTrue(ec instanceof ObjectConfiguration);

        ObjectConfiguration ocChild = (ObjectConfiguration) ec;
        Assert.assertEquals(cc.getClass().getName(), ocChild.getClassName());

        Assert.assertEquals("Some string value",
                ((PrimitiveConfiguration) ocChild.getFields().get("someValue")).getValue());
        Assert.assertEquals("Some other string value",
                ((PrimitiveConfiguration) ocChild.getFields().get("otherValue")).getValue());
    }

    @Test
    public void testHierarchy_FruitBasket0()

    {
        FruitBasket basket = new FruitBasket();
        basket.setFruits(new ArrayList<Fruit>());
        basket.getFruits().add(new Banana());
        basket.getFruits().add(new Apple());
        basket.getFruits().add(new Banana());
        basket.getFruits().add(new Apple());

        ObjectConfiguration oc = serializeAndParse(basket);

        Assert.assertEquals(basket.getClass().getName(), oc.getClassName());

        ElementConfiguration ec = oc.getFields().get("fruits");
        Assert.assertEquals(ElementConfigurationType.List, ec.getFieldConfigurationType());
        Assert.assertTrue(ec instanceof ListConfiguration);

        List<ElementConfiguration> fruits = ((ListConfiguration) ec).getListConfiguration();

        Assert.assertEquals(4, fruits.size());

        ObjectConfiguration oc0 = (ObjectConfiguration) fruits.get(0);
        Assert.assertEquals(Banana.class.getName(), oc0.getClassName());

        ObjectConfiguration oc1 = (ObjectConfiguration) fruits.get(1);
        Assert.assertEquals(Apple.class.getName(), oc1.getClassName());

        ObjectConfiguration oc2 = (ObjectConfiguration) fruits.get(2);
        Assert.assertEquals(Banana.class.getName(), oc2.getClassName());

        ObjectConfiguration oc3 = (ObjectConfiguration) fruits.get(3);
        Assert.assertEquals(Apple.class.getName(), oc3.getClassName());
    }

    @Test
    public void testHierarchy_SingleFruit0()

    {
        SingleFruitBasket basket = new SingleFruitBasket();
        basket.setFruit(new Apple());

        ObjectConfiguration oc = serializeAndParse(basket);

        Assert.assertEquals(basket.getClass().getName(), oc.getClassName());

        ElementConfiguration ec = oc.getFields().get("fruit");
        Assert.assertEquals(ElementConfigurationType.Object, ec.getFieldConfigurationType());

        ObjectConfiguration oc0 = (ObjectConfiguration) ec;
        Assert.assertEquals(Apple.class.getName(), oc0.getClassName());
    }
}

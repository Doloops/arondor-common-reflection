package com.arondor.common.reflection.util;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class PasswordCipherUtils
{
    PasswordCipher cipher;

    @Before
    public void init()
    {
        cipher = new PasswordCipher();
    }

    @Test
    public void test0()
    {
        String source = "toto";
        String encoded = cipher.encode(source);
        Assert.assertNotEquals(source, encoded);
        System.out.println("Encoded: " + encoded);
        String decoded = cipher.decode(encoded);
        System.out.println("Decoded: " + decoded);
        Assert.assertEquals(source, decoded);
    }

    @Test
    public void test1()
    {
        String source = "12345678901234567890123456789012345678901234567890";
        String encoded = cipher.encode(source);
        Assert.assertNotEquals(source, encoded);
        System.out.println("Encoded: " + encoded);
        String decoded = cipher.decode(encoded);
        System.out.println("Decoded: " + decoded);
        Assert.assertEquals(source, decoded);
    }

}

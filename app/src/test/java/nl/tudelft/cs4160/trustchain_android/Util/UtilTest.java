package nl.tudelft.cs4160.trustchain_android.Util;

import junit.framework.Assert;

import org.junit.Test;

import static nl.tudelft.cs4160.trustchain_android.Util.Util.ellipsize;

public class UtilTest {
    @Test
    public void ellipsizetest() {
        String input = "12345678910";
        String expected = "1(..)0";
        Assert.assertEquals(expected,ellipsize(input,5));
    }

    @Test
    public void ellipsizetest2() {
        String input = "12345678910";
        String expected = "12(..)10";
        Assert.assertEquals(expected,ellipsize(input,8));
    }

    @Test
    public void ellipsizetest3() {
        String input = "12345678910";
        Assert.assertEquals(input,ellipsize(input,11));
    }

    @Test
    public void ellipsizetest4() {
        String input = "12345678910";
        String expected = "1(..)0";
        Assert.assertEquals(expected,ellipsize(input,6));
    }

    @Test
    public void ellipsizetest5() {
        String input = "12";
        String expected = "12";
        Assert.assertEquals(expected,ellipsize(input,5));
    }

}
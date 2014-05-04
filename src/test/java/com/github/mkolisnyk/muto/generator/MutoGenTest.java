package com.github.mkolisnyk.muto.generator;

import org.junit.Assert;
import org.junit.Test;


/**
 * .
 * @author Myk Kolisnyk
 */
public class MutoGenTest {

    @Test
    public void testGenerate(){
        MutoGen gen = new MutoGen();
        Assert.assertEquals("", gen.generate("", null));
    }
}

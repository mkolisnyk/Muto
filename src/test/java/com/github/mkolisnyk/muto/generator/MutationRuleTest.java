package com.github.mkolisnyk.muto.generator;

import org.junit.Assert;
import org.junit.Test;

public class MutationRuleTest {
    
    private class FakeMutationRule extends MutationRule {

        @Override
        public String apply(String input, int position) {
            return null;
        }

        @Override
        public int total(String input) {
            return 2;
        }
    }
    
    @Test
    public void testNextHasNext(){
        MutationRule rule = new FakeMutationRule();
        Assert.assertEquals(0,rule.getTick());
        Assert.assertTrue(rule.hasNext(""));
        Assert.assertNull(rule.next(""));
        Assert.assertEquals(1,rule.getTick());
        Assert.assertTrue(rule.hasNext(""));
        Assert.assertNull(rule.next(""));
        Assert.assertFalse(rule.hasNext(""));
    }
    
    @Test
    public void testResetRule(){
        MutationRule rule = new FakeMutationRule();
        rule.hasNext("");
        rule.hasNext("");
        rule.reset();
        Assert.assertEquals(0,rule.getTick());
        Assert.assertTrue(rule.hasNext(""));
    }
}

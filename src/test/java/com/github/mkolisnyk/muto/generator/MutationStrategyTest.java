package com.github.mkolisnyk.muto.generator;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MutationStrategyTest {

    private class FakeMutationRule extends MutationRule {

        @Override
        public String apply(String input, int position) {
            return null;
        }

        @Override
        public int total(String input) {
            return 1;
        }
    }
    
    private class FakeMutationStrategy extends MutationStrategy {

        @Override
        public String next(String input) {
            for(MutationRule rule:this.getRuleSet()){
                rule.next(input);
            }
            return null;
        }        
    }

    private MutationStrategy strategy;
    private MutationRule rule;

    @Before
    public void setUp(){
        strategy = new FakeMutationStrategy();
        rule = new FakeMutationRule();
    }
    
    @Test
    public void testAddRule() {
        strategy.addRule(rule);
        strategy.addRule(rule);
        Assert.assertEquals(2, strategy.getRuleSet().size());
        Assert.assertEquals(rule, strategy.getRuleSet().get(0));
        Assert.assertEquals(rule, strategy.getRuleSet().get(1));
    }

    @Test
    public void testNext() {
        strategy.addRule(rule);
        Assert.assertTrue(strategy.hasNext(""));
        Assert.assertNull(strategy.next(""));
        Assert.assertFalse(strategy.hasNext(""));
    }

    @Test
    public void testReset() {
        strategy.addRule(rule);
        Assert.assertNull(strategy.next(""));
        Assert.assertEquals(1,strategy.getRuleSet().get(0).getTick());
        Assert.assertFalse(strategy.hasNext(""));
        strategy.reset();
        Assert.assertTrue(strategy.hasNext(""));
        Assert.assertEquals(0,strategy.getRuleSet().get(0).getTick());
    }
    
    @Test
    public void testEmptyStrategy() {
        Assert.assertFalse(strategy.hasNext(""));
        strategy.reset();
        Assert.assertFalse(strategy.hasNext(""));
    }
}

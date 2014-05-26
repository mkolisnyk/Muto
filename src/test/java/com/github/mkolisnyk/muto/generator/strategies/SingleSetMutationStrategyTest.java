package com.github.mkolisnyk.muto.generator.strategies;

import static org.junit.Assert.*;
import org.junit.Assert;

import org.junit.Test;

import com.github.mkolisnyk.muto.generator.rules.BlockCleanMutationRule;
import com.github.mkolisnyk.muto.generator.rules.NumberSignMutationRule;

public class SingleSetMutationStrategyTest {
    private SingleSetMutationStrategy strategy;
    private String sample = "sample {{nested} -1 with {sign {test} - 1 } line}";
    public SingleSetMutationStrategyTest() {
        strategy = new SingleSetMutationStrategy();
        strategy.addRule(new BlockCleanMutationRule());
        strategy.addRule(new NumberSignMutationRule());
    }
    
    @Test
    public void testNext() {
        String result = strategy.next(sample);
        Assert.assertNotEquals(sample, result);
    }

    @Test
    public void testHasNextIsAlwaysFalse() {
        Assert.assertFalse(strategy.hasNext(sample));
        Assert.assertEquals(1, strategy.total(sample));
        strategy.reset();
        Assert.assertFalse(strategy.hasNext(sample));
        Assert.assertEquals(1, strategy.total(sample));
    }

}

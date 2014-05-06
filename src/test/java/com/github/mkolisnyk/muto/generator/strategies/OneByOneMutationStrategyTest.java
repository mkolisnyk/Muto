package com.github.mkolisnyk.muto.generator.strategies;

import org.junit.Assert;
import org.junit.Test;

import com.github.mkolisnyk.muto.generator.MutationRule;
import com.github.mkolisnyk.muto.generator.rules.BlockCleanMutationRule;
import com.github.mkolisnyk.muto.generator.rules.NumberSignMutationRule;

public class OneByOneMutationStrategyTest {

    private String input = "class Test { private int var=-1; public void main(){int i=3;}}";
    
    @Test
    public void testNext() {
        String expecteds[] = {
                "class Test { private int var=1; public void main(){int i=3;}}",
                "class Test { private int var=-1; public void main(){int i=-3;}}",
                "class Test {}",
                "class Test { private int var=-1; public void main(){}}"
        };
        int currentTurn = 0;
        MutationRule blockClean = new BlockCleanMutationRule();
        MutationRule numberSign = new NumberSignMutationRule();
        OneByOneMutationStrategy strategy = new OneByOneMutationStrategy();
        strategy.addRule(numberSign);
        strategy.addRule(blockClean);
        
        while(strategy.hasNext(input)){
            String actual = strategy.next(input);
            Assert.assertEquals(expecteds[currentTurn], actual);
            currentTurn++;
        }
        Assert.assertEquals(expecteds.length, currentTurn);
        Assert.assertFalse(strategy.hasNext(input));
    }

    @Test
    public void testNextOnEmptyRuleset(){
        OneByOneMutationStrategy strategy = new OneByOneMutationStrategy();
        String actual = strategy.next(input);
        Assert.assertEquals(input, actual);
    }

    @Test
    public void testNextOnCompletedRuleset(){
        MutationRule blockClean = new BlockCleanMutationRule();
        MutationRule numberSign = new NumberSignMutationRule();
        OneByOneMutationStrategy strategy = new OneByOneMutationStrategy();
        strategy.addRule(numberSign);
        strategy.addRule(blockClean);
        while(strategy.hasNext(input)){
            strategy.next(input);
        }
        Assert.assertFalse(strategy.hasNext(input));
        Assert.assertEquals(input, strategy.next(input));
    }
}

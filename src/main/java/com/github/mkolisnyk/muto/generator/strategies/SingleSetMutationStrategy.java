/**
 * .
 */
package com.github.mkolisnyk.muto.generator.strategies;

import java.util.Random;

import com.github.mkolisnyk.muto.generator.MutationRule;
import com.github.mkolisnyk.muto.generator.MutationStrategy;

/**
 * @author Myk Kolisnyk
 *
 */
public class SingleSetMutationStrategy extends MutationStrategy {

    /* (non-Javadoc)
     * @see com.github.mkolisnyk.muto.generator.MutationStrategy
     * #next(java.lang.String)
     */
    @Override
    public final String next(final String input) {
        OneByOneMutationStrategy strategy = new OneByOneMutationStrategy();
        for (MutationRule rule : this.getRuleSet()) {
            strategy.addRule(rule);
        }
        int total = strategy.total(input);
        Random random = new Random();
        int index = random.nextInt(total);
        for (int i = 0; i < index; i++) {
            strategy.next(input);
        }
        String result = strategy.next(input);
        return result;
    }

    /**
     * .
     * @param input .
     * @return .
     */
    @Override
    public final boolean hasNext(final String input) {
        return false;
    }

    /**
     * .
     * @param input .
     * @return .
     */
    @Override
    public final int total(final String input) {
        return 0;
    }
}

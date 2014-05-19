package com.github.mkolisnyk.muto.generator.strategies;

import com.github.mkolisnyk.muto.generator.MutationRule;
import com.github.mkolisnyk.muto.generator.MutationStrategy;

/**
 * @author Myk Kolisnyk
 */
public abstract class IterativeMutationStrategy extends MutationStrategy {
    /**
     * .
     * @param input .
     * @return .
     */
    @Override
    public final boolean hasNext(final String input) {
        for (MutationRule rule:this.getRuleSet()) {
            if (rule.hasNext(input)) {
                return true;
            }
        }
        return false;
    }

    /**
     * .
     * @param input .
     * @return .
     */
    @Override
    public final int total(final String input) {
        int count = 0;
        for (MutationRule rule : this.getRuleSet()) {
            count += rule.total(input);
        }
        return count;
    }
}

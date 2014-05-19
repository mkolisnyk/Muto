package com.github.mkolisnyk.muto.generator.strategies;

import com.github.mkolisnyk.muto.generator.MutationRule;

/**
 * .
 * @author Myk Kolisnyk
 */
public class OneByOneMutationStrategy extends IterativeMutationStrategy {

    /**
     * .
     */
    private int currentRuleIndex = 0;
    /**
     * @param input .
     * @return .
     */
    @Override
    protected final String next(final String input) {
        int total = this.getRuleSet().size();
        MutationRule rule;
        if (currentRuleIndex >= total) {
            return input;
        }
        rule = this.getRuleSet().get(currentRuleIndex);
        if (!rule.hasNext(input)) {
            currentRuleIndex++;
        }
        if (currentRuleIndex >= total) {
            return input;
        }
        rule = this.getRuleSet().get(currentRuleIndex);
        String result = rule.doNext(input);
        this.setLocation(rule.getLocation());
        return result;
    }

}

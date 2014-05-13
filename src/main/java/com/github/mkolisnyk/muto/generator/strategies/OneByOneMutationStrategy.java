package com.github.mkolisnyk.muto.generator.strategies;

import com.github.mkolisnyk.muto.generator.MutationRule;
import com.github.mkolisnyk.muto.generator.MutationStrategy;

/**
 * .
 * @author Myk Kolisnyk
 */
public class OneByOneMutationStrategy extends MutationStrategy {

    /**
     * .
     */
    private int currentRuleIndex = 0;
    @Override
    public final String next(final String input) {
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
        String result = rule.next(input);
        this.setLocation(rule.getLocation());
        return result;
    }

}

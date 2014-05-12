package com.github.mkolisnyk.muto.generator;

import java.util.ArrayList;
import java.util.List;

import com.github.mkolisnyk.muto.data.MutationLocation;

/**
 * @author Myk Kolisnyk
 */
public abstract class MutationStrategy {

    /**
     * .
     */
    private MutationLocation location;
    /**
     * .
     */
    private List<MutationRule> ruleSet = null;

    /**
     * .
     * @return .
     */
    public final List<MutationRule> getRuleSet() {
        if (ruleSet == null) {
            ruleSet = new ArrayList<MutationRule>();
        }
        return ruleSet;
    }
    /**
     * .
     * @param rule .
     */
    public final void addRule(final MutationRule rule) {
        if (ruleSet == null) {
            ruleSet = new ArrayList<MutationRule>();
        }
        ruleSet.add(rule);
    }
    /**
     * .
     * @return .
     */
    public final MutationLocation getLocation() {
        return location;
    }
    /**
     * .
     * @param location .
     */
    public final void setLocation(final MutationLocation location) {
        this.location = location;
    }
    /**
     * .
     * @param input .
     * @return .
     */
    public abstract String next(String input);
    /**
     * .
     * @param input .
     * @return .
     */
    public final boolean hasNext(final String input) {
        if (ruleSet == null) {
            return false;
        }
        for (MutationRule rule:ruleSet) {
            if (rule.hasNext(input)) {
                return true;
            }
        }
        return false;
    }
    /**
     * .
     */
    public final void reset() {
        if (ruleSet == null) {
            return;
        }
        for (MutationRule rule:ruleSet) {
            rule.reset();
        }
    }
}

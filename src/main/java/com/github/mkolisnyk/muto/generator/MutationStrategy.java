package com.github.mkolisnyk.muto.generator;

import java.util.ArrayList;
import java.util.List;

import com.github.mkolisnyk.muto.data.MutationLocation;
import com.github.mkolisnyk.muto.reporter.MutoListener;

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
     */
    private List<MutoListener> listeners = new ArrayList<MutoListener>();
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
     * @param locationValue .
     */
    public final void setLocation(final MutationLocation locationValue) {
        this.location = locationValue;
    }
    /**
     * @return the listeners
     */
    public final List<MutoListener> getListeners() {
        return listeners;
    }
    /**
     * @param listenersValue the listeners to set
     */
    public final void setListeners(final List<MutoListener> listenersValue) {
        this.listeners = listenersValue;
    }
    /**
     * .
     * @param input .
     * @return .
     */
    protected abstract String next(String input);
    /**
     * .
     * @param input .
     * @return .
     */
    public final String doNext(final String input) {
        for (MutoListener listener : this.getListeners()) {
            listener.beforeMutationStrategyRun();
        }
        String result = this.next(input);
        for (MutoListener listener : this.getListeners()) {
            listener.afterMutationStrategyRun();
        }
        return result;
    }

    /**
     * .
     * @param input .
     * @return .
     */
    public abstract boolean hasNext(final String input);
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
    /**
     * .
     * @param input .
     * @return .
     */
    public abstract int total(final String input);
}

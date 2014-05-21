package com.github.mkolisnyk.muto.reporter.listeners;

import com.github.mkolisnyk.muto.data.MutationLocation;
import com.github.mkolisnyk.muto.reporter.MutoListener;
import com.github.mkolisnyk.muto.reporter.MutoResult;

/**
 * @author Myk Kolisnyk
 */
public class DummyListener implements MutoListener {

    /**
     * .
     */
    public final void beforeSuiteRun() {
    }

    /**
     * .
     */
    public final void afterSuiteRun() {
    }

    /**
     * .
     */
    public final void beforeTestRun() {
    }

    /**
     * .
     * @param result .
     */
    public final void afterTestRun(final MutoResult result) {
    }

    /**
     * .
     */
    public final void beforeFileStrategyRun() {
    }

    /**
     * .
     * @param fileName .
     */
    public final void afterFileStrategyRun(final String fileName) {
    }

    /**
     * .
     */
    public final void beforeMutationStrategyRun() {
    }

    /**
     * .
     */
    public final void afterMutationStrategyRun() {
    }

    /**
     * .
     */
    public final void beforeMutationRuleRun() {
    }

    /**
     * .
     * @param location .
     */
    public final void afterMutationRuleRun(final MutationLocation location) {
    }
}

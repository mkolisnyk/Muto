package com.github.mkolisnyk.muto.reporter;

import com.github.mkolisnyk.muto.data.MutationLocation;

/**
 * @author Myk Kolisnyk
 */
public interface MutoListener {

    /**
     *
     */
    void beforeSuiteRun();

    /**
     * .
     */
    void afterSuiteRun();

    /**
     * .
     */
    void beforeTestRun();

    /**
     * .
     * @param result .
     */
    void afterTestRun(MutoResult result);
    /**
     * .
     */
    void beforeFileStrategyRun();
    /**
     * .
     * @param fileName .
     */
    void afterFileStrategyRun(String fileName);

    /**
     * .
     */
    void beforeMutationStrategyRun();
    /**
     * .
     */
    void afterMutationStrategyRun();

    /**
     * .
     */
    void beforeMutationRuleRun();
    /**
     * .
     * @param location .
     */
    void afterMutationRuleRun(MutationLocation location);
}

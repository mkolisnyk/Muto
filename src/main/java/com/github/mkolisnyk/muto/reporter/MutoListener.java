package com.github.mkolisnyk.muto.reporter;

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
     */
    void afterFileStrategyRun();

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
     */
    void afterMutationRuleRun();
}

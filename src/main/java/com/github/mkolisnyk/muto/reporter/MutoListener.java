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
     * @throws Exception .
     */
    void afterSuiteRun() throws Exception;

    /**
     * .
     */
    void beforeTestRun();

    /**
     * .
     * @param result .
     * @throws Exception .
     */
    void afterTestRun(MutoResult result) throws Exception;
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

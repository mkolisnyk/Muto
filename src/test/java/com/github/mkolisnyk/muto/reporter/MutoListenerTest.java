package com.github.mkolisnyk.muto.reporter;

/**
 * @author Myk Kolisnyk
 */
public interface MutoListenerTest {

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
     */
    void afterTestRun();
}

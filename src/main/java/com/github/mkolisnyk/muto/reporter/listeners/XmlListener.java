package com.github.mkolisnyk.muto.reporter.listeners;

import java.io.File;

import javax.xml.bind.JAXB;

import com.github.mkolisnyk.muto.data.MutationLocation;
import com.github.mkolisnyk.muto.reporter.MutoListener;
import com.github.mkolisnyk.muto.reporter.MutoResult;

/**
 * @author Myk Kolisnyk
 */
public class XmlListener implements MutoListener {

    /**
     * .
     */
    private int testCount = 0;
    /**
     * .
     */
    public void beforeSuiteRun() {
    }
    /**
     * .
     */
    public void afterSuiteRun() {
    }
    /**
     * .
     */
    public final void beforeTestRun() {
        testCount++;
    }
    /**
     * .
     * @param result .
     */
    public final void afterTestRun(final MutoResult result) {
        String outputFile =
               result.getOutputLocation()
                + File.separator + "muto_result_" + testCount + ".xml";
        JAXB.marshal(result, new File(outputFile));
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

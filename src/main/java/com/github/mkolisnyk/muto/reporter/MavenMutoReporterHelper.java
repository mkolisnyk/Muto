package com.github.mkolisnyk.muto.reporter;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import com.github.mkolisnyk.muto.reporter.result.JUnitTestCase;
import com.github.mkolisnyk.muto.reporter.result.JUnitTestSuite;

/**
 * @author Myk Kolisnyk
 */
public class MavenMutoReporterHelper {
    /**
     * .
     */
    public  static final String PASSED = "Passed";
    /**
     * .
     */
    public  static final String FAILED = "Failed";
    /**
     * .
     */
    public  static final String ERRORED = "Errored";

    /**
     * .
     * @param resultArray .
     * @return .
     */
    public final Map<String, Integer> getOverviewMap(
            final MutoResult[] resultArray) {
        Map<String, Integer> result = new HashMap<String, Integer>();
        int passed = 0;
        int failed = 0;
        int errored = 0;
        for (MutoResult item : resultArray) {
            if (item.getResults() != null
                    && item.getResults().size() > 0) {
                int errorCount = 0;
                for (JUnitTestSuite suite:item.getResults()) {
                    errorCount += suite.getErrors() + suite.getFailures();
                }
                if (errorCount > 0) {
                    errored++;
                } else {
                    passed++;
                }
            } else {
                failed++;
            }
        }
        result.put(PASSED, passed);
        result.put(FAILED, failed);
        result.put(ERRORED, errored);
        return result;
    }

    /**
     * .
     * @param resultArray .
     * @return .
     */
    @SuppressWarnings("unchecked")
    public final Map<String, Integer> getTestClassesRating(
            final MutoResult[] resultArray) {
        SortedMap<String, Integer> result = new TreeMap<String, Integer>();
        for (MutoResult item : resultArray) {
            if (item.getResults() == null
                    || item.getResults().size() == 0) {
                continue;
            }
            for (JUnitTestSuite suite:item.getResults()) {
                int currentRating = suite.getErrors() + suite.getFailures();
                String suiteName = suite.getName();
                if (result.containsKey("" + suiteName)) {
                    currentRating += result.get(suiteName);
                }
                if (suiteName != null) {
                    result.put(suiteName, currentRating);
                }
            }
        }
        return result;
    }

    /**
     * .
     * @param resultArray .
     * @return .
     */
    @SuppressWarnings("unchecked")
    public final Map<String, Integer> getTestCasesRating(
            final MutoResult[] resultArray) {
        SortedMap<String, Integer> result = new TreeMap<String, Integer>();
        for (MutoResult item : resultArray) {
            if (item.getResults() == null || item.getResults().size() == 0) {
                continue;
            }
            for (JUnitTestSuite suite:item.getResults()) {
                for (JUnitTestCase test: suite.getTestCases()) {
                    String testName = suite.getName() + " " + test.getName();
                    int rating = 0;
                    if (result.containsKey(testName)) {
                        rating = result.get(testName);
                    }
                    if (test.getError() != null || test.getFailure() != null) {
                        rating++;
                    }
                    result.put(testName, rating);
                }
            }
        }
        //return (Map<String, Integer>) entriesSortedByValues(result,false);
        return result;
    }

    /**
     * .
     * @param resultArray .
     * @return .
     */
    public final Map<String, Integer> getMutationRulesStats(
            final MutoResult[] resultArray) {
        SortedMap<String, Integer> result = new TreeMap<String, Integer>();
        for (MutoResult item : resultArray) {
            String ruleName = item.getLocation().getRuleName();
            int count = 0;
            if (result.containsKey(ruleName)) {
                count = result.get(ruleName);
            }
            for (JUnitTestSuite suite : item.getResults()) {
                count += suite.getErrors() + suite.getFailures();
            }
            result.put(ruleName, count);
        }
        return result;
    }
}

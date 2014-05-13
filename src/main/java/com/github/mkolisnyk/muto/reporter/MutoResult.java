package com.github.mkolisnyk.muto.reporter;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXB;

import org.apache.commons.io.FileUtils;

import com.github.mkolisnyk.muto.data.MutationLocation;
import com.github.mkolisnyk.muto.reporter.result.JUnitTestSuite;

/**
 * .
 * @author Myk Kolisnyk
 *
 */
public class MutoResult {
    /**
     * .
     */
    private String outputLocation;
    /**
     * .
     */
    private String testReportsLocation;
    /**
     * .
     */
    private List<JUnitTestSuite> results;
    /**
     * .
     */
    private int exitCode;
    /**
     * .
     */
    private MutationLocation location;
    /**
     * .
     * @param reportsLocation .
     */
    public MutoResult(final String reportsLocation) {
        this.testReportsLocation = reportsLocation;
    }
    /**
     * .
     * @return .
     */
    public String getOutputLocation() {
        return outputLocation;
    }
    /**
     * .
     * @param outputLocation .
     */
    public void setOutputLocation(String outputLocation) {
        this.outputLocation = outputLocation;
    }
    /**
     * .
     * @return .
     */
    public final String getTestReportsLocation() {
        return testReportsLocation;
    }
    /**
     * .
     * @return .
     */
    public final List<JUnitTestSuite> getResults() {
        return results;
    }
    /**
     * .
     * @return .
     */
    public final int getExitCode() {
        return exitCode;
    }
    /**
     * .
     * @param exitCodeValue .
     */
    public final void setExitCode(final int exitCodeValue) {
        this.exitCode = exitCodeValue;
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
     * .
     */
    public final void retrieveResults() {
        results = new ArrayList<JUnitTestSuite>();
        Iterator<File> iter = FileUtils.iterateFiles(new File(
                this.testReportsLocation), new String[] {"xml"},
                true);
        while (iter.hasNext()) {
            String file = iter.next().getAbsolutePath();
            JUnitTestSuite suite = retrieveResult(file);
            if (suite.getErrors() > 0 || suite.getFailures() > 0) {
                results.add(suite);
            }
        }
    }
    /**
     * .
     * @param file .
     * @return .
     */
    public final JUnitTestSuite retrieveResult(final String file) {
        return JAXB.unmarshal(file, JUnitTestSuite.class);
    }
}

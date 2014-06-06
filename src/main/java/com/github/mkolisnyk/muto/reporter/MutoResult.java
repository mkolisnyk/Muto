package com.github.mkolisnyk.muto.reporter;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXB;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.commons.io.FileUtils;

import com.github.mkolisnyk.muto.data.MutationLocation;
import com.github.mkolisnyk.muto.reporter.result.JUnitTestSuite;

/**
 * .
 * @author Myk Kolisnyk
 *
 */
@XmlRootElement(name = "mutoResult")
public class MutoResult {
    /**
     * .
     */
    public static final int PASSED = 0;
    /**
     * .
     */
    public static final int FAILED = 1;
    /**
     * .
     */
    public static final int ERRORRED = 2;
    /**
     * .
     */
    public static final int UNDEFINED = 3;

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
    @XmlElement(name = "results")
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
     */
    public MutoResult() {
        this("");
    }
    /**
     * .
     * @return .
     */
    public final String getOutputLocation() {
        return outputLocation;
    }
    /**
     * .
     * @param outputLocationValue .
     */
    public final void setOutputLocation(final String outputLocationValue) {
        this.outputLocation = outputLocationValue;
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
    @XmlTransient
    public final List<JUnitTestSuite> getResults() {
        return results;
    }

    /**
     * @param resultsValue the results to set
     */
    public final void setResults(final List<JUnitTestSuite> resultsValue) {
        this.results = resultsValue;
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
    @SuppressWarnings("unchecked")
    public final void retrieveResults() {
        results = new ArrayList<JUnitTestSuite>();
        Iterator<File> iter = FileUtils.iterateFiles(new File(
                this.testReportsLocation), new String[] {"xml"},
                false);
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

    /**
     * .
     * @return .
     */
    private int getStatus() {
        if (this.results == null || this.results.size() == 0) {
            if (this.getExitCode() != 0) {
                return FAILED;
            } else {
                return UNDEFINED;
            }
        }
        int errors = 0;
        for (JUnitTestSuite suite : results) {
            errors = suite.getErrors() + suite.getFailures();
        }
        if (errors > 0) {
            return ERRORRED;
        } else {
            return PASSED;
        }
    }

    /**
     * .
     * @return .
     */
    public final boolean isPassed() {
        return this.getStatus() == PASSED;
    }

    /**
     * .
     * @return .
     */
    public final boolean isFailed() {
        return this.getStatus() == FAILED;
    }

    /**
     * .
     * @return .
     */
    public final boolean isErrorred() {
        return this.getStatus() == ERRORRED;
    }

    /**
     * .
     * @return .
     */
    public final boolean isUndefined() {
        return this.getStatus() == UNDEFINED;
    }
}

package com.github.mkolisnyk.muto.reporter.result;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
/**
 * @author Myk Kolisnyk
 */
@XmlRootElement(name = "testsuite")
public class JUnitTestSuite {
    /**
     * .
     */
    @XmlAttribute
    private int failures;
    /**
     * .
     */
    @XmlAttribute
    private float time;
    /**
     * .
     */
    @XmlAttribute
    private int errors;
    /**
     * .
     */
    @XmlAttribute
    private int skipped;
    /**
     * .
     */
    @XmlAttribute
    private int tests;
    /**
     * .
     */
    @XmlAttribute
    private String name;
    /**
     * .
     */
    @XmlElement(name = "testcase")
    private JUnitTestCase[] testCases;
    /**
     * .
     * @return .
     */
    public final int getFailures() {
        return failures;
    }
    /**
     * .
     * @return .
     */
    public final float getTime() {
        return time;
    }
    /**
     * .
     * @return .
     */
    public final int getErrors() {
        return errors;
    }
    /**
     * .
     * @return .
     */
    public final int getSkipped() {
        return skipped;
    }
    /**
     * .
     * @return .
     */
    public final int getTests() {
        return tests;
    }
    /**
     * .
     * @return .
     */
    public final String getName() {
        return name;
    }
    /**
     * .
     * @return .
     */
    @XmlTransient
    public final JUnitTestCase[] getTestCases() {
        if (testCases == null) {
            return new JUnitTestCase[] {};
        }
        return testCases.clone();
    }
    /**
     * .
     * @param newTestCases .
     */
    public final void setTestCases(final JUnitTestCase[] newTestCases) {
        this.testCases = newTestCases.clone();
    }
}

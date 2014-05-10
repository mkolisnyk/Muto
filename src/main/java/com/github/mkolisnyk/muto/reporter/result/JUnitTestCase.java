package com.github.mkolisnyk.muto.reporter.result;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
/**
 * @author Myk Kolisnyk
 */
@XmlRootElement(name = "testcase")
public class JUnitTestCase {
    /**
     * .
     */
    @XmlAttribute
    private float time;
    /**
     * .
     */
    @XmlAttribute
    private String classname;
    /**
     * .
     */
    @XmlAttribute
    private String name;
    /**
     * .
     */
    @XmlElement(name = "error")
    private JUnitError error;
    /**
     * .
     */
    @XmlElement(name = "failure")
    private JUnitFailure failure;
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
    public final String getClassname() {
        return classname;
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
    public final JUnitError getError() {
        return error;
    }
    /**
     * .
     * @return .
     */
    public final JUnitFailure getFailure() {
        return failure;
    }
    /**
     * .
     * @param timeValue .
     */
    @XmlTransient
    public final void setTime(final float timeValue) {
        this.time = timeValue;
    }
    /**
     * .
     * @param classnameValue .
     */
    @XmlTransient
    public final void setClassname(final String classnameValue) {
        this.classname = classnameValue;
    }
    /**
     * .
     * @param nameValue .
     */
    @XmlTransient
    public final void setName(final String nameValue) {
        this.name = nameValue;
    }
    /**
     * .
     * @param errorValue .
     */
    @XmlTransient
    public final void setError(final JUnitError errorValue) {
        this.error = errorValue;
    }
    /**
     * .
     * @param failureValue .
     */
    @XmlTransient
    public final void setFailure(final JUnitFailure failureValue) {
        this.failure = failureValue;
    }
}

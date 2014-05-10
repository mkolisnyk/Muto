package com.github.mkolisnyk.muto.reporter.result;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;
/**
 * @author Myk Kolisnyk
 */
public class JUnitFailure {
    /**
     * .
     */
    @XmlAttribute
    private String type;
    /**
     * .
     */
    @XmlValue
    private String text;
    /**
     * .
     * @return .
     */
    public final String getType() {
        return type;
    }
    /**
     * .
     * @return .
     */
    public final String getText() {
        return text;
    }
}

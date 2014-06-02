package com.github.mkolisnyk.muto.reporter;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Myk Kolisnyk
 */
@XmlRootElement(name = "mutoResultsSet")
public class MutoResultSet {
    /**
     * .
     */
    private MutoResult[] resultList;

    /**
     * @return the results
     */
    public final MutoResult[] getResultList() {
        return resultList.clone();
    }

    /**
     * @param resultsValue the results to set
     */
    public final void setResultList(final MutoResult[] resultsValue) {
        this.resultList = resultsValue.clone();
    }
}

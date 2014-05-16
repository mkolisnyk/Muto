package com.github.mkolisnyk.muto.reporter.listeners;

import java.io.File;

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
        System.out.println(outputFile);
        /*FileWriter writer = null;
        try {
            writer = new FileWriter(new File(outputFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
        JAXB.marshal(result, writer);*/
    }
}

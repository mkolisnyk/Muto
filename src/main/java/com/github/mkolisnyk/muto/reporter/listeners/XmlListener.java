package com.github.mkolisnyk.muto.reporter.listeners;

import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.JAXB;

import org.junit.Assert;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.SimpleLayout;

import com.github.mkolisnyk.muto.data.MutationLocation;
import com.github.mkolisnyk.muto.reporter.MutoListener;
import com.github.mkolisnyk.muto.reporter.MutoResult;
import com.github.mkolisnyk.muto.reporter.MutoResultSet;

/**
 * @author Myk Kolisnyk
 */
public class XmlListener implements MutoListener {
    /**
     * .
     */
    private static Logger log = Logger.getLogger(ConsoleListener.class);

    static {
        log.addAppender(new ConsoleAppender(new SimpleLayout()));
    }

    /**
     * .
     */
    private MutationLocation latestLocation;
    /**
     * .
     */
    private String outputLocation;
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
     * @throws Exception .
     */
    public final void afterSuiteRun() throws Exception {
        String finalReport = this.outputLocation + File.separator
                + "muto_total.xml";
        File finalReportFile = new File(finalReport);
        while (finalReportFile.exists()) {
            Assert.assertTrue("Unable to devele file: "
                                + finalReportFile.getAbsolutePath(),
                    finalReportFile.delete());
        }
        List<MutoResult> results = new LinkedList<MutoResult>();
        File output = new File(this.outputLocation);
        log.debug("Final output: " + finalReport);
        log.debug("Reports location: " + output.getAbsolutePath());
        Iterator<File> iter = FileUtils.iterateFiles(
                output.getAbsoluteFile(), new String[] {"xml"},
                true);
        while (iter.hasNext()) {
            File file = iter.next().getAbsoluteFile();
            if (file.getName().equals("muto_total.xml")) {
                continue;
            }
            log.debug("Processing file: " + file.getAbsolutePath());
            MutoResult result = new MutoResult();
            result = JAXB.unmarshal(file, MutoResult.class);
            log.debug("Adding result to results list");
            results.add(result);
        }
        log.debug("Generating final output. Total results: "
                + results.size());
        MutoResult[] resultArray = new MutoResult[results.size()];
        for (int i = 0; i < results.size(); i++) {
            resultArray[i] = results.get(i);
        }
        MutoResultSet resultSet = new MutoResultSet();
        resultSet.setResultList(resultArray);
        JAXB.marshal(resultSet, finalReportFile);
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
     * @throws Exception .
     */
    public final void afterTestRun(final MutoResult result) throws Exception {
        File output = new File(result.getOutputLocation());
        this.outputLocation = output.getAbsolutePath();
        String outputFile =
               output.getAbsolutePath()
                + File.separator + "muto_result_" + testCount + ".xml";
        if (!output.getAbsoluteFile().exists()) {
            Assert.assertTrue(output.mkdirs());
        }
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
        if (this.latestLocation == null) {
            this.latestLocation = new MutationLocation(0, 0);
        }
        this.latestLocation.setFileName(fileName);
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
        this.latestLocation = location;
    }
}

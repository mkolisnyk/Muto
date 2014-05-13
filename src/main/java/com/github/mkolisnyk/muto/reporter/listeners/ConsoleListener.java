package com.github.mkolisnyk.muto.reporter.listeners;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.SimpleLayout;

import com.github.mkolisnyk.muto.reporter.MutoListener;
import com.github.mkolisnyk.muto.reporter.MutoResult;

/**
 * @author Myk Kolisnyk
 */
public class ConsoleListener implements MutoListener {
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
    public void beforeSuiteRun() {
        log.info("Starting suite");
    }

    /**
     * .
     */
    public void afterSuiteRun() {
        log.info("Suite completed");
    }

    /**
     * .
     */
    public void beforeTestRun() {
        log.info("Start test");
    }

    /**
     * .
     */
    public void afterTestRun(MutoResult result) {
        log.info(String
                .format("Test is done. Exit code: %d. Test Suites changed: %d",
                        result.getExitCode(), result.getResults()
                                .size()));
    }
}

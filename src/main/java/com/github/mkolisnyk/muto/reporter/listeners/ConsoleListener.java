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
    public final void beforeSuiteRun() {
        log.info("Starting suite");
    }

    /**
     * .
     */
    public final void afterSuiteRun() {
        log.info("Suite completed");
    }

    /**
     * .
     */
    public final void beforeTestRun() {
        log.info("Start test");
    }

    /**
     * .
     * @param result .
     */
    public final void afterTestRun(final MutoResult result) {
        log.info(String
                .format("Test is done. Exit code: %0$d. "
                        + "Test Suites changed: TBD",
                        result.getExitCode()));
    }
}

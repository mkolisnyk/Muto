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

    /**
     * .
     */
    public final void beforeFileStrategyRun() {
        log.debug("Entering file processing strategy");
    }

    /**
     * .
     */
    public final void afterFileStrategyRun() {
        log.debug("Exiting file processing strategy");
    }

    /**
     * .
     */
    public final void beforeMutationStrategyRun() {
        log.debug("Entering mutation strategy");
    }

    /**
     * .
     */
    public final void afterMutationStrategyRun() {
        log.debug("Exiting mutation strategy");
    }

    /**
     * .
     */
    public final void beforeMutationRuleRun() {
        log.debug("Applying mutation");
    }

    /**
     * .
     */
    public final void afterMutationRuleRun() {
        log.debug("Restoring mutation");
    }
}

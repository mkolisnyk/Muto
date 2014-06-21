package com.github.mkolisnyk.muto.reporter.listeners;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Layout;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

import com.github.mkolisnyk.muto.data.MutationLocation;
import com.github.mkolisnyk.muto.reporter.MutoListener;
import com.github.mkolisnyk.muto.reporter.MutoResult;

/**
 * @author Myk Kolisnyk
 */
public class ConsoleListener implements MutoListener {
    /**
     * .
     */
    private static final String LAYOUT_PATTERN
                    = "%d\t%-5p\t%m%n";
    /**
     * .
     */
    private static final Layout LOG4NET_LAYOUT
                    = new PatternLayout(LAYOUT_PATTERN);

    /**
     * .
     */
    private static Logger log = Logger.getLogger(ConsoleListener.class);

    static {
        log.addAppender(new ConsoleAppender(LOG4NET_LAYOUT));
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
     * @param fileName .
     */
    public final void afterFileStrategyRun(final String fileName) {
        log.debug("Processed file: " + fileName);
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
     * @param location .
     */
    public final void afterMutationRuleRun(final MutationLocation location) {
        log.debug("Mutation was applied to " + location);
    }
}

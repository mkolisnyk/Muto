package com.github.mkolisnyk.muto;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Layout;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

/**
 * @author Myk Kolisnyk
 */
public final class Log {
    /**
     * .
     */
    private Logger logger;
    /**
     * .
     */
    private static Log instance;
    /**
     * .
     */
    private static final String LAYOUT_PATTERN = "%d\t%-5p\t%m%n";
    /**
     * .
     */
    private static final Layout LOG4NET_LAYOUT
                            = new PatternLayout(LAYOUT_PATTERN);

    /**
     * .
     */
    private Log() {
        logger = Logger.getLogger(Log.class);
        logger.addAppender(new ConsoleAppender(LOG4NET_LAYOUT));
    }

    /**
     * .
     * @return .
     */
    public static Logger get() {
        if (instance == null) {
            instance = new Log();
        }
        return instance.logger;
    }

    /**
     * @param message .
     * @see org.apache.log4j.Category#debug(java.lang.Object)
     */
    public static void debug(final Object message) {
        get().debug(message);
    }

    /**
     * @param message .
     * @param t .
     * @see org.apache.log4j.Category
     *          #error(java.lang.Object, java.lang.Throwable)
     */
    public static void error(final Object message, final Throwable t) {
        get().error(message, t);
    }

    /**
     * @param message .
     * @see org.apache.log4j.Category#error(java.lang.Object)
     */
    public static void error(final Object message) {
        get().error(message);
    }

    /**
     * @param message .
     * @see org.apache.log4j.Category#info(java.lang.Object)
     */
    public static void info(final Object message) {
        get().info(message);
    }
}

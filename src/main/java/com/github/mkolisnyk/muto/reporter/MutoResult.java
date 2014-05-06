package com.github.mkolisnyk.muto.reporter;

import com.github.mkolisnyk.muto.data.MutationLocation;

/**
 * .
 * @author Myk Kolisnyk
 *
 */
public class MutoResult {
    /**
     * .
     */
    private int exitCode;
    /**
     * .
     */
    private MutationLocation location;
    /**
     * .
     * @return .
     */
    public final int getExitCode() {
        return exitCode;
    }
    /**
     * .
     * @param exitCodeValue .
     */
    public final void setExitCode(final int exitCodeValue) {
        this.exitCode = exitCodeValue;
    }
    /**
     * .
     * @return .
     */
    public final MutationLocation getLocation() {
        return location;
    }
    /**
     * .
     * @param locationValue .
     */
    public final void setLocation(final MutationLocation locationValue) {
        this.location = locationValue;
    }
}

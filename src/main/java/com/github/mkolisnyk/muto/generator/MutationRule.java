package com.github.mkolisnyk.muto.generator;

import com.github.mkolisnyk.muto.data.MutationLocation;

/**
 * .
 * @author Myk Kolisnyk
 *
 */
public abstract class MutationRule {
    /**
     * .
     */
    private int tick;
    /**
     * .
     */
    private MutationLocation location;

    /**
     * .
     */
    public MutationRule() {
        this.location = new MutationLocation();
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
     * @param newLocation .
     */
    public final void setLocation(final MutationLocation newLocation) {
        this.location = newLocation;
    }

    /**
     * .
     * @param input .
     * @param position .
     * @return .
     */
    public abstract String apply(String input, int position);

    /**
     * .
     * @param input .
     * @return .
     */
    public abstract int total(String input);

    /**
     * @param input .
     * @return .
     */
    public final String next(final String input) {
        return apply(input, tick++);
    }

    /**
     * @param input .
     * @return .
     */
    public final boolean hasNext(final String input) {
        return tick < total(input);
    }

    /**
     * .
     */
    public final void reset() {
        tick = 0;
    }
    /**
     * .
     * @return .
     */
    public final int getTick() {
        return tick;
    }
}

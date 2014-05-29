package com.github.mkolisnyk.muto.generator;

import java.util.ArrayList;
import java.util.List;

import com.github.mkolisnyk.muto.data.MutationLocation;
import com.github.mkolisnyk.muto.reporter.MutoListener;

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
    private List<MutoListener> listeners = new ArrayList<MutoListener>();
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
        this.location.setRule(this);
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
     * @return the listeners
     */
    public final List<MutoListener> getListeners() {
        return listeners;
    }
    /**
     * @param listenersValue the listeners to set
     */
    public final void setListeners(final List<MutoListener> listenersValue) {
        this.listeners = listenersValue;
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
    protected final String next(final String input) {
        return apply(input, tick++);
    }

    /**
     * .
     * @param input .
     * @return .
     */
    public final String doNext(final String input) {
        for (MutoListener listener : this.getListeners()) {
            listener.beforeMutationRuleRun();
        }
        String result = this.next(input);
        for (MutoListener listener : this.getListeners()) {
            listener.afterMutationRuleRun(this.getLocation());
        }
        return result;
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

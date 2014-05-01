package com.github.mkolisnyk.muto.generator;

/**
 * .
 * @author Myk Kolisnyk
 *
 */
public interface MutationRule {

    /**
     * .
     * @param input .
     * @param position .
     * @return .
     */
    String apply(String input, int position);
}

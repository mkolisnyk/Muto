package com.github.mkolisnyk.muto.generator.rules;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.github.mkolisnyk.muto.data.MutationLocation;
import com.github.mkolisnyk.muto.generator.MutationRule;

/**
 * @author Myk Kolisnyk
 */
public class NumberSignMutationRule extends MutationRule {
    /**
     * .
     * @param input .
     * @param position .
     * @return .
     */
    @Override
    public final String apply(final String input, final int position) {
        String result = input;
        Pattern pattern = Pattern.compile("([-]?)(\\d+)");
        Matcher matcher = pattern.matcher(input);
        for (int i = 0; i < position; i++) {
            if (!matcher.find(position)) {
                return result;
            }
        }
        if (matcher.find()) {
            MutationLocation location = new MutationLocation(matcher.start(),
                    matcher.end());
            this.setLocation(location);
            Integer value = new Integer(matcher.group(0));
            value = -1 * value;
            result = result.substring(0, location.getStartPosition())
                    + value
                    + result.substring(location.getEndPosition());
        }
        return result;
    }

    /**
     * .
     * @param input .
     * @return .
     */
    @Override
    public final int total(final String input) {
        int count = 0;
        Pattern pattern = Pattern.compile("([-]?)(\\d+)");
        Matcher matcher = pattern.matcher(input);
        while (matcher.find()) {
            count++;
        }
        return count;
    }
}

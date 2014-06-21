package com.github.mkolisnyk.muto.generator.rules;

import com.github.mkolisnyk.muto.data.MutationLocation;
import com.github.mkolisnyk.muto.generator.MutationRule;

/**
 * @author Myk Kolisnyk
 */
public class BlockCleanMutationRule extends MutationRule {

    /**
     * .
     * @param input .
     * @param position .
     * @return .
     */
    private int depthAtPosition(final String input, final int position) {
        int depth = 0;
        for (int i = 0; i <= position; i++) {
            if (input.charAt(i) == '{') {
                depth++;
            } else if (input.charAt(i) == '}') {
                depth--;
            }
        }
        return depth;
    }

    /**
     * .
     * @param input .
     * @return .
     */
    private int maxDepth(final String input) {
        int max = 0;
        for (int i = 0; i < input.length(); i++) {
            max = Math.max(max, depthAtPosition(input, i));
        }
        return max;
    }

    /**
     * .
     * @param input .
     * @param startPos .
     * @param depth .
     * @return .
     */
    private MutationLocation nextBlockOnLevel(final String input,
            final int startPos, final int depth) {
        MutationLocation location = null;
        int start = -1;
        int end = -1;
        for (int i = startPos; i < input.length(); i++) {
            if (depthAtPosition(input, i) == depth) {
                start = i;
                break;
            }
        }
        for (int i = start; i < input.length(); i++) {
            if (depthAtPosition(input, i) == depth - 1) {
                end = i - 1;
                break;
            }
        }
        if (end > start && start > 0) {
            location = new MutationLocation(start, end);
            location.setMatchedText(input.substring(start + 1,
                    end + 1));
        }
        return location;
    }

    /**
     * .
     * @param input .
     * @param position .
     * @return .
     */
    private String transform(final String input, final int position) {
        String result = input;
        int maxDepth = this.maxDepth(input);
        int depth = 1;

        MutationLocation location = new MutationLocation(-1, -1);
        for (int i = 0; i <= position; i++) {
            while (depth <= maxDepth) {
                location = this.nextBlockOnLevel(input,
                        location.getEndPosition() + 1, depth);
                if (location == null) {
                    location = new MutationLocation(-1, -1);
                    depth++;
                } else {
                    break;
                }
            }
        }

        //if (location != null) {
            result = result.substring(0,
                    location.getStartPosition() + 1)
                    + result.substring(location.getEndPosition() + 1);
            this.setLocation(location);
        //}
        return result;
    }

    @Override
    public final String apply(final String input, final int position) {
        String result = transform(input, position);
        return result;
    }

    @Override
    public final int total(final String input) {
        int count = 0;
        for (int i = 0; i < input.length(); i++) {
            if (input.toCharArray()[i] == '{') {
                count++;
            }
        }
        return count;
    }
}

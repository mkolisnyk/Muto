package com.github.mkolisnyk.muto.data;

/**
 * @author Myk Kolisnyk
 */
public class MutationLocation {
    /**
     * .
     */
    private int startPosition;
    /**
     * .
     */
    private int endPosition;
    /**
     * .
     */
    private String fileName;
    /**
     * .
     */
    private String matchedText;

    /**
     * .
     */
    public MutationLocation() {
        this.startPosition = 0;
        this.endPosition = 0;
        this.fileName = "";
    }

    /**
     * .
     * @param start .
     * @param end .
     */
    public MutationLocation(final int start, final int end) {
        this();
        this.startPosition = start;
        this.endPosition = end;
    }
    /**
     * .
     * @param start .
     * @param end .
     * @param fileNameValue .
     */
    public MutationLocation(final int start, final int end,String fileNameValue) {
        this();
        this.startPosition = start;
        this.endPosition = end;
        this.fileName = fileNameValue;
    }
    /**
     * .
     * @return .
     */
    public final int getStartPosition() {
        return startPosition;
    }
    /**
     * .
     * @return .
     */
    public final int getEndPosition() {
        return endPosition;
    }
    /**
     * .
     * @return .
     */
    public final String getFileName() {
        return fileName;
    }
    /**
     * .
     * @param fileNameValue .
     */
    public final void setFileName(final String fileNameValue) {
        this.fileName = fileNameValue;
    }

    /**
     * .
     * @return .
     */
    public final String getMatchedText() {
        return matchedText;
    }

    /**
     * .
     * @param newMatchedText .
     */
    public final void setMatchedText(final String newMatchedText) {
        this.matchedText = newMatchedText;
    }

    @Override
    public final boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (!obj.getClass().equals(this.getClass())) {
            return false;
        }
        MutationLocation compare = (MutationLocation) obj;
        return this.getStartPosition() == compare.getStartPosition()
                && this.getEndPosition() == compare.getEndPosition()
                && this.getFileName().equals(compare.getFileName());
    }

    @Override
    public final int hashCode() {
        return super.hashCode()
                + this.startPosition
                + this.endPosition;
    }

    @Override
    public final String toString() {
        return String.format("{%d, %d, \"%s\"}",
                this.getStartPosition(),
                this.getEndPosition(),
                this.getFileName());
    }
}

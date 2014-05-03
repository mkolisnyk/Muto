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
        return super.hashCode() + this.startPosition + this.endPosition;
    }
}

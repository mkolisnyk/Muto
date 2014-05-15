package com.github.mkolisnyk.muto.helpers;

import java.io.File;
import java.util.List;

/**
 * @author Myk Kolisnyk
 */
public class FilesContainer {
    /**
     * .
     */
    private String baseDirectory;
    /**
     * .
     */
    private List<String> includes;
    /**
     * .
     */
    private List<String> excludes;
    /**
     * .
     * @return .
     */
    public final List<String> getIncludes() {
        return includes;
    }
    /**
     * .
     * @param includes .
     */
    public final void setIncludes(final List<String> includes) {
        this.includes = includes;
    }
    /**
     * .
     * @return .
     */
    public final List<String> getExcludes() {
        return excludes;
    }
    /**
     * .
     * @param excludes .
     */
    public final void setExcludes(List<String> excludes) {
        this.excludes = excludes;
    }
    /**
     * .
     * @return .
     */
    public String getBaseDirectory() {
        return baseDirectory;
    }
    /**
     * .
     * @param rootDirectory .
     */
    public void setBaseDirectory(String rootDirectory) {
        this.baseDirectory = rootDirectory;
    }
    /**
     * .
     * @return .
     */
    final List<File> getActualFileList() {
        return null;
    }
}

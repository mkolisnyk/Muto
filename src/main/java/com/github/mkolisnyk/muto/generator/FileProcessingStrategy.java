package com.github.mkolisnyk.muto.generator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

/**
 * @author Myk Kolisnyk
 */
public abstract class FileProcessingStrategy {
    /**
     * .
     */
    private static final String BACKUP_EXT = ".bak";
    /**
     * .
     */
    private List<MutationStrategy> mutationStrategies;
    /**
     * .
     */
    private List<String> files;
    /**
     * .
     */
    public abstract void next();
    /**
     * .
     * @return .
     */
    public abstract boolean hasNext();
    /**
     * .
     * @param fileName .
     * @return .
     */
    public final String read(final String fileName) {
        try {
            File src = new File(fileName);
            restore(fileName);
            return FileUtils.readFileToString(src);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
    /**
     * .
     * @param fileName .
     * @param content .
     */
    public final void write(final String fileName, final String content) {
        try {
            File src = new File(fileName);
            File dest = new File(fileName + BACKUP_EXT);
            FileUtils.copyFile(src, dest);
            FileUtils.writeStringToFile(src, content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * .
     * @param fileName .
     */
    public final void restore(final String fileName) {
        File src = new File(fileName);
        File dest = new File(fileName + BACKUP_EXT);
        if (dest.exists()) {
            try {
                FileUtils.copyFile(dest, src);
                dest.delete();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * .
     * @return .
     */
    public final List<MutationStrategy> getMutationStrategies() {
        return mutationStrategies;
    }
    /**
     * .
     * @param mutationStrategiesList .
     */
    public final void setMutationStrategies(
            final List<MutationStrategy> mutationStrategiesList) {
        this.mutationStrategies = mutationStrategiesList;
    }
    /**
     * .
     * @param mutationStrategy .
     */
    public final void addMutationStrategy(
            final MutationStrategy mutationStrategy) {
        if (this.mutationStrategies == null) {
           this.mutationStrategies = new ArrayList<MutationStrategy>();
        }
        this.mutationStrategies.add(mutationStrategy);
    }
    /**
     * .
     * @return .
     */
    public final List<String> getFiles() {
        return files;
    }
    /**
     * .
     * @param fileList .
     */
    public final void setFiles(final List<String> fileList) {
        this.files = fileList;
    }
    /**
     * .
     */
    public final void resetStrategies() {
        for (MutationStrategy strategy:mutationStrategies) {
            strategy.reset();
        }
    }
}

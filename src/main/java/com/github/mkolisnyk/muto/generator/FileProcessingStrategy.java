package com.github.mkolisnyk.muto.generator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;

import com.github.mkolisnyk.muto.Log;
import com.github.mkolisnyk.muto.data.MutationLocation;
import com.github.mkolisnyk.muto.reporter.MutoListener;

/**
 * @author Myk Kolisnyk
 */
public abstract class FileProcessingStrategy {
    /**
     * .
     */
    private MutationLocation location = new MutationLocation();
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
    private List<MutoListener> listeners = new ArrayList<MutoListener>();
    /**
     * .
     */
    protected abstract void next();
    /**
     * .
     */
    public final void doNext() {
        for (MutoListener listener : this.getListeners()) {
            listener.beforeFileStrategyRun();
        }
        Log.debug("Switching to the next processing step");
        this.next();
        Log.debug("Processing step is done");
        for (MutoListener listener : this.getListeners()) {
            String fileName = "";
            if (this.getLocation() != null) {
                fileName = this.getLocation().getFileName();
            }
            listener.afterFileStrategyRun(fileName);
        }
    }
    /**
     * .
     * @return .
     */
    public abstract boolean hasNext();
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
    /**
     * .
     * @return .
     */
    public final List<MutoListener> getListeners() {
        return listeners;
    }
    /**
     * .
     * @param listenersValue .
     */
    public final void setListeners(final List<MutoListener> listenersValue) {
        this.listeners = listenersValue;
    }
    /**
     * .
     * @param fileName .
     * @return .
     */
    public final String read(final String fileName) {
        Log.debug(
                String.format("Reading contents of the file: '%s'",
                        fileName));
        try {
            File src = new File(fileName);
            Log.debug(
                    String.format(
                            "The full path is: '%s'",
                            src.getAbsolutePath()));
            restore(fileName);
            this.location.setFileName(fileName);
            return FileUtils.readFileToString(src);
        } catch (IOException e) {
            Log.error(
                    String.format(
                            "There was the problem reading '%s' file.",
                            fileName), e);
        }
        return "";
    }
    /**
     * .
     * @param fileName .
     * @param content .
     */
    public final void write(final String fileName, final String content) {
        Log.debug(String.format("Writing text to the file: '%s'", fileName));
        try {
            File src = new File(fileName);
            Log.debug(
                    String.format("Initial file: '%s'",
                            src.getAbsolutePath()));
            File dest = new File(fileName + BACKUP_EXT);
            Log.debug(
                    String.format("Backup file: '%s'",
                            dest.getAbsolutePath()));
            Log.debug("Make a backup copy");
            FileUtils.copyFile(src, dest);
            Log.debug("Update source file content");
            FileUtils.writeStringToFile(src, content);
        } catch (IOException e) {
            Log.error(
                    String.format("There was the problem writing '%s' file.",
                            fileName), e);
            Assert.fail(e.getLocalizedMessage());
        }
    }
    /**
     * .
     * @param fileName .
     */
    public final void restore(final String fileName) {
        File src = new File(fileName);
        File dest = new File(fileName + BACKUP_EXT);
        Log.debug(String.format("Initial file: '%s'", src.getAbsolutePath()));
        Log.debug(String.format("Backup file: '%s'", dest.getAbsolutePath()));
        if (dest.exists()) {
            try {
                Log.debug("Start restoring file");
                FileUtils.copyFile(dest, src);
                Log.debug("Remove backup copy");
                Assert.assertTrue(dest.delete());
            } catch (Throwable e) {
                Log.error(
                        String.format(
                                "There was the problem restoring '%s' file.",
                                fileName), e);
                Assert.fail(e.getLocalizedMessage());
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
            Log.debug(
                    "Mutation strategies weren't initialized."
                    + " Creating empty list.");
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
        Log.debug("Reset all available mutation strategies");
        for (MutationStrategy strategy:mutationStrategies) {
            strategy.reset();
        }
    }
}

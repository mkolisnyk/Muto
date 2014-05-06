package com.github.mkolisnyk.muto.processor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.github.mkolisnyk.muto.generator.FileProcessingStrategy;
import com.github.mkolisnyk.muto.reporter.MutoListener;
import com.github.mkolisnyk.muto.reporter.MutoResult;

/**
 * .
 * @author Myk Kolisnyk
 *
 */
public class MutoProcessor {
    /**
     * .
     */
    private static final int DEFAULTTIMEOUT = 600000;
    /**
     * .
     */
    private String sourceDirectory;
    /**
     * .
     */
    private String targetDirectory;
    /**
     * .
     */
    private String runCommand;
    /**
     * .
     */
    private String testReportsLocation;
    /**
     * .
     */
    private List<String> filesToProcess;
    /**
     * .
     */
    private List<FileProcessingStrategy> fileStrategies;
    /**
     * .
     */
    private List<MutoListener> listeners;
    /**
     * .
     * @return .
     */
    public final String getSourceDirectory() {
        return sourceDirectory;
    }
    /**
     * .
     * @param newSourceDirectory .
     */
    public final void setSourceDirectory(final String newSourceDirectory) {
        this.sourceDirectory = newSourceDirectory;
    }
    /**
     * .
     * @return .
     */
    public final String getTargetDirectory() {
        return targetDirectory;
    }
    /**
     * .
     * @param newTargetDirectory .
     */
    public final void setTargetDirectory(final String newTargetDirectory) {
        this.targetDirectory = newTargetDirectory;
    }
    /**
     * .
     * @return .
     */
    public final String getRunCommand() {
        return runCommand;
    }
    /**
     * .
     * @param newRunCommand .
     */
    public final void setRunCommand(final String newRunCommand) {
        this.runCommand = newRunCommand;
    }
    /**
     * .
     * @return .
     */
    public final String getTestReportsLocation() {
        return testReportsLocation;
    }
    /**
     * .
     * @param newTestReportsLocation .
     */
    public final void setTestReportsLocation(
            final String newTestReportsLocation) {
        this.testReportsLocation = newTestReportsLocation;
    }

    /**
     * .
     * @return .
     */
    public final List<String> getFilesToProcess() {
        return filesToProcess;
    }
    /**
     * .
     * @param newFilesToProcess .
     */
    public final void setFilesToProcess(final List<String> newFilesToProcess) {
        this.filesToProcess = newFilesToProcess;
    }
    /**
     * .
     * @return .
     */
    public final List<FileProcessingStrategy> getFileStrategies() {
        return fileStrategies;
    }
    /**
     * .
     * @param newFileStrategies .
     */
    public final void setFileStrategies(
            final List<FileProcessingStrategy> newFileStrategies) {
        this.fileStrategies = newFileStrategies;
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
     * @param listenersArray .
     */
    public final void setListeners(final List<MutoListener> listenersArray) {
        this.listeners = listenersArray;
    }
    /**
     * .
     */
    private void beforeSuite() {
        for (MutoListener listener:listeners) {
            listener.beforeSuiteRun();
        }
    }
    /**
     * .
     */
    private void afterSuite() {
        for (MutoListener listener:listeners) {
            listener.afterSuiteRun();
        }
    }
    /**
     * .
     */
    private void beforeTest() {
        for (MutoListener listener:listeners) {
            listener.beforeTestRun();
        }
    }
    /**
     * .
     * @param result .
     */
    private void afterTest(final MutoResult result) {
        for (MutoListener listener:listeners) {
            listener.afterTestRun(result);
        }
    }
    /**
     * Copies project files into working directory.
     * @throws IOException .
     */
    public final void copyWorkspace() throws IOException {
        File source = new File(this.sourceDirectory);
        File workspace = new File(this.targetDirectory);
        if (workspace.exists() && workspace.isDirectory()) {
            FileUtils.deleteDirectory(workspace);
        }
        // FileUtils.mkdir(targetDirectory);
        workspace.mkdirs();
        for (File copyItem : source.listFiles()) {
            File targetItem = new File(workspace + File.separator
                    + copyItem.getName());
            try {
                if (!copyItem.getAbsolutePath().equals(
                        workspace.getAbsolutePath())) {
                    if (copyItem.isDirectory()) {
                        FileUtils.copyDirectory(copyItem, targetItem);
                    } else {
                        // FileUtils.copyFile(copyItem, workspace);
                        Files.copy(copyItem.toPath(),
                                targetItem.toPath());
                    }
                }
            } catch (Exception e) {
                Exception ext = new Exception("Failed to copy: "
                        + copyItem.getAbsolutePath(), e);
                ext.printStackTrace();
            }
        }
    }
    /**
     * Performs code mutations and further processing.
     * @throws IOException .
     */
    public final void process() throws IOException {
        this.copyWorkspace();
        this.beforeSuite();
        for (FileProcessingStrategy fileStrategy:this.fileStrategies) {
            fileStrategy.setFiles(this.filesToProcess);
            while (fileStrategy.hasNext()) {
                this.beforeTest();
                fileStrategy.next();
                try {
                    int exitCode = -1;
                    MutoResult result = new MutoResult();
                    exitCode = this.runCommand();
                    result.setExitCode(exitCode);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                this.afterTest(null);
            }
        }
        this.afterSuite();
        this.cleanupWorkspace();
    }
    /**
     * Runs specific command and tracks the status code.
     * @return .
     * @throws IOException .
     * @throws InterruptedException .
     */
    public final int runCommand() throws IOException,
            InterruptedException {
        Runtime runtime = Runtime.getRuntime();
        Process process = runtime.exec(runCommand, null, new File(
                this.targetDirectory));
        process.wait(DEFAULTTIMEOUT);
        return process.exitValue();
    }
    /**
     * .
     */
    private static final int MAXTRIES = 3;
    /**
     * Removes working directory and all related resources.
     */
    public final void cleanupWorkspace() {
        File workspace = new File(this.targetDirectory);
        if (workspace.exists() && workspace.isDirectory()) {
            for (int i = 0; i < MAXTRIES; i++) {
                try {
                    FileUtils.deleteDirectory(workspace);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }
}

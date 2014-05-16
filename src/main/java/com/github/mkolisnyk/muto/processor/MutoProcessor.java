package com.github.mkolisnyk.muto.processor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.concurrent.TimeoutException;

import org.apache.commons.io.FileUtils;

import com.github.mkolisnyk.muto.data.MutationLocation;
import com.github.mkolisnyk.muto.generator.FileProcessingStrategy;
import com.github.mkolisnyk.muto.reporter.MutoListener;
import com.github.mkolisnyk.muto.reporter.MutoResult;

/**
 * .
 * @author Myk Kolisnyk
 *
 */
public class MutoProcessor {
    /*private static Logger logger = Logger.getLogger(MutoProcessor.class);

    static {
        logger.addAppender(new ConsoleAppender(new SimpleLayout()));
    }*/
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
    private String outputLocation;
    /**
     * .
     */
    private List<File> filesToProcess;
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
     * @param sourceDirectoryValue .
     */
    public final void setSourceDirectory(final String sourceDirectoryValue) {
        this.sourceDirectory = sourceDirectoryValue;
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
    public final String getOutputLocation() {
        return outputLocation;
    }
    /**
     * .
     * @param outputLocationValue .
     */
    public final void setOutputLocation(final String outputLocationValue) {
        this.outputLocation = outputLocationValue;
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
    public final List<File> getFilesToProcess() {
        return filesToProcess;
    }
    /**
     * .
     * @param newFilesToProcess .
     */
    public final void setFilesToProcess(final List<File> newFilesToProcess) {
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
        this.cleanupWorkspace();

        assert workspace.mkdirs();
        for (File copyItem : source.listFiles()) {
            File targetItem = new File(workspace + File.separator
                    + copyItem.getName());
            if (!copyItem.getAbsolutePath().equals(
                    workspace.getAbsolutePath())) {
                if (copyItem.isDirectory()) {
                    FileUtils.copyDirectory(copyItem, targetItem);
                } else {
                    Files.copy(copyItem.toPath(),
                            targetItem.toPath());
                }
            }
        }
    }
    /**
     * Performs code mutations and further processing.
     * @throws Exception .
     */
    public final void process() throws Exception {
        this.copyWorkspace();
        this.beforeSuite();
        for (FileProcessingStrategy fileStrategy:this.fileStrategies) {
            fileStrategy.setFiles(this.filesToProcess);
            while (fileStrategy.hasNext()) {
                this.beforeTest();
                fileStrategy.next();
                MutoResult result = new MutoResult(this.testReportsLocation);
                MutationLocation locationValue = fileStrategy.getLocation();
                result.setLocation(locationValue);
                result.setOutputLocation(this.outputLocation);
                try {
                    int exitCode = -1;
                    exitCode = this.runCommand();
                    result.setExitCode(exitCode);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                this.afterTest(result);
            }
        }
        this.afterSuite();
        this.cleanupWorkspace();
    }
    /**
     * .
     * @author Myk Kolisnyk
     */
    private static final class Worker extends Thread {
        /**
         * .
         */
        private final Process process;
        /**
         * .
         */
        private Integer exit;
        /**
         * .
         * @param processValue .
         */
        private Worker(final Process processValue) {
          this.process = processValue;
        }
        /**
         * .
         */
        public void run() {
          try {
            exit = process.waitFor();
          } catch (InterruptedException ignore) {
            return;
          }
        }
      }
    /**
     * Runs specific command and tracks the status code.
     * @return .
     * @throws Exception .
     */
    public final int runCommand() throws Exception {
        Runtime runtime = Runtime.getRuntime();
        Process process = runtime.exec(runCommand, null, new File(
                this.targetDirectory));
        Worker worker = new Worker(process);
        worker.start();
        try {
            worker.join(DEFAULTTIMEOUT);
            if (worker.exit != null) {
                return worker.exit;
            } else {
                throw new TimeoutException();
            }
        } catch (InterruptedException ex) {
            worker.interrupt();
            Thread.currentThread().interrupt();
            throw ex;
        } finally {
            process.destroy();
        }
    }

    /**
     * .
     */
    private static final int MAXTRIES = 10;
    /**
     * Removes working directory and all related resources.
     */
    public final void cleanupWorkspace() {
        File workspace = new File(this.targetDirectory);
        if (workspace.exists() && workspace.isDirectory()) {
            for (int i = 0; i < MAXTRIES; i++) {
                try {
                    FileUtils.deleteDirectory(workspace);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

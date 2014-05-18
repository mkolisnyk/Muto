package com.github.mkolisnyk.muto.processor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
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
        @Override
        public void run() {
          try {
            exit = process.waitFor();
          } catch (InterruptedException ignore) {
            return;
          }
        }
      }
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
    private List<String> excludes;
    /**
     * .
     */
    private List<String> includes;
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
    private List<File> processFiles;
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
     */
    private static final int MAXTRIES = 10;

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
     * @param result .
     */
    private void afterTest(final MutoResult result) {
        for (MutoListener listener:listeners) {
            listener.afterTestRun(result);
        }
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
    private void beforeTest() {
        for (MutoListener listener:listeners) {
            listener.beforeTestRun();
        }
    }
    /**
     * .
     * @param fileName .
     * @return .
     */
    private boolean isFileExcluded(final String fileName) {
        File file = new File(fileName);
        for (String excludeItem : this.excludes) {
            String absolutePath = file.getAbsolutePath();
            if (absolutePath.contains(excludeItem)
                    || absolutePath.equals(excludeItem)
            /* || absolutePath.matches(excludeItem) */) {
                return true;
            }
        }
        return false;
    }
    /**
     * .
     * @param rootDirectory .
     * @return .
     */
    public final List<String> getFilesToCopy(
            final String rootDirectory) {
        List<String> files = new ArrayList<String>();
        File root = new File(rootDirectory).getAbsoluteFile();
        String[] fileList = root.list();
        for (String fileListItem : fileList) {
            File item = new File(root + File.separator + fileListItem);
            if (isFileExcluded(item.getAbsolutePath())) {
                continue;
            }
            if (item.isDirectory()) {
                files.addAll(this.getFilesToCopy(item
                        .getAbsolutePath()));
            } else {
                files.add(item.getAbsolutePath());
            }
        }
        return files;
    }
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
    /**
     * Copies project files into working directory.
     * @throws IOException .
     */
    public final void copyWorkspace() throws IOException {
        File workspace = new File(this.targetDirectory);
        this.excludes.add(targetDirectory);
        this.cleanupWorkspace();
        String absSourceDirectory = new File(sourceDirectory)
                .getAbsolutePath();
        String absTargetDirectory = new File(targetDirectory)
                .getAbsolutePath();

        assert workspace.mkdirs();
        List<String> filesToCopy = this
                .getFilesToCopy(absSourceDirectory);
        for (String sourceFile : filesToCopy) {
            File source = new File(sourceFile);
            String fileToCopy = sourceFile.replace(
                    absSourceDirectory, absTargetDirectory);
            File file = new File(fileToCopy);
            if (!file.getParentFile().exists()) {
                assert file.getParentFile().mkdirs();
            }
            if (file.exists()) {
                continue;
            }
            Files.copy(source.toPath(),
                    new File(fileToCopy).toPath());
        }
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
     * @return .
     */
    public final List<File> getFilesToProcess() {
        return processFiles;
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
     * @return .
     */
    public final List<String> getIncludes() {
        return includes;
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
     * @return .
     */
    public final String getOutputLocation() {
        return outputLocation;
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
     * @return .
     */
    public final String getSourceDirectory() {
        return sourceDirectory;
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
     * @return .
     */
    public final String getTestReportsLocation() {
        return testReportsLocation;
    }
    /**
     * Performs code mutations and further processing.
     * @throws Exception .
     */
    public final void process() throws Exception {
        this.copyWorkspace();
        this.beforeSuite();
        for (FileProcessingStrategy fileStrategy:this.fileStrategies) {
            fileStrategy.setFiles(this.processFiles);
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
     * @param excludesValue .
     */
    public final void setExcludes(final List<String> excludesValue) {
        this.excludes = excludesValue;
    }
    /**
     * .
     * @param newFilesToProcess .
     */
    public final void setFilesToProcess(final List<File> newFilesToProcess) {
        this.processFiles = newFilesToProcess;
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
     * @param includesValue .
     */
    public final void setIncludes(final List<String> includesValue) {
        this.includes = includesValue;
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
     * @param outputLocationValue .
     */
    public final void setOutputLocation(final String outputLocationValue) {
        this.outputLocation = outputLocationValue;
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
     * @param sourceDirectoryValue .
     */
    public final void setSourceDirectory(final String sourceDirectoryValue) {
        this.sourceDirectory = sourceDirectoryValue;
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
     * @param newTestReportsLocation .
     */
    public final void setTestReportsLocation(
            final String newTestReportsLocation) {
        this.testReportsLocation = newTestReportsLocation;
    }
}

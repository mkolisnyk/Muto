package com.github.mkolisnyk.muto.processor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

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
    private List<String> excludes = new ArrayList<String>();
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
    private List<String> processFilesExclusions = new ArrayList<String>();
    /**
     * .
     */
    private List<String> processFilesIncludes = new ArrayList<String>();
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
     * @throws Exception .
     */
    private void afterSuite() throws Exception {
        for (MutoListener listener:listeners) {
            listener.afterSuiteRun();
        }
    }
    /**
     * .
     * @param result .
     * @throws Exception .
     */
    private void afterTest(final MutoResult result) throws Exception {
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
     * @param exclusions .
     * @return .
     */
    private boolean isFileExcluded(
            final String fileName,
            final List<String> exclusions) {
        File file = new File(fileName);
        for (String excludeItem : exclusions) {
            String absolutePath = file.getAbsolutePath();
            if (absolutePath.contains(excludeItem)
                    || absolutePath.matches(excludeItem)) {
                return true;
            }
        }
        return false;
    }
    /**
     * .
     * @param fileName .
     * @param inclusions .
     * @return .
     */
    private boolean isFileIncluded(
            final String fileName,
            final List<String> inclusions) {
        File file = new File(fileName);
        if (inclusions == null || inclusions.size() == 0) {
            return true;
        }
        for (String includeItem : inclusions) {
            String absolutePath = file.getAbsolutePath();
            if (absolutePath.contains(includeItem)
                    || absolutePath.matches(includeItem)) {
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
    private List<String> getFilesRecursively(final String rootDirectory) {
        List<String> files = new ArrayList<String>();
        File root = new File(rootDirectory).getAbsoluteFile();
        String[] fileList = root.list();
        for (String fileListItem : fileList) {
            File item = new File(root + File.separator + fileListItem);
            if (item.isDirectory()) {
                files.addAll(this.getFilesRecursively(item
                        .getAbsolutePath()));
            } else {
                files.add(item.getAbsolutePath());
            }
        }
        return files;
    }
    /**
     * .
     * @return .
     */
    public final List<String> getFilesToCopy() {
        String absSourceDirectory = new File(sourceDirectory)
                .getAbsolutePath();
        List<String> files = getFilesRecursively(absSourceDirectory);
        for (int i = 0; i < files.size(); i++) {
            if (this.isFileExcluded(files.get(i), this.excludes)) {
                files.remove(i);
                i--;
            }
        }
        return files;
    }
    /**
     * .
     * @return .
     */
    public final List<String> getFilesToProcess() {
        String absTargetDirectory = new File(targetDirectory)
                .getAbsolutePath();
        List<String> files = getFilesRecursively(absTargetDirectory);
        for (int i = 0; i < files.size(); i++) {
            if (this.isFileExcluded(files.get(i),
                    this.processFilesExclusions)
            || !this.isFileIncluded(files.get(i),
                    this.processFilesIncludes)) {
                files.remove(i);
                i--;
            }
        }
        return files;
    }
    /**
     * .
     * @param file .
     * @throws Exception .
     */
    private void deleteFile(final File file) throws Exception {
        if (!file.exists()) {
            return;
        }
        if (file.isDirectory()) {
            for (File item : file.listFiles()) {
                deleteFile(item);
            }
        }
        for (int i = 0; i < MAXTRIES; i++) {
            if (file.delete()) {
                break;
            }
        }
        if (file.exists()) {
            throw new IOException("Unable to delete file '"
                    + file.getAbsolutePath() + "'");
        }
    }
    /**
     * Removes working directory and all related resources.
     */
    public final void cleanupWorkspace() {
        File workspace = new File(this.targetDirectory);
        if (workspace.exists() && workspace.isDirectory()) {
            for (int i = 0; i < MAXTRIES; i++) {
                try {
                    deleteFile(workspace);
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
        this.excludes.add(targetDirectory.replace("\\", "\\\\"));
        this.cleanupWorkspace();
        String absSourceDirectory = new File(sourceDirectory)
                .getAbsolutePath();
        String absTargetDirectory = workspace.getAbsolutePath();
        if (!workspace.exists() && !workspace.mkdirs()) {
                throw new IOException(
                        "Failed to create workspace at: "
                                + workspace.getAbsolutePath());
        }
        List<String> filesToCopy = this.getFilesToCopy();
        for (String sourceFile : filesToCopy) {
            File source = new File(sourceFile);
            String fileToCopy = sourceFile.replace(
                    absSourceDirectory, absTargetDirectory);
            File file = new File(fileToCopy);
            if (!file.getAbsoluteFile().getParentFile().exists()
                && !file.getAbsoluteFile().getParentFile().mkdirs()) {
                throw new IOException("Failed to prepare target directory: "
                            + file.getAbsoluteFile().getParent());
            }
            Files.copy(source.toPath(), file.toPath());
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
    public final List<String> getProcessFiles() {
        return getFilesToProcess();
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
            fileStrategy.setFiles(this.getProcessFiles());
            while (fileStrategy.hasNext()) {
                this.beforeTest();
                fileStrategy.doNext();
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
                result.retrieveResults();
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
     * @param newFileStrategies .
     */
    public final void setFileStrategies(
            final List<FileProcessingStrategy> newFileStrategies) {
        this.fileStrategies = newFileStrategies;
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
    /**
     * .
     * @return .
     */
    public final List<String> getProcessFilesExclusions() {
        return processFilesExclusions;
    }
    /**
     * .
     * @param processFilesExclusionsValue .
     */
    public final void setProcessFilesExclusions(
            final List<String> processFilesExclusionsValue) {
        this.processFilesExclusions = processFilesExclusionsValue;
    }
    /**
     * @return the processFilesIncludes
     */
    public final List<String> getProcessFilesIncludes() {
        return processFilesIncludes;
    }
    /**
     * @param processFilesIncludesValue the processFilesIncludes to set
     */
    public final void setProcessFilesIncludes(
            final List<String> processFilesIncludesValue) {
        this.processFilesIncludes = processFilesIncludesValue;
    }
}

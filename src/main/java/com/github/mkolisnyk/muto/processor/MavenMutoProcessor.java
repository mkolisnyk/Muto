/**
 * .
 */
package com.github.mkolisnyk.muto.processor;

import java.util.ArrayList;
import java.util.List;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import com.github.mkolisnyk.muto.generator.FileProcessingStrategy;
import com.github.mkolisnyk.muto.generator.MutationRule;
import com.github.mkolisnyk.muto.generator.MutationStrategy;
import com.github.mkolisnyk.muto.reporter.MutoListener;



/**
 * @author Myk Kolisnyk
 */
@Mojo(name = "generate", defaultPhase = LifecyclePhase.VERIFY)
public class MavenMutoProcessor extends AbstractMojo {

    /**
     * .
     */
    @Parameter(property = "muto.target.directory",
               defaultValue = "target/muto/workspace")
    private String       targetDirectory;

    /**
     * .
     */
    @Parameter(property = "muto.source.directory", defaultValue = ".")
    private String sourceDirectory;
    /**
     * .
     */
    @Parameter
    private List<String> excludes;
    /**
     * .
     */
    @Parameter
    private List<String> fileStrategies;

    /**
     * .
     */
    @Parameter
    private List<String> mutationStrategies;

    /**
     * .
     */
    @Parameter
    private List<String> mutationRules;

    /**
     * .
     */
    @Parameter
    private List<String> listeners;

    /**
     * .
     */
    @Parameter(property = "muto.testreports.directory",
               defaultValue = "target/muto/workspace/target/surfire")
    private String       testReportsLocation;

    /**
     * .
     */
    @Parameter(property = "muto.run.command")
    private String runCommand;
    /**
     * @return the runCommand
     */
    public final String getRunCommand() {
        return runCommand;
    }
    /**
     * @param runCommandValue the runCommand to set
     */
    public final void setRunCommand(final String runCommandValue) {
        this.runCommand = runCommandValue;
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
    public final String getSourceDirectory() {
        return sourceDirectory;
    }
    /**
     * .
     * @return .
     */
    public final List<String> getFileStrategies() {
        return fileStrategies;
    }
    /**
     * .
     * @return .
     */
    public final List<String> getMutationStrategies() {
        return mutationStrategies;
    }
    /**
     * .
     * @return .
     */
    public final List<String> getListeners() {
        return listeners;
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
     * @param targetDirectoryValue .
     */
    public final void setTargetDirectory(final String targetDirectoryValue) {
        this.targetDirectory = targetDirectoryValue;
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
     * @param fileStrategiesValue .
     */
    public final void setFileStrategies(
            final List<String> fileStrategiesValue) {
        this.fileStrategies = fileStrategiesValue;
    }

    /**
     * .
     * @param mutationStrategiesValue .
     */
    public final void setMutationStrategies(
            final List<String> mutationStrategiesValue) {
        this.mutationStrategies = mutationStrategiesValue;
    }

    /**
     * .
     * @param listenersValue .
     */
    public final void setListeners(final List<String> listenersValue) {
        this.listeners = listenersValue;
    }

    /**
     * .
     * @param testReportsLocationValue .
     */
    public final void setTestReportsLocation(
            final String testReportsLocationValue) {
        this.testReportsLocation = testReportsLocationValue;
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
     * @param excludesValue .
     */
    public final void setExcludes(final List<String> excludesValue) {
        this.excludes = excludesValue;
    }
    /**
     * @return the mutationRules .
     */
    public final List<String> getMutationRules() {
        return mutationRules;
    }
    /**
     * @param mutationRulesValue the mutationRules to set
     */
    public final void setMutationRules(final List<String> mutationRulesValue) {
        this.mutationRules = mutationRulesValue;
    }
    /**
     * .
     * @return .
     * @throws Exception .
     */
    private List<MutoListener> initListeners() throws Exception {
        List<MutoListener> listenerObjects = new ArrayList<MutoListener>();
        if (this.listeners == null) {
            this.listeners = new ArrayList<String>();
            this.listeners
                    .add("com.github.mkolisnyk.muto.reporter.listeners."
                            + "DummyListener");
        }
        for (String item : this.listeners) {
            Class<?> clazz = Class.forName(item, true,
                    ClassLoader.getSystemClassLoader());
            MutoListener listener = (MutoListener) clazz
                    .newInstance();
            listenerObjects.add(listener);
        }
        return listenerObjects;
    }
    /**
     * .
     * @return .
     * @throws Exception .
     */
    private List<FileProcessingStrategy> initFileProcessingStrategies()
            throws Exception {
        List<FileProcessingStrategy> strategies
            = new ArrayList<FileProcessingStrategy>();
        if (this.fileStrategies == null) {
            this.fileStrategies = new ArrayList<String>();
            /*this.fileStrategies
                    .add("com.github.mkolisnyk.muto.generator."
                    + "filestrategies.OneByOneFileProcessingStrategy");*/
        }
        for (String item : this.fileStrategies) {
            Class<?> clazz = Class.forName(item, true,
                    ClassLoader.getSystemClassLoader());
            FileProcessingStrategy strategy = (FileProcessingStrategy) clazz
                    .newInstance();
            strategy.setListeners(initListeners());
            List<MutationStrategy> mutoStrategies = this
                    .initMutationStrategies();
            for (MutationStrategy mutationStrategy : mutoStrategies) {
                strategy.addMutationStrategy(mutationStrategy);
            }
            strategies.add(strategy);
        }
        return strategies;
    }
    /**
     * .
     * @return .
     * @throws Exception .
     */
    private List<MutationStrategy> initMutationStrategies()
            throws Exception {
        List<MutationStrategy> strategies = new ArrayList<MutationStrategy>();
        if (this.mutationStrategies == null) {
            this.mutationStrategies = new ArrayList<String>();
            this.mutationStrategies
                    .add("com.github.mkolisnyk.muto.generator.strategies."
                            + "OneByOneMutationStrategy");
        }
        List<MutoListener> listenerList = initListeners();
        for (String item : this.mutationStrategies) {
            Class<?> clazz = Class.forName(item, true,
                    ClassLoader.getSystemClassLoader());
            MutationStrategy strategy = (MutationStrategy) clazz
                    .newInstance();
            strategy.setListeners(listenerList);
            List<MutationRule> rules = this.initMutationRules();
            for (MutationRule rule : rules) {
                rule.setListeners(listenerList);
                strategy.addRule(rule);
            }
            strategies.add(strategy);
        }
        return strategies;
    }
    /**
     * .
     * @return .
     * @throws Exception .
     */
    private List<MutationRule> initMutationRules()
            throws Exception {
        List<MutationRule> rules = new ArrayList<MutationRule>();
        if (this.mutationRules == null) {
            this.mutationRules = new ArrayList<String>();
            this.mutationRules
                    .add("com.github.mkolisnyk.muto.generator.rules."
                            + "BlockCleanMutationRule");
            this.mutationRules
                    .add("com.github.mkolisnyk.muto.generator.rules."
                            + "NumberSignMutationRule");
        }
        for (String ruleItem : this.mutationRules) {
            Class<?> clazz = Class.forName(ruleItem, true,
                    ClassLoader.getSystemClassLoader());
            rules.add((MutationRule) clazz.newInstance());
        }
        return rules;
    }
    /**
     * .
     * @return .
     * @throws Exception .
     */
    public final MutoProcessor init() throws Exception {
        MutoProcessor processor = new MutoProcessor();
        processor.setTargetDirectory(targetDirectory);
        processor.setSourceDirectory(sourceDirectory);
        processor.setFileStrategies(initFileProcessingStrategies());
        processor.setTestReportsLocation(testReportsLocation);
        processor.setListeners(initListeners());
        processor.setRunCommand(runCommand);
        processor.setExcludes(excludes);
        return processor;
    }
    /**
     * .
     * @throws MojoExecutionException .
     * @throws MojoFailureException .
     * @see org.apache.maven.plugin.Mojo#execute()
     */
    public final void execute() throws MojoExecutionException,
            MojoFailureException {
        try {
            MutoProcessor processor = init();
            processor.process();
        } catch (Exception e) {
            e.printStackTrace();
            throw new MojoFailureException(e, "Execution failure",
                    "Maven Muto Processor failed to execute");
        }
    }
}

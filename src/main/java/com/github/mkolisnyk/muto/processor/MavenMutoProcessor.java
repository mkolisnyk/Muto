/**
 * .
 */
package com.github.mkolisnyk.muto.processor;

import java.io.File;
import java.util.List;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;


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
    private String       sourceDirectory;

    /**
     * .
     */
    @Parameter
    private List<File>   files;

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
    private List<String> listeners;

    /**
     * .
     */
    @Parameter(property = "muto.testreports.directory",
               defaultValue = "target/muto/workspace/target/surfire")
    private String       testReportsLocation;

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
     * @param filesValue .
     */
    public final void setFiles(final List<File> filesValue) {
        this.files = filesValue;
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
     * @throws MojoExecutionException .
     * @throws MojoFailureException .
     * @see org.apache.maven.plugin.Mojo#execute()
     */
    public final void execute() throws MojoExecutionException,
            MojoFailureException {
        MutoProcessor processor = new MutoProcessor();
        try {
            processor.process();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
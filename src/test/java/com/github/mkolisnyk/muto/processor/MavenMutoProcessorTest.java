package com.github.mkolisnyk.muto.processor;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.junit.Assert;
import org.junit.Test;

public class MavenMutoProcessorTest {

    private List<File> filesValue = new ArrayList<File>();
    private List<String> fileStrategiesValue = new ArrayList<String>();
    private List<String> listenersValue = new ArrayList<String>();
    private List<String> mutationStrategiesValue = new ArrayList<String>();
    private String sourceDirectoryValue = "";
    private String targetDirectoryValue = "target/muto/workspace";
    private String testReportsLocationValue = "";

    @Test
    public void testExecuteWithEmptyParameters() throws Exception {
        MavenMutoProcessor processor = new MavenMutoProcessor();
        targetDirectoryValue = (new File(targetDirectoryValue)).getAbsolutePath();
        sourceDirectoryValue = (new File(sourceDirectoryValue)).getAbsolutePath();
        processor.setFiles(filesValue);
        processor.setFileStrategies(fileStrategiesValue);
        processor.setListeners(listenersValue);
        processor.setMutationStrategies(mutationStrategiesValue);
        processor.setSourceDirectory(sourceDirectoryValue);
        processor.setTargetDirectory(targetDirectoryValue);
        processor.setTestReportsLocation(testReportsLocationValue );
        processor.execute();

        Assert.assertEquals(filesValue, processor.getFiles());
        Assert.assertEquals(fileStrategiesValue,processor.getFileStrategies());
        Assert.assertEquals(listenersValue,processor.getListeners());
        Assert.assertEquals(mutationStrategiesValue,processor.getMutationStrategies());
        Assert.assertEquals(sourceDirectoryValue,processor.getSourceDirectory());
        Assert.assertEquals(targetDirectoryValue,processor.getTargetDirectory());
        Assert.assertEquals(testReportsLocationValue,processor.getTestReportsLocation());
    }

    @Test
    public void testExecuteWithNullParameters() throws Exception {
        MavenMutoProcessor processor = new MavenMutoProcessor();
        targetDirectoryValue = (new File(targetDirectoryValue)).getAbsolutePath();
        sourceDirectoryValue = (new File(sourceDirectoryValue)).getAbsolutePath();
        processor.setFiles(null);
        processor.setFileStrategies(null);
        processor.setListeners(null);
        processor.setMutationStrategies(null);
        processor.setSourceDirectory(sourceDirectoryValue);
        processor.setTargetDirectory(targetDirectoryValue);
        processor.setTestReportsLocation(null);
        processor.execute();

        Assert.assertNull(processor.getFiles());
        Assert.assertNull(processor.getFileStrategies());
        Assert.assertNull(processor.getListeners());
        Assert.assertNull(processor.getMutationStrategies());
        Assert.assertEquals(sourceDirectoryValue,processor.getSourceDirectory());
        Assert.assertEquals(targetDirectoryValue,processor.getTargetDirectory());
        Assert.assertNull(processor.getTestReportsLocation());
    }

}

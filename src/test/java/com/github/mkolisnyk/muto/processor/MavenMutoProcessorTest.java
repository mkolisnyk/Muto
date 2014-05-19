package com.github.mkolisnyk.muto.processor;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

public class MavenMutoProcessorTest {
    private List<String> excludesValue = new ArrayList<String>();
    private List<String> fileStrategiesValue = new ArrayList<String>();
    private List<String> listenersValue = new ArrayList<String>();
    private List<String> mutationStrategiesValue = new ArrayList<String>();
    private String sourceDirectoryValue = "";
    private String targetDirectoryValue = "target/muto/workspace";
    private String testReportsLocationValue = "";

    @After
    public void tearDowm() {
        File target = new File(targetDirectoryValue);
        if(target.exists()) {
            target.delete();
        }
    }
    
    @Test
    public void testExecuteWithEmptyParameters() throws Exception {
        MavenMutoProcessor processor = new MavenMutoProcessor();
        targetDirectoryValue = (new File(targetDirectoryValue)).getAbsolutePath();
        sourceDirectoryValue = (new File(sourceDirectoryValue)).getAbsolutePath();
        excludesValue.add(".git");
        processor.setExcludes(excludesValue);
        processor.setFileStrategies(fileStrategiesValue);
        processor.setListeners(listenersValue);
        processor.setMutationStrategies(mutationStrategiesValue);
        processor.setSourceDirectory(sourceDirectoryValue);
        processor.setTargetDirectory(targetDirectoryValue);
        processor.setTestReportsLocation(testReportsLocationValue );
        processor.execute();

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
        excludesValue.add(".git");
        processor.setExcludes(excludesValue);
        processor.setFileStrategies(null);
        processor.setListeners(null);
        processor.setMutationStrategies(null);
        processor.setSourceDirectory(sourceDirectoryValue);
        processor.setTargetDirectory(targetDirectoryValue);
        processor.setTestReportsLocation(null);
        processor.execute();

        Assert.assertNotNull(processor.getFileStrategies());
        Assert.assertNotNull(processor.getListeners());
        Assert.assertNotNull(processor.getMutationStrategies());
        Assert.assertEquals(sourceDirectoryValue,processor.getSourceDirectory());
        Assert.assertEquals(targetDirectoryValue,processor.getTargetDirectory());
        Assert.assertNull(processor.getTestReportsLocation());
    }

}

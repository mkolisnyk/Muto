package com.github.mkolisnyk.muto.processor;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.github.mkolisnyk.muto.generator.filestrategies.OneByOneFileProcessingStrategy;
import com.github.mkolisnyk.muto.generator.rules.BlockCleanMutationRule;
import com.github.mkolisnyk.muto.generator.strategies.SingleSetMutationStrategy;
import com.github.mkolisnyk.muto.reporter.listeners.ConsoleListener;

public class MavenMutoProcessorTest {
    private List<String> excludesValue = new ArrayList<String>();
    private List<String> fileStrategiesValue = new ArrayList<String>();
    private List<String> listenersValue = new ArrayList<String>();
    private List<String> mutationStrategiesValue = new ArrayList<String>();
    private List<String> mutationRulesValue = new ArrayList<String>();
    private String sourceDirectoryValue = "";
    private String targetDirectoryValue = "target/muto/workspace";
    private String testReportsLocationValue = "";

    @Before
    public void setUp() {
        excludesValue = new ArrayList<String>();
        fileStrategiesValue = new ArrayList<String>();
        listenersValue = new ArrayList<String>();
        mutationStrategiesValue = new ArrayList<String>();
        mutationRulesValue = new ArrayList<String>();
    }
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
        excludesValue.add("\\.git");
        processor.setExcludes(excludesValue);
        processor.setFileStrategies(null);
        processor.setListeners(null);
        processor.setMutationStrategies(null);
        processor.setSourceDirectory(sourceDirectoryValue);
        processor.setTargetDirectory(targetDirectoryValue);
        processor.setTestReportsLocation(null);
        processor.setRunCommand("java -version");
        processor.execute();

        Assert.assertNotNull(processor.getFileStrategies());
        Assert.assertNotNull(processor.getListeners());
        Assert.assertNull(processor.getMutationStrategies());
        Assert.assertEquals(sourceDirectoryValue,processor.getSourceDirectory());
        Assert.assertEquals(targetDirectoryValue,processor.getTargetDirectory());
        Assert.assertNull(processor.getTestReportsLocation());
    }

    @Test
    public void testExecuteSimpleStrategies() throws Exception {
        MavenMutoProcessor processor = new MavenMutoProcessor();
        targetDirectoryValue = (new File(targetDirectoryValue)).getAbsolutePath();
        sourceDirectoryValue = (new File(sourceDirectoryValue)).getAbsolutePath();
        excludesValue.add("\\.git");
        excludesValue.add("target");
        excludesValue.add("src" + File.separator + "site");
        processor.setExcludes(excludesValue);
        fileStrategiesValue.add(OneByOneFileProcessingStrategy.class.getCanonicalName());
        processor.setFileStrategies(fileStrategiesValue);
        listenersValue.add(ConsoleListener.class.getCanonicalName());
        processor.setListeners(listenersValue);
        mutationStrategiesValue.add(SingleSetMutationStrategy.class.getCanonicalName());
        processor.setMutationStrategies(mutationStrategiesValue);
        mutationRulesValue.add(BlockCleanMutationRule.class.getCanonicalName());
        processor.setMutationRules(mutationRulesValue);
        processor.setSourceDirectory(sourceDirectoryValue);
        processor.setTargetDirectory(targetDirectoryValue);
        processor.setTestReportsLocation(testReportsLocationValue );
        processor.setRunCommand("java -version");
        processor.execute();

        Assert.assertEquals(fileStrategiesValue,processor.getFileStrategies());
        Assert.assertEquals(listenersValue,processor.getListeners());
        Assert.assertEquals(mutationStrategiesValue,processor.getMutationStrategies());
        Assert.assertEquals(sourceDirectoryValue,processor.getSourceDirectory());
        Assert.assertEquals(targetDirectoryValue,processor.getTargetDirectory());
        Assert.assertEquals(testReportsLocationValue,processor.getTestReportsLocation());
    }
}

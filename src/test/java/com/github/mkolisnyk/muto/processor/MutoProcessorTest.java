package com.github.mkolisnyk.muto.processor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.github.mkolisnyk.muto.generator.FileProcessingStrategy;
import com.github.mkolisnyk.muto.reporter.MutoListener;
import com.github.mkolisnyk.muto.reporter.MutoResult;
import com.github.mkolisnyk.muto.reporter.listeners.DummyListener;

public class MutoProcessorTest {

    private MutoProcessor processor;
    private File source;
    private File target;
    
    @Before
    public void setUp() {
        List<String> excludes = new ArrayList<String>();
        excludes.add(".git");
        processor = new MutoProcessor();
        processor.setExcludes(excludes);
        source = new File("");
        target = new File("target/muto/workspace");
        processor.setSourceDirectory(source.getAbsolutePath());
        processor.setTargetDirectory(target.getAbsolutePath());
    }
    
    @After
    public void tearDown() {
        if(target.exists() && target.isFile()) {
            target.delete();
        }
    }
    
    @Test
    public void testProcessorPrepareWorkspace() throws IOException{
        processor.copyWorkspace();
        Assert.assertTrue(target.exists());
        Assert.assertTrue(target.list().length > 0);
        processor.cleanupWorkspace();
        Assert.assertFalse(target.exists());
    }
    
    @Test
    public void testRunCommandPositiveTest() throws Exception {
        String command = "java -version";
        processor.setRunCommand("java -version");
        Assert.assertEquals(command,processor.getRunCommand());
        target = new File("target/muto/workspace");
        if (!target.exists()) {
           target.mkdirs(); 
        }
        Assert.assertEquals(0, processor.runCommand());
    }
    
    @Test
    public void testRunCommandInvalidCommandTest() throws Exception {
        processor.setRunCommand("java file");
        target = new File("target/muto/workspace");
        if (!target.exists()) {
           target.mkdirs();
        }
        Assert.assertEquals(1, processor.runCommand());
    }
    
    @Test
    public void testGetSetParameters() {
        processor.setTargetDirectory(target.getAbsolutePath());
        processor.setSourceDirectory(".");
        List<FileProcessingStrategy> newFileStrategies = new ArrayList<FileProcessingStrategy>();
        newFileStrategies.add(new FileProcessingStrategy() {
            
            @Override
            public void next() {
            }
            
            @Override
            public boolean hasNext() {
                return false;
            }
        });
        processor.setFileStrategies(newFileStrategies );
        String newTestReportsLocation = "src";
        processor.setTestReportsLocation(newTestReportsLocation );
        List<MutoListener> listenersArray = new ArrayList<MutoListener>();
        listenersArray.add(new DummyListener());
        processor.setListeners(listenersArray );
        Assert.assertEquals(target.getAbsolutePath(), processor.getTargetDirectory());
        Assert.assertEquals(".", processor.getSourceDirectory());
        Assert.assertEquals(newFileStrategies, processor.getFileStrategies());
        Assert.assertEquals(newTestReportsLocation, processor.getTestReportsLocation());
        Assert.assertEquals(listenersArray, processor.getListeners());
    }
    
    @Test
    public void testProcessVerifyListener() throws Exception {
        final List<String> logSteps = new ArrayList<String>();
        List<FileProcessingStrategy> newFileStrategies = new ArrayList<FileProcessingStrategy>();
        newFileStrategies.add(new FileProcessingStrategy() {
            private boolean flag = true;
            @Override
            public void next() {
                flag = false;
            }
            
            @Override
            public boolean hasNext() {
                return flag;
            }
        });
        processor.setFileStrategies(newFileStrategies );
        String newTestReportsLocation = "src";
        processor.setTestReportsLocation(newTestReportsLocation );
        List<MutoListener> listenersArray = new ArrayList<MutoListener>();
        listenersArray.add(new MutoListener() {
            public void beforeTestRun() {
                logSteps.add("Start Test Run");
            }
            
            public void beforeSuiteRun() {
                logSteps.add("Start Suite Run");
            }
            
            public void afterTestRun(MutoResult result) {
                logSteps.add("End Test Run");
            }
            
            public void afterSuiteRun() {
                logSteps.add("End Suite Run");
            }

            public void beforeFileStrategyRun() {
                // TODO Auto-generated method stub
                
            }

            public void afterFileStrategyRun() {
                // TODO Auto-generated method stub
                
            }

            public void beforeMutationStrategyRun() {
                // TODO Auto-generated method stub
                
            }

            public void afterMutationStrategyRun() {
                // TODO Auto-generated method stub
                
            }

            public void beforeMutationRuleRun() {
                // TODO Auto-generated method stub
                
            }

            public void afterMutationRuleRun() {
                // TODO Auto-generated method stub
                
            }
        });
        processor.setListeners(listenersArray );
        processor.setRunCommand("java -version");
        processor.process();
        Assert.assertEquals(4, logSteps.size());
        Assert.assertEquals("Start Suite Run", logSteps.get(0));
        Assert.assertEquals("Start Test Run", logSteps.get(1));
        Assert.assertEquals("End Test Run", logSteps.get(2));
        Assert.assertEquals("End Suite Run", logSteps.get(3));
    }
    
    @Test
    public void testCleanupWorkSpaceIfLastItemIsFile() throws IOException {
        processor.cleanupWorkspace();
        target.getParentFile().mkdirs();
        target.createNewFile();
        processor.cleanupWorkspace();
        Assert.assertTrue(target.exists());
    }
    
    @Test
    public void testGetFilesToCopy() {
        List<String> result = processor.getFilesToCopy();
        Assert.assertTrue(result.size() > 0);
        Assert.assertFalse(result.contains(target.getAbsolutePath()));
        
        for(String item:result) {
            File file = new File(item);
            Assert.assertTrue(file.exists());
        }
    }
    
    @Test
    public void testGetFilesToCopyWithRegexpExclusion() {
        String pattern = "(.*)\\.java";
        List<String> exclusions = processor.getExcludes();
        exclusions.add(pattern);
        processor.setExcludes(exclusions);

        List<String> result = processor.getFilesToCopy();
        Assert.assertTrue(result.size() > 0);
        Assert.assertFalse(result.contains(target.getAbsolutePath()));
        
        for(String item:result) {
            File file = new File(item);
            Assert.assertTrue(file.exists());
            Assert.assertFalse(
                    file.getAbsolutePath() + " wasn't excluded",
                    file.getAbsolutePath().matches(pattern));
        }
    }
    
    @Test
    public void testGetFilesToCopyWithPreciseExclusion() {
        String pattern = ".classpath";
        List<String> exclusions = processor.getExcludes();
        exclusions.add(pattern);
        processor.setExcludes(exclusions);
        
        Assert.assertTrue(new File(source.getAbsolutePath() + File.separator + pattern).exists());
        List<String> result = processor.getFilesToCopy();
        Assert.assertTrue(result.size() > 0);
        Assert.assertFalse(result.contains(target.getAbsolutePath()));
        
        for(String item:result) {
            File file = new File(item);
            Assert.assertTrue(file.exists());
            Assert.assertFalse(
                    file.getAbsolutePath() + " wasn't excluded",
                    file.getAbsolutePath().contains(pattern));
        }
    }
    @Test
    public void testGetFilesToProcess() throws IOException {
        processor.copyWorkspace();
        List<String> result = processor.getFilesToProcess();
        Assert.assertTrue(result.size() > 0);
        Assert.assertFalse(result.contains(target.getAbsolutePath()));
        
        for(String item:result) {
            File file = new File(item);
            Assert.assertTrue(file.getAbsolutePath().startsWith(target.getAbsolutePath()));
        }
    }
    
    @Test
    public void testGetFilesToProcessWithRegexpExclusion() throws IOException {
        processor.copyWorkspace();
        String pattern = "(.*)\\.java";
        List<String> exclusions = processor.getProcessFilesExclusions();
        exclusions.add(pattern);
        processor.setProcessFilesExclusions(exclusions);
        
        List<String> result = processor.getFilesToProcess();
        Assert.assertTrue(result.size() > 0);
        Assert.assertFalse(result.contains(target.getAbsolutePath()));
        
        for(String item:result) {
            File file = new File(item);
            Assert.assertTrue(file.getAbsolutePath().startsWith(target.getAbsolutePath()));
        }
        
        for(String item:result) {
            File file = new File(item);
            Assert.assertTrue(file.exists());
            Assert.assertFalse(
                    file.getAbsolutePath() + " wasn't excluded",
                    file.getAbsolutePath().equals(pattern));
        }
    }
}

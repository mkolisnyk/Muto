package com.github.mkolisnyk.muto.reporter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.github.mkolisnyk.muto.data.MutationLocation;
import com.github.mkolisnyk.muto.reporter.result.JUnitError;
import com.github.mkolisnyk.muto.reporter.result.JUnitTestCase;
import com.github.mkolisnyk.muto.reporter.result.JUnitTestSuite;

public class MutoResultTest {

    @Test
    public void testResultReadReports(){
        File path = new File("src/test/resources");
        MutoResult result = new MutoResult(path.getAbsolutePath());
        result.retrieveResults();
        List<JUnitTestSuite> results = result.getResults();
        String expectedSuites[] = {
                "com.github.mkolisnyk.muto.generator.MutationStrategyTest",
                "com.github.mkolisnyk.muto.generator.MutationStrategyTest",
                "com.github.mkolisnyk.muto.generator.MutationStrategyTest"
        };
        Assert.assertEquals(3, results.size());
        for(int i=0;i<results.size();i++) {
            for(int j=0;j<expectedSuites.length;j++){
                if(results.get(i).getName().equals(expectedSuites[j])) {
                    results.remove(i);
                    i--;
                    break;
                }
            }
        }
        Assert.assertEquals(0, results.size());
    }
    
    @Test
    public void testConstructorInitializesTestFolderName() {
        String folderName = "src/test/resources";
        MutoResult result = new MutoResult(folderName);
        Assert.assertEquals(folderName, result.getTestReportsLocation());
    }
    
    @Test
    public void testGetSetParameters() {
        String folderName = "src/test/resources";
        int exitCode = 1;
        MutationLocation location = new MutationLocation();
        MutoResult result = new MutoResult(folderName);
        result.setExitCode(exitCode);
        result.setLocation(location);
        Assert.assertEquals(folderName, result.getTestReportsLocation());
        Assert.assertEquals(exitCode, result.getExitCode());
        Assert.assertEquals(location, result.getLocation());
    }
    
    @Test
    public void testStatusPassed() {
        String folderName = "src/test/resources";
        int exitCode = 0;
        MutationLocation location = new MutationLocation();
        MutoResult result = new MutoResult(folderName);
        
        List<JUnitTestSuite> suites = new ArrayList<JUnitTestSuite>();
        suites.add(new JUnitTestSuite());
        result.setResults(suites);
        
        Assert.assertTrue(result.isPassed());
        Assert.assertFalse(result.isFailed());
        Assert.assertFalse(result.isUndefined());
        Assert.assertFalse(result.isErrorred());
    }

    @Test
    public void testStatusFailed() {
        String folderName = "src/test/resources";
        int exitCode = 1;
        MutoResult result = new MutoResult(folderName);
        
        List<JUnitTestSuite> suites = new ArrayList<JUnitTestSuite>();
        result.setResults(suites);
        result.setExitCode(exitCode);
        Assert.assertFalse(result.isPassed());
        Assert.assertTrue(result.isFailed());
        Assert.assertFalse(result.isUndefined());
        Assert.assertFalse(result.isErrorred());
    }

    @Test
    public void testStatusUndefined() {
        String folderName = "src/test/resources";
        int exitCode = 0;
        MutoResult result = new MutoResult(folderName);
        
        List<JUnitTestSuite> suites = new ArrayList<JUnitTestSuite>();
        result.setResults(suites);
        result.setExitCode(exitCode);
        Assert.assertFalse(result.isPassed());
        Assert.assertFalse(result.isFailed());
        Assert.assertTrue(result.isUndefined());
        Assert.assertFalse(result.isErrorred());
    }

    //@Test
    public void testStatusErrored() {
        String folderName = "src/test/resources";
        int exitCode = 0;
        MutoResult result = new MutoResult(folderName);
        
        List<JUnitTestSuite> suites = new ArrayList<JUnitTestSuite>();
        JUnitTestSuite suite = new JUnitTestSuite();
        JUnitTestCase testCase = new JUnitTestCase();
        testCase.setError(new JUnitError());
        suite.setTestCases(new JUnitTestCase[]{testCase});
        suites.add(suite);
        result.setResults(suites);
        result.setExitCode(exitCode);
        Assert.assertTrue(result.isErrorred());
    }
}

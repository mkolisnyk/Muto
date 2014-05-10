package com.github.mkolisnyk.muto.reporter;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.github.mkolisnyk.muto.data.MutationLocation;
import com.github.mkolisnyk.muto.reporter.result.JUnitTestSuite;

public class MutoResultTest {

    @Test
    public void testResultReadReports(){
        MutoResult result = new MutoResult("src/test/resources");
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
}

package com.github.mkolisnyk.muto.reporter.listeners;

import java.io.File;

import org.junit.Assert;
import org.junit.Test;

import com.github.mkolisnyk.muto.data.MutationLocation;
import com.github.mkolisnyk.muto.reporter.MutoResult;

public class XmlListenerTest {

    @Test
    public void testAfterTestRun() throws Exception {
        File reportsDir = new File("src/test/resources");
        File outputDir = new File("target/muto");
        File outputFile = new File(outputDir.getAbsolutePath() + File.separator + "muto_result_1.xml");
        outputDir.mkdirs();
        XmlListener listener = new XmlListener();
        MutoResult result = new MutoResult(reportsDir.getAbsolutePath());
        result.setOutputLocation(outputDir.getAbsolutePath());
        result.retrieveResults();
        MutationLocation location = new MutationLocation(2,10,"test_file");
        location.setMatchedText("Sample < matched text");
        result.setLocation(location);
        listener.beforeSuiteRun();
        listener.beforeTestRun();
        listener.beforeFileStrategyRun();
        listener.beforeMutationStrategyRun();
        listener.beforeMutationRuleRun();
        listener.afterMutationRuleRun(new MutationLocation());
        listener.afterMutationStrategyRun();
        listener.afterFileStrategyRun("Test");
        listener.afterTestRun(result);
        listener.afterSuiteRun();
        Assert.assertTrue(outputFile.exists());
    }

}

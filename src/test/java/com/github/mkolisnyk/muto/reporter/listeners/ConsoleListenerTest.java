package com.github.mkolisnyk.muto.reporter.listeners;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.github.mkolisnyk.muto.data.MutationLocation;
import com.github.mkolisnyk.muto.reporter.MutoResult;

public class ConsoleListenerTest {

    private ByteArrayOutputStream baos;
    private PrintStream ps;
    private PrintStream old;
    private ConsoleListener listener = new ConsoleListener();

    @Before
    public void setUp() {
        old = System.out;
        baos = new ByteArrayOutputStream();
        ps = new PrintStream(baos);
        System.setOut(ps);
    }
    
    @After
    public void finalizeTest() throws IOException {
        baos.reset();
        System.out.flush();
        System.setOut(old);
        ps.close();
        baos.close();
    }
    
    @Test
    public void testConsoleListener() {
        MutoResult result = new MutoResult(null);
        result.setExitCode(-1);
        
        listener.beforeSuiteRun();
        listener.beforeTestRun();
        listener.beforeFileStrategyRun();
        listener.beforeMutationStrategyRun();
        listener.beforeMutationRuleRun();
        listener.afterMutationRuleRun(new MutationLocation());
        listener.afterMutationStrategyRun();
        listener.afterFileStrategyRun("");
        listener.afterTestRun(result);
        listener.afterSuiteRun();
        //Assert.assertTrue(baos.toString().length() > 0);
    }
}

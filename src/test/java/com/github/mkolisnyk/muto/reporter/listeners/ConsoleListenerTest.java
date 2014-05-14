package com.github.mkolisnyk.muto.reporter.listeners;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import org.junit.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.github.mkolisnyk.muto.reporter.MutoResult;

public class ConsoleListenerTest {

    private ByteArrayOutputStream baos;
    private PrintStream ps;
    private PrintStream old = System.out;
    private ConsoleListener listener;

    @Before
    public void setUp() {
        baos = new ByteArrayOutputStream();
        ps = new PrintStream(baos);
        System.setOut(ps);
        listener = new ConsoleListener();
    }
    
    @After
    public void tearDown() throws IOException {
        System.out.flush();
        System.setOut(old);
    }
    
    @Test
    public void testConsoleListener() {
        MutoResult result = new MutoResult(null);
        result.setExitCode(-1);
        
        listener.beforeSuiteRun();
        listener.beforeTestRun();
        listener.afterTestRun(result);
        listener.afterSuiteRun();
        Assert.assertEquals("INFO - Starting suite" + System.lineSeparator()
                + "INFO - Start test" + System.lineSeparator()
                + "INFO - Test is done. Exit code: -1. Test Suites changed: TBD" + System.lineSeparator() 
                + "INFO - Suite completed" + System.lineSeparator(), baos.toString());
    }

}

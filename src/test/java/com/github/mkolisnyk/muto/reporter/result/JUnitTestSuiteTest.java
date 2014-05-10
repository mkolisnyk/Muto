package com.github.mkolisnyk.muto.reporter.result;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.github.mkolisnyk.muto.reporter.MutoResult;

public class JUnitTestSuiteTest {

    private JUnitTestSuite suite;
    
    @Before
    public void setUp() {
        MutoResult result = new MutoResult("");
        String file = ClassLoader.getSystemResource("TEST-1Error1FailureTest.xml").toString();
        suite = result.retrieveResult(file);
    }
    
    @Test
    public void testGetFailures() {
        Assert.assertEquals(1, suite.getFailures());
    }

    @Test
    public void testGetTime() {
        Assert.assertEquals(0.018, suite.getTime(),0.0001);
    }

    @Test
    public void testGetErrors() {
        Assert.assertEquals(1, suite.getErrors());
    }

    @Test
    public void testGetSkipped() {
        Assert.assertEquals(0, suite.getSkipped());
    }

    @Test
    public void testGetTests() {
        Assert.assertEquals(2, suite.getTests());
    }

    @Test
    public void testGetName() {
        Assert.assertEquals("com.github.mkolisnyk.muto.generator.MutationStrategyTest", suite.getName());
    }

    @Test
    public void testGetSetTestCases() {
        JUnitTestCase testCases[] = suite.getTestCases();
        testCases[0].setClassname("Empty");
        testCases[0].setName("TestName");
        testCases[0].setError(null);
        testCases[0].setFailure(null);
        testCases[0].setTime(0.01f);
        suite.setTestCases(testCases);
        Assert.assertArrayEquals(testCases, suite.getTestCases());
        Assert.assertEquals(suite.getTestCases()[0].getClassname(), "Empty");
        Assert.assertEquals(suite.getTestCases()[0].getName(), "TestName");
        Assert.assertEquals(suite.getTestCases()[0].getTime(), 0.01f, 0.001);
        Assert.assertNull(suite.getTestCases()[0].getError());
        Assert.assertNull(suite.getTestCases()[0].getFailure());
    }
    
    @Test
    public void testErrorFailureFields() {
        String expectedError = "java.lang.Exception: Test exception";
        String expectedErrorMessage = "Test exception";
        String expectedErrorType = "java.lang.Exception";
        String expectedFailureType = "java.lang.AssertionError";
        String expectedFailure = "java.lang.AssertionError";
        JUnitTestCase testCases[] = suite.getTestCases();
        JUnitTestCase testCase = testCases[0];
        Assert.assertNotNull(testCase.getError());
        Assert.assertEquals(expectedErrorMessage, testCase.getError().getMessage());
        Assert.assertEquals(expectedErrorType, testCase.getError().getType());
        Assert.assertTrue(testCase.getError().getText().contains(expectedError));
        testCase = testCases[1];
        Assert.assertNotNull(testCase.getFailure());
        Assert.assertEquals(expectedFailureType, testCase.getFailure().getType());
        Assert.assertTrue(testCase.getFailure().getText().contains(expectedFailure));
    }
}

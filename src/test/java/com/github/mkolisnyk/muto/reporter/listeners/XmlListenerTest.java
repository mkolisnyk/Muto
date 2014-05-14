package com.github.mkolisnyk.muto.reporter.listeners;

import org.junit.Assert;
import org.junit.Test;

import com.github.mkolisnyk.muto.reporter.MutoResult;

public class XmlListenerTest {

    @Test
    public void testAfterTestRun() {
        XmlListener listener = new XmlListener();
        MutoResult result = new MutoResult(null);
        listener.beforeSuiteRun();
        listener.beforeTestRun();
        listener.afterTestRun(result);
        listener.afterSuiteRun();
    }

}

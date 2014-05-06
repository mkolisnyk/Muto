package com.github.mkolisnyk.muto.processor;

import java.io.File;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MutoProcessorTest {

    private MutoProcessor processor;
    private File source;
    private File target;
    
    @Before
    public void setUp() {
        processor = new MutoProcessor();
        source = new File("");
        target = new File("target/muto/workspace");
        processor.setSourceDirectory(source.getAbsolutePath());
        processor.setTargetDirectory(target.getAbsolutePath());
    }
    
    @Test
    public void testProcessorPrepareWorkspace() throws IOException{
        processor.copyWorkspace();
        Assert.assertTrue(target.exists());
        processor.cleanupWorkspace();
        Assert.assertFalse(target.exists());        
    }
}

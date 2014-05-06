package com.github.mkolisnyk.muto.processor;

import java.io.File;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

public class MutoProcessorTest {

    private MutoProcessor processor;
    private File source;
    private File target;
    
    @Before
    public void setUp() {
        processor = new MutoProcessor();
        source = new File(".\\");
        target = new File("target\\muto\\");
        processor.setSourceDirectory(source.getAbsolutePath());
        processor.setTargetDirectory(target.getAbsolutePath());
    }
    
    @Test
    public void testProcessorInitialization() throws IOException{
        processor.copyWorkspace();
        processor.cleanupWorkspace();
    }
}

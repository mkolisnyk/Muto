package com.github.mkolisnyk.muto.reporter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Locale;
import java.util.Properties;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MavenMutoReporterTest {
    MavenMutoReporter reporter;
    
    @Before
    public void setUp() {
        reporter = new MavenMutoReporter();
    }
    
    @Test
    public void testGetDescription() throws Exception {
        Properties props = new Properties();
        props.load(new FileReader(new File("src/main/resources/muto.properties")));
        Assert.assertEquals(props.get("report.description"), reporter.getDescription(Locale.getDefault()).trim() );
        props.clear();
        props.load(new FileReader(new File("src/main/resources/muto_en.properties")));
        Assert.assertEquals(props.get("report.description"), reporter.getDescription(Locale.ENGLISH).trim() );
    }

    @Test
    public void testGetName() throws FileNotFoundException, IOException {
        Properties props = new Properties();
        props.load(new FileReader(new File("src/main/resources/muto.properties")));
        Assert.assertEquals(props.get("report.name"), reporter.getName(Locale.getDefault()).trim() );
        props.clear();
        props.load(new FileReader(new File("src/main/resources/muto_en.properties")));
        Assert.assertEquals(props.get("report.name"), reporter.getName(Locale.ENGLISH).trim() );
    }

    @Test
    public void testGetOutputName() {
        Assert.assertEquals("muto", reporter.getOutputName());
    }
}

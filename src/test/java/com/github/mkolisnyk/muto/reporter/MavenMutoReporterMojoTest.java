package com.github.mkolisnyk.muto.reporter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Locale;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.maven.doxia.siterenderer.DefaultSiteRenderer;
import org.apache.maven.doxia.siterenderer.Renderer;
import org.apache.maven.doxia.siterenderer.sink.SiteRendererSink;
import org.apache.maven.plugin.testing.AbstractMojoTestCase;
import org.apache.maven.reporting.sink.SinkFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MavenMutoReporterMojoTest extends AbstractMojoTestCase {
    MavenMutoReporter reporter;
    
    @Before
    public void setUp() throws Exception {
        super.setUp();
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
    public void testGetValuesInvalidLocale() {
        Assert.assertEquals("", reporter.getName(null));
        Assert.assertEquals("", reporter.getDescription(null));
    }
    
    @Test
    public void testGetOutputName() {
        Assert.assertEquals("muto", reporter.getOutputName());
    }
    
    @Test
    public void testReportExecution() throws Exception {
        File outputHtml = new File( "target/test", "test_report.html" );
        outputHtml.getParentFile().mkdirs();
        
        Renderer renderer = new DefaultSiteRenderer();
        reporter.setSiteRenderer(renderer);
        Assert.assertEquals(renderer, reporter.getSiteRenderer());
        Assert.assertNull(reporter.getProject());
        reporter.setOutputDirectory("src/test/resources/reporting");
        Assert.assertEquals("src/test/resources/reporting", reporter.getOutputDirectory());
        SiteRendererSink sink = SinkFactory.createSink( outputHtml.getParentFile(), outputHtml.getName() );
        reporter.setSink(sink);
        reporter.setSiteRenderer(renderer);
        reporter.executeReport(Locale.ENGLISH);
        
        String expected = FileUtils.readFileToString(new File("src/test/resources/reporting/muto.html"));
        Assert.assertEquals(expected.trim(), sink.getBody().trim());
    }
}

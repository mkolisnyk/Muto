package com.github.mkolisnyk.muto.reporter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

import org.apache.maven.doxia.sink.Sink;
import org.apache.maven.doxia.siterenderer.Renderer;
import org.apache.maven.project.MavenProject;
import org.apache.maven.reporting.AbstractMavenReport;
import org.apache.maven.reporting.MavenReportException;

/**
 * @author Myk Kolisnyk
 * @goal muto-report
 * @phase site
 */
public class MavenMutoReporter extends AbstractMavenReport {
    /**
     * .
     * @parameter expression="${project.reporting.outputDirectory}"
     * @required
     * @readonly
     */
    private String outputDirectory;
    /**
     * @parameter default-value="${project}"
     * @required
     * @readonly
     */
    private MavenProject project;
    /**
     * @component
     * @required
     * @readonly
     */
    private Renderer siteRenderer;
    /**
     * @param arg0 .
     * @return .
     */
    public final String getDescription(final Locale arg0) {
        String name = "";
        try {
            name = this.getBundle(arg0).getString("report.description");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return name;
    }

    /**
     * @param arg0 .
     * @return .
     */
    public final String getName(final Locale arg0) {
        String name = "";
        try {
            name = this.getBundle(arg0).getString("report.name");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return name;
    }

    /**
     * @return .
     */
    public final String getOutputName() {
        return "muto";
    }

    /**
     * @param outputDirectoryValue the outputDirectory to set
     */
    public final void setOutputDirectory(final String outputDirectoryValue) {
        this.outputDirectory = outputDirectoryValue;
    }

    /**
     * @param projectValue the project to set
     */
    public final void setProject(final MavenProject projectValue) {
        this.project = projectValue;
    }

    /**
     * @param siteRendererValue the siteRenderer to set
     */
    public final void setSiteRenderer(final Renderer siteRendererValue) {
        this.siteRenderer = siteRendererValue;
    }

    @Override
    protected final void executeReport(final Locale locale)
            throws MavenReportException {
        Sink sink = getSink();
        sink.head();
        sink.title();
        sink.text(this.getName(locale));
        sink.title_();
        sink.head_();
        
        sink.body();
        sink.section1();
        sink.sectionTitle1();
        sink.text("Overview");
        sink.sectionTitle1_();
        sink.section1_();
        sink.paragraph();
        sink.paragraph_();
        
        sink.section1();
        sink.sectionTitle1();
        sink.text("Rule Set");
        sink.sectionTitle1_();
        sink.section1_();
        sink.paragraph();
        sink.paragraph_();

        sink.section1();
        sink.sectionTitle1();
        sink.text("Result Details");
        sink.sectionTitle1_();
        sink.paragraph();
        sink.paragraph_();
        sink.section1_();

        sink.section2();
        sink.sectionTitle2();
        sink.text("Affected Tests Rating");
        sink.sectionTitle2_();
        sink.section2_();
        sink.paragraph();
        sink.paragraph_();

        sink.section2();
        sink.sectionTitle2();
        sink.text("Biggest Impact");
        sink.sectionTitle2_();
        sink.section2_();
        sink.paragraph();
        sink.paragraph_();

        sink.body_();
        
        sink.flush();
        sink.close();
    }

    @Override
    protected final String getOutputDirectory() {
        return this.outputDirectory;
    }

    @Override
    protected final MavenProject getProject() {
        return this.project;
    }

    @Override
    protected final Renderer getSiteRenderer() {
        return this.siteRenderer;
    }

    /**
     * .
     * @param locale .
     * @return .
     * @throws Exception 
     */
    private ResourceBundle getBundle(final Locale locale) throws Exception {
        return ResourceBundle.getBundle("muto", locale);
    }
}

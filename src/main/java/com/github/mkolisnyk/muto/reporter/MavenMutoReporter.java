package com.github.mkolisnyk.muto.reporter;

import java.io.File;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;

import javax.xml.bind.JAXB;

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
     */
    private Sink sink;
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

    /**
     * .
     * @return .
     */
    @Override
    public final Sink getSink() {
        if (sink != null) {
            return sink;
        } else {
            return super.getSink();
        }
    }

    /**
     * .
     * @param sinkValue .
     */
    public final void setSink(final Sink sinkValue) {
        sink = sinkValue;
    }

    @Override
    protected final void executeReport(final Locale locale)
            throws MavenReportException {
        MavenMutoReporterHelper helper = new MavenMutoReporterHelper();
        MutoResultSet resultArray;

        resultArray = JAXB.unmarshal(
                new File(
                     this.outputDirectory + File.separator + "muto_total.xml"
                ),
                MutoResultSet.class);

        sink = getSink();
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
        sink.paragraph();
        Map<String, Integer> results
            = helper.getOverviewMap(resultArray.getResultList());
        MavenMutoReporterDrawer.drawOverviewChart(sink, results);
        sink.paragraph_();
        sink.section1_();

        sink.section1();
        sink.sectionTitle1();
        sink.text("Rule Set");
        sink.sectionTitle1_();
        sink.paragraph();
        results = helper.getMutationRulesStats(resultArray.getResultList());
        MavenMutoReporterDrawer.drawStatsTable(sink, results);
        sink.paragraph_();
        sink.section1_();

        sink.section1();
        sink.sectionTitle1();
        sink.text("Result Statistics");
        sink.sectionTitle1_();
        sink.paragraph();
        results = helper.getTestClassesRating(resultArray.getResultList());
        MavenMutoReporterDrawer.drawStatsTable(sink, results);
        sink.paragraph_();
        sink.section1_();

        sink.section2();
        sink.sectionTitle2();
        sink.text("Affected Tests Rating");
        sink.sectionTitle2_();
        sink.paragraph();
        results = helper.getTestCasesRating(resultArray.getResultList());
        MavenMutoReporterDrawer.drawTestCasesStatsTable(sink, results);
        sink.paragraph_();
        sink.section2_();

        /*sink.section2();
        sink.sectionTitle2();
        sink.text("Biggest Impact");
        sink.sectionTitle2_();
        sink.section2_();
        sink.paragraph();
        sink.paragraph_();*/

        sink.section1();
        sink.sectionTitle1();
        sink.text("Result Details");
        sink.sectionTitle1_();
        sink.paragraph();
        sink.paragraph_();
        sink.section1_();

        sink.section2();
        sink.sectionTitle2();
        sink.text("Evergreen Tests");
        sink.sectionTitle2_();
        sink.paragraph();
        results = helper.getTestCasesRating(resultArray.getResultList());
        Map<String, Integer> filteredResults = new HashMap<String, Integer>();
        /*for (String testName : results.keySet()) {
            if (results.get(testName) <= 0) {
                filteredResults.put(testName, results.get(testName));
            }
        }*/
        for (Entry<String, Integer> entry : results.entrySet()) {
            if (entry.getValue() <= 0) {
                filteredResults.put(entry.getKey(), entry.getValue());
            }
        }
        MavenMutoReporterDrawer.drawTestCasesStatsTable(sink, filteredResults);
        sink.paragraph_();
        sink.section2_();

        sink.section2();
        sink.sectionTitle2();
        sink.text("Passed Runs");
        sink.sectionTitle2_();
        sink.paragraph();
        MavenMutoReporterDrawer.drawTestRuns(
                sink, resultArray.getResultList(), true);
        sink.paragraph_();
        sink.section2_();

        sink.section2();
        sink.sectionTitle2();
        sink.text("Failed/Errored Runs");
        sink.sectionTitle2_();
        sink.paragraph();
        MavenMutoReporterDrawer.drawTestRuns(
                sink, resultArray.getResultList(), false);
        sink.paragraph_();
        sink.section2_();

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
     * @throws Exception .
     */
    private ResourceBundle getBundle(final Locale locale) throws Exception {
        return ResourceBundle.getBundle("muto", locale);
    }
}

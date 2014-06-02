/**
 * .
 */
package com.github.mkolisnyk.muto.reporter;

import java.util.Map;
import java.util.Map.Entry;

import org.apache.maven.doxia.sink.Sink;

/**
 * @author Myk Kolisnyk
 */
public final class MavenMutoReporterDrawer {
    /**
     * .
     */
    private MavenMutoReporterDrawer() {
    }

    /**
     * .
     * @param sink .
     * @param results .
     */
    public static void drawOverviewChart(
            final Sink sink,
            final Map<String, Integer> results) {
        final int radius = 90;
        final int centerX = 120;
        final int centerY = 110;
        final int minX = 30;
        final int minY = 20;
        final int splitSegments = 4;
        final int maxPercentage = 100;
        String template = "<svg width=\"450\" height=\"250\">"
                + "<defs>"
                + "<style type=\"text/css\"><![CDATA["
                + "  .errors {fill:red;"
                + "     fill-opacity: 1;"
                + "     stroke:red;"
                + "     stroke-width: 1 }"
                + "  .failures {fill:gold;"
                + "     fill-opacity: 1;"
                + "     stroke:gold;"
                + "     stroke-width: 1 }"
                + "  .passes {fill:darkgreen;"
                + "     fill-opacity: 1;"
                + "     stroke:darkgreen;"
                + "     stroke-width: 1 }"
                + "  .undefined {fill:silver;"
                + "     fill-opacity: 1;"
                + "     stroke:silver;"
                + "     stroke-width: 1 }"
                + "]]></style>"
                + "</defs>"
                + "<rect x=\"5\" y=\"5\" "
                + "     width=\"440\""
                + "     height=\"240\""
                + "     style=\"fill: none;"
                + "     stroke: black;"
                + "     stroke-width: 1\"/>"
                + "{Graph}"
                + "<rect x=\"240\" y=\"25\" "
                + "     width=\"20\" height=\"20\" class=\"errors\"/>"
                + "<rect x=\"240\" y=\"60\" "
                + "     width=\"20\" height=\"20\" class=\"failures\"/>"
                + "<rect x=\"240\" y=\"95\" "
                + "     width=\"20\" height=\"20\" class=\"passes\"/>"
                + "<rect x=\"240\" y=\"130\" "
                + "     width=\"20\" height=\"20\" class=\"undefined\"/>"
                + "<text x=\"270\" y=\"40\">Errors {E%}% ({E})</text>"
                + "<text x=\"270\" y=\"75\">Failures {F%}% ({F})</text>"
                + "<text x=\"270\" y=\"110\">Passed {P%}% ({P})</text>"
                + "<text x=\"270\" y=\"145\">Undefined {U%}% ({U})</text>"
                + "<text x=\"130\" y=\"225\">"
                + "<tspan font-weight=\"bold\">Total Status Overview</tspan>"
                + "</text>"
                + "</svg>";
        int errors = results.get(MavenMutoReporterHelper.ERRORED);
        int fails = results.get(MavenMutoReporterHelper.FAILED);
        int passed = results.get(MavenMutoReporterHelper.PASSED);
        int total = 0;
        for (Entry<String, Integer> res : results.entrySet()) {
            total += res.getValue();
        }
        if (total == 0) {
            total = 1;
        }
        String graphText = "";
        int prevX = minX;
        int prevY = minY + radius;
        for (int i = 1; i <= splitSegments; i++) {
            int eX = centerX
                    - (int) (Math.sin(Math.PI / 2 + 2 * Math.PI
                            * (double) (i * errors / (double) splitSegments)
                            / (double) total) * radius);
            int eY = centerY
                    - (int) (Math.cos(Math.PI / 2 + 2 * Math.PI
                            * (double) (i * errors / (double) splitSegments)
                            / (double) total) * radius);
            graphText = graphText.concat("<path d=\"M" + centerX + ","
                    + centerY + " L" + prevX + "," + prevY + " A" + radius
                    + "," + radius + " 0 0,0 " + eX + "," + eY
                    + " z\" class=\"errors\"/>");
            prevX = eX;
            prevY = eY;
        }
        for (int i = 1; i <= splitSegments; i++) {
            int eX = centerX
                    - (int) (Math.sin(Math.PI / 2 + 2 * Math.PI
                            * (double) (errors + i * fails
                                    / (double) splitSegments)
                            / (double) total) * radius);
            int eY = centerY
                    - (int) (Math.cos(Math.PI / 2 + 2 * Math.PI
                            * (double) (errors + i * fails
                                    / (double) splitSegments)
                            / (double) total) * radius);
            graphText = graphText.concat("<path d=\"M" + centerX + ","
                    + centerY + " L" + prevX + "," + prevY + " A" + radius
                    + "," + radius + " 0 0,0 " + eX + "," + eY
                    + " z\" class=\"failures\"/>");
            prevX = eX;
            prevY = eY;
        }
        for (int i = 1; i <= splitSegments; i++) {
            int eX = centerX
                    - (int) (Math
                            .sin(Math.PI
                                    / 2
                                    + 2
                                    * Math.PI
                                    * (double) (i * passed / splitSegments
                                            + errors + fails)
                                    / (double) total) * radius);
            int eY = centerY
                    - (int) (Math
                            .cos(Math.PI
                                    / 2
                                    + 2
                                    * Math.PI
                                    * (double) (i * passed / splitSegments
                                            + errors + fails)
                                    / (double) total) * radius);
            graphText = graphText.concat("<path d=\"M" + centerX + ","
                    + centerY + " L" + prevX + "," + prevY + " A" + radius
                    + "," + radius + " 0 0,0 " + eX + "," + eY
                    + " z\" class=\"passes\"/>");
            prevX = eX;
            prevY = eY;
        }
        template = template
                .replaceAll("\\{Graph\\}", graphText)
                .replaceAll("\\{E%\\}",
                        "" + maxPercentage * (float) errors
                            / (float) total)
                .replaceAll("\\{E\\}",
                        "" + errors)
                .replaceAll("\\{F%\\}",
                        "" + maxPercentage * (float) fails
                            / (float) total)
                .replaceAll("\\{F\\}",
                        "" + fails)
                .replaceAll("\\{P%\\}",
                        "" + maxPercentage * (float) passed
                            / (float) total)
                .replaceAll("\\{P\\}",
                        "" + passed)
                .replaceAll("\\{U%\\}",
                        "" + maxPercentage
                            * (float) (total - passed - errors - fails)
                            / (float) total)
                .replaceAll("\\{U\\}",
                        "" + (total - passed - errors - fails));
        sink.rawText(template);
    }

    /**
     * .
     * @param sink .
     * @param results .
     */
    public static void drawStatsTable(
            final Sink sink,
            final Map<String, Integer> results) {
        sink.table();
        sink.tableRow();
        sink.tableHeaderCell();
        sink.text("Rule Name");
        sink.tableHeaderCell_();
        sink.tableHeaderCell();
        sink.text("Errors Detected");
        sink.tableHeaderCell_();
        sink.tableRow_();
        for (Entry<String, Integer> rule : results.entrySet()) {
            sink.tableRow();
            sink.tableCell();
            sink.text(rule.getKey());
            sink.tableCell_();
            sink.tableCell();
            sink.text(rule.getValue().toString());
            sink.tableCell_();
            sink.tableRow_();
        }
        sink.table_();
    }

    /**
     * .
     * @param sink .
     * @param results .
     */
    public static void drawTestCasesStatsTable(
            final Sink sink,
            final Map<String, Integer> results) {
        sink.table();
        sink.tableRow();
        sink.tableHeaderCell();
        sink.text("Test Class Name");
        sink.tableHeaderCell_();
        sink.tableHeaderCell();
        sink.text("Test Name");
        sink.tableHeaderCell_();
        sink.tableHeaderCell();
        sink.text("Errors Detected");
        sink.tableHeaderCell_();
        sink.tableRow_();
        for (Entry<String, Integer> testRecord : results.entrySet()) {
            sink.tableRow();
            sink.tableCell();
            sink.text(testRecord.getKey().split(" ")[0]);
            sink.tableCell_();
            sink.tableCell();
            sink.text(testRecord.getKey().split(" ")[1]);
            sink.tableCell_();
            sink.tableCell();
            sink.text(testRecord.getValue().toString());
            sink.tableCell_();
            sink.tableRow_();
        }
        sink.table_();
    }

    /**
     * .
     * @param sink .
     * @param results .
     * @param passedOnly .
     */
    public static void drawTestRuns(
            final Sink sink,
            final MutoResult[] results,
            final boolean passedOnly) {
        sink.table();
        sink.tableRow();
        sink.tableHeaderCell();
        sink.text("Rule Name");
        sink.tableHeaderCell_();
        sink.tableHeaderCell();
        sink.text("Mutated File");
        sink.tableHeaderCell_();
        sink.tableHeaderCell();
        sink.text("Change Location");
        sink.tableHeaderCell_();
        sink.tableHeaderCell();
        sink.text("Changed Fragment");
        sink.tableHeaderCell_();
        if (!passedOnly) {
            sink.tableHeaderCell();
            sink.text("Affected Tests");
            sink.tableHeaderCell_();
        }
        sink.tableRow_();

        for (MutoResult result : results) {
            if (result.isPassed() == passedOnly) {
                sink.tableRow();
                sink.tableCell();
                sink.text(result.getLocation().getRuleName());
                sink.tableCell_();

                sink.tableCell();
                sink.text(result.getLocation().getFileName());
                sink.tableCell_();

                sink.tableCell();
                sink.text("from position "
                        + result.getLocation().getStartPosition()
                        + " to "
                        + result.getLocation().getEndPosition());
                sink.tableCell_();

                sink.tableCell();
                sink.text(result.getLocation().getMatchedText());
                sink.tableCell_();

                if (!passedOnly) {
                    sink.tableCell();
                    sink.text("-");
                    sink.tableCell_();
                }

                sink.tableRow_();
            }
        }
        sink.table_();
    }
}

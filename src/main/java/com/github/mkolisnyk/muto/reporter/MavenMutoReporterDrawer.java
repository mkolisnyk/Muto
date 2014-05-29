/**
 * 
 */
package com.github.mkolisnyk.muto.reporter;

import java.util.Map;

import org.apache.maven.doxia.sink.Sink;

/**
 * @author Myk Kolisnyk
 */
public final class MavenMutoReporterDrawer {
    private MavenMutoReporterDrawer() {
    }
    
    public static void drawOverviewChart(Sink sink, Map<String, Integer> results) {
        int radius = 90;
        int centerX = 120;
        int centerY = 110;
        int minX = 30;
        int minY = 20;
        String template = "<svg width=\"400\" height=\"250\">"
                + "<defs>"
                + "<style type=\"text/css\"><![CDATA["
                + "  .errors { fill:red;fill-opacity: 1;stroke:black;stroke-width: 1 }"
                + "  .failures { fill:gold;fill-opacity: 1;stroke:black;stroke-width: 1 }"
                + "  .passes { fill:darkgreen;fill-opacity: 1;stroke:black;stroke-width: 1 }"
                + "  .undefined { fill:silver;fill-opacity: 1;stroke:black;stroke-width: 1 }"
                + "]]></style>"
                + "</defs>"
                + "<rect x=\"5\" y=\"5\" width=\"390\" height=\"240\" style=\"fill: none; stroke: black; stroke-width: 1\"/>"
                + "<path d=\"M" + centerX + "," + centerY + " L" + minX + "," + (minY + radius)+ " A" + radius + "," + radius + " 0 0,1 {E_X},{E_Y} z\" class=\"errors\"/>"
                + "<path d=\"M" + centerX + "," + centerY + " L{E_X},{E_Y} A" + radius + "," + radius + " 0 0,1 {F_X},{F_Y} z\" class=\"failures\"/>"
                + "<path d=\"M" + centerX + "," + centerY + " L{F_X},{F_Y} A" + radius + "," + radius + " 0 0,1 {P_X},{P_Y} z\" class=\"passes\"/>"
                + "<path d=\"M" + centerX + "," + centerY + " L{P_X},{P_Y} A" + radius + "," + radius + " 0 0,1 " + minX + "," + (minY + radius)+ " z\" class=\"undefined\"/>"
                + "<rect x=\"240\" y=\"25\" width=\"20\" height=\"20\" class=\"errors\"/>"
                + "<rect x=\"240\" y=\"60\" width=\"20\" height=\"20\" class=\"failures\"/>"
                + "<rect x=\"240\" y=\"95\" width=\"20\" height=\"20\" class=\"passes\"/>"
                + "<rect x=\"240\" y=\"130\" width=\"20\" height=\"20\" class=\"undefined\"/>"
                + "<text x=\"270\" y=\"40\">Errors {E%} ({E})</text>"
                + "<text x=\"270\" y=\"75\">Failures {F%} ({F})</text>"
                + "<text x=\"270\" y=\"110\">Passed {P%} ({P})</text>"
                + "<text x=\"270\" y=\"145\">Undefined {U%} ({U})</text>"
                + "<text x=\"130\" y=\"225\"><tspan font-weight=\"bold\">Total Status Overview</tspan></text>"
                + "</svg>";
        int errors = results.get(MavenMutoReporterHelper.ERRORED);
        int fails = results.get(MavenMutoReporterHelper.FAILED);
        int passed = results.get(MavenMutoReporterHelper.PASSED);
        int total = results.size();
        if (total == 0) {
            total = 1;
        }
        int e_x = centerX - (int) (Math.sin(Math.PI / 2 + 2 * Math.PI * (double) errors/(double) total) * radius);
        int e_y = centerY - (int) (Math.cos(Math.PI / 2 + 2 * Math.PI * (double) errors/(double) total) * radius);
        int f_x = centerX - (int) (Math.sin(Math.PI / 2 + 2 * Math.PI * (double) (errors + fails)/(double) total) * radius);
        int f_y = centerY - (int) (Math.cos(Math.PI / 2 + 2 * Math.PI * (double) (errors + fails)/(double) total) * radius);
        int p_x = centerX - (int) (Math.sin(Math.PI / 2 + 2 * Math.PI * (double) (passed + errors + fails)/(double) total) * radius);
        int p_y = centerY - (int) (Math.cos(Math.PI / 2 + 2 * Math.PI * (double) (passed + errors + fails)/(double) total) * radius);
        
        template = template
                .replaceAll("{E_X}", "" + e_x)
                .replaceAll("{E_Y}", "" + e_y)
                .replaceAll("{F_X}", "" + f_x)
                .replaceAll("{F_Y}", "" + f_y)
                .replaceAll("{P_X}", "" + p_x)
                .replaceAll("{P_Y}", "" + p_y)
                .replaceAll("{E%}", "" + (float) errors/ (float) total)
                .replaceAll("{E}", "" + errors)
                .replaceAll("{F%}", "" + (float) fails/ (float) total)
                .replaceAll("{F}", "" + fails)
                .replaceAll("{P%}", "" + (float) passed/ (float) total)
                .replaceAll("{P}", "" + passed)
                .replaceAll("{U%}", "" + (float) (total - passed - errors - fails)/ (float) total)
                .replaceAll("{U}", "" + (total - passed - errors - fails));

        sink.rawText(template);
    }
}

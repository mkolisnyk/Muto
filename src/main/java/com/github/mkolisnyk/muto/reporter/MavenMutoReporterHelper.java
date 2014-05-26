package com.github.mkolisnyk.muto.reporter;

import java.util.HashMap;
import java.util.Map;

public class MavenMutoReporterHelper {
    private static final int SUCCESS_CODE = 0;
    private static final String PASSED = "Passed";
    private static final String FAILED = "Failed";
    private static final String ERRORED = "Errored";
    
    public Map<String, Integer> getOverviewMap(
            MutoResult[] resultArray) {
        Map<String, Integer> result = new HashMap<String, Integer>();
        int passed = 0;
        int failed = 0;
        int errored = 0;
        for (MutoResult item : resultArray) {
            if (item.getResults() != null
                    && item.getResults().size() > 0) {
                errored++;
                continue;
            }
            if (item.getExitCode() == SUCCESS_CODE) {
                passed++;
            } else {
                failed++;
            }
        }
        result.put(PASSED, passed);
        result.put(FAILED, failed);
        result.put(ERRORED, errored);
        return result;
    }
}

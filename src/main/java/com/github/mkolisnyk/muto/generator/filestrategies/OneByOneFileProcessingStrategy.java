package com.github.mkolisnyk.muto.generator.filestrategies;

import java.io.File;

import com.github.mkolisnyk.muto.data.MutationLocation;
import com.github.mkolisnyk.muto.generator.FileProcessingStrategy;
import com.github.mkolisnyk.muto.generator.MutationStrategy;

/**
 * @author Myk Kolisnyk
 */
public class OneByOneFileProcessingStrategy extends
        FileProcessingStrategy {

    /**
     * .
     */
    private int currentFileIndex     = 0;
    /**
     * .
     */
    private int currentStrategyIndex = 0;

    @Override
    public final void next() {
        File file = this.getFiles().get(currentFileIndex);
        MutationStrategy strategy = this.getMutationStrategies().get(
                currentStrategyIndex);
        String content = "";
        content = read(file.getAbsolutePath());
        String newContent = strategy.next(content);
        MutationLocation location = strategy.getLocation();
        location.setFileName(file.getAbsolutePath());
        this.setLocation(location);
        write(file.getAbsolutePath(), newContent);
        if (!strategy.hasNext(content)) {
            currentStrategyIndex++;
        }
        if (currentStrategyIndex >= this.getMutationStrategies()
                .size()) {
            this.resetStrategies();
            currentFileIndex++;
            currentStrategyIndex = 0;
        }
    }

    @Override
    public final boolean hasNext() {
        if (this.getFiles().size() <= currentFileIndex) {
            return false;
        }
        return true;
    }
}

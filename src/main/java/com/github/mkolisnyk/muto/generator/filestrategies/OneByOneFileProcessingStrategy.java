package com.github.mkolisnyk.muto.generator.filestrategies;

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
        String file = this.getFiles().get(currentFileIndex);
        MutationStrategy strategy = this.getMutationStrategies().get(
                currentStrategyIndex);
        String content = "";
        content = read(file);
        String newContent = strategy.next(content);
        write(file, newContent);
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

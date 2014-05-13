package com.github.mkolisnyk.muto.generator.filestrategies;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;

import com.github.mkolisnyk.muto.data.MutationLocation;
import com.github.mkolisnyk.muto.generator.FileProcessingStrategy;
import com.github.mkolisnyk.muto.generator.MutationRule;
import com.github.mkolisnyk.muto.generator.MutationStrategy;
import com.github.mkolisnyk.muto.generator.rules.BlockCleanMutationRule;
import com.github.mkolisnyk.muto.generator.strategies.OneByOneMutationStrategy;

public class OneByOneFileProcessingStrategyTest {
    private String file1 = ClassLoader.getSystemResource("sample_file.txt").getFile();
    private String file2 = ClassLoader.getSystemResource("sample_file2.txt").getFile();

    private String[] fileContent1 = {
            "sample {}",
            "sample {block content -1 {}}",
            "sample {block content -1 {with nested block}}",
            "sample {block content -1 {with nested block}}",
            "sample {block content -1 {with nested block}}",
    };
    private String[] fileContent2 = {
            "another sample {{multiple block} content -1 {with nested block}}",
            "another sample {{multiple block} content -1 {with nested block}}",
            "another sample {}",
            "another sample {{} content -1 {with nested block}}",
            "another sample {{multiple block} content -1 {}}",
    };
    private MutationLocation locations[] = {
            new MutationLocation(7,43,file1),
            new MutationLocation(25,42,file1),
            new MutationLocation(15,62,file2),
            new MutationLocation(16,30,file2),
            new MutationLocation(44,61,file2),
    };

    @Test
    public void testOneByOneFileProcessingStrategy() throws IOException {
        FileProcessingStrategy fileStrategy = new OneByOneFileProcessingStrategy();
        MutationStrategy strategy = new OneByOneMutationStrategy();
        MutationRule rule = new BlockCleanMutationRule();
        List<String> files = new ArrayList<String>();
        
        files.add(file1);
        files.add(file2);
        
        fileStrategy.setFiles(files);
        strategy.addRule(rule);
        fileStrategy.addMutationStrategy(strategy);
        
        int counter = 0;
        String actual;
        while(fileStrategy.hasNext()) {
            fileStrategy.next();
            actual = FileUtils.readFileToString(new File(file1));
            Assert.assertEquals("Iteration " + counter, fileContent1[counter], actual);
            actual = FileUtils.readFileToString(new File(file2));
            Assert.assertEquals("Iteration " + counter, fileContent2[counter], actual);
            Assert.assertEquals("Iteration " + counter, locations[counter], 
                    fileStrategy.getLocation());
            counter++;
            fileStrategy.restore(file1);
            fileStrategy.restore(file2);
        }
        Assert.assertEquals(5, counter);
    }
}

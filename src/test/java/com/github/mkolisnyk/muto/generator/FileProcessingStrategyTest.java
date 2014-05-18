package com.github.mkolisnyk.muto.generator;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;

import com.github.mkolisnyk.muto.generator.rules.NumberSignMutationRule;
import com.github.mkolisnyk.muto.generator.strategies.OneByOneMutationStrategy;

/**
 * @author Myk Kolisnyk
 */
public class FileProcessingStrategyTest {

    private class FakeFileProcessingStrategy extends FileProcessingStrategy {

        @Override
        public void next() {
        }

        @Override
        public boolean hasNext() {
            return false;
        }        
    }
    
    @Test
    public void testGetSetMutationStrategies() {
        FileProcessingStrategy fileStrategy = new FakeFileProcessingStrategy(); 
        List<MutationStrategy> strategies = new ArrayList<MutationStrategy>();
        strategies.add(new OneByOneMutationStrategy());
        fileStrategy.setMutationStrategies(strategies);
        Assert.assertEquals(strategies, fileStrategy.getMutationStrategies());
    }

    @Test
    public void testAddMutationStrategy() {
        FileProcessingStrategy fileStrategy = new FakeFileProcessingStrategy();
        OneByOneMutationStrategy strategy = new OneByOneMutationStrategy(); 
        fileStrategy.addMutationStrategy(strategy);
        Assert.assertEquals(fileStrategy.getMutationStrategies().get(0),strategy);
        fileStrategy.addMutationStrategy(strategy);
        Assert.assertEquals(fileStrategy.getMutationStrategies().get(1),strategy);
    }

    @Test
    public void testGetSetFiles() {
        List<String> files = new ArrayList<String>();
        FileProcessingStrategy fileStrategy = new FakeFileProcessingStrategy();
        files.add("file1");
        files.add("file2");
        fileStrategy.setFiles(files);
        Assert.assertTrue(fileStrategy.getFiles().containsAll(files));
        Assert.assertTrue(files.containsAll(fileStrategy.getFiles()));
    }

    @Test
    public void testRead() throws IOException {
        FileProcessingStrategy fileStrategy = new FakeFileProcessingStrategy();
        URL fileUrl = ClassLoader.getSystemResource("sample_file.txt");
        String file = fileUrl.getFile();
        String content = fileStrategy.read(file);
        String actual = FileUtils.readFileToString(new File(file));
        Assert.assertEquals(content, actual);                
        Assert.assertEquals("", fileStrategy.read("unknown"));
    }
    
    @Test
    public void testWrite() throws Exception {
        FileProcessingStrategy fileStrategy = new FakeFileProcessingStrategy();
        URL fileUrl = ClassLoader.getSystemResource("sample_file.txt");
        String file = fileUrl.getFile();
        String content = fileStrategy.read(file);
        fileStrategy.write(file, content);
        String actual = FileUtils.readFileToString(new File(file));
        Assert.assertEquals(content, actual);                
        File backup = new File(file + ".bak");
        Assert.assertTrue(backup.exists());
        fileStrategy.read(file);
        Assert.assertFalse(backup.exists());
    }
    
    @Test
    public void testRestore() throws Exception {
        FileProcessingStrategy fileStrategy = new FakeFileProcessingStrategy();
        URL fileUrl = ClassLoader.getSystemResource("sample_file.txt");
        String file = fileUrl.getFile();
        String content = fileStrategy.read(file);
        
        fileStrategy.write(file, content.substring(content.length() / 2));
        String content2 = fileStrategy.read(file);
        Assert.assertEquals(content, content2);
    }
    
    @Test
    public void testRestoreStrategies() {
        String input = "-1=-1";
        FileProcessingStrategy fileStrategy = new FakeFileProcessingStrategy(); 
        List<MutationStrategy> strategies = new ArrayList<MutationStrategy>();
        MutationStrategy strategy = new OneByOneMutationStrategy();
        MutationRule rule = new NumberSignMutationRule();
        strategy.addRule(rule);
        
        while(strategy.hasNext(input)) {
            strategy.next(input);
        }
        Assert.assertFalse(strategy.hasNext(input));

        strategies.add(strategy);
        fileStrategy.setMutationStrategies(strategies);
        fileStrategy.resetStrategies();
        strategy = fileStrategy.getMutationStrategies().get(0);
        Assert.assertTrue(strategy.hasNext(input));
    }
}

package com.github.mkolisnyk.muto.generator.rules;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.github.mkolisnyk.muto.data.MutationLocation;
import com.github.mkolisnyk.muto.generator.MutationRule;

@RunWith(Parameterized.class)
public class NumberSignMutationRuleTest {

    @Parameters(name="Test: {5}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                 { "int i=-1", 0, "int i=1", new MutationLocation(6,8) ,1 , "Simple replacement verification" },
                 { "int i=1", 0, "int i=-1", new MutationLocation(6,7) ,1 , "Positive number replacement verification" },
                 { "int i=-1,j=2;", 0, "int i=1,j=2;", new MutationLocation(6,8) ,2 , "Multiple entries. First value replaced" },
                 { "int i=-1,j=2;", 1, "int i=-1,j=-2;", new MutationLocation(11,12) ,2 , "Multiple entries. Second value replaced" },
                 { "No number string", 2, "No number string", new MutationLocation(), 0 , "No replacement should be done on no numbers" },
                 { "int i=1", 2, "int i=1", new MutationLocation(), 1 , "Out of range position should return string unchanged" }/*,
                 { "int i=test-1", 0, "int i=test-1", new MutationLocation() ,0 , "When - sign is a part of expression it's not changed" }*/
           });
    }
    
    private String input;
    private Integer position;
    private String expectedOutput;
    private MutationLocation location;
    private Integer total;
    private String description;
    private MutationRule rule;
    
    public NumberSignMutationRuleTest(String input,
            Integer position,
            String expectedOutput, MutationLocation location,
            Integer total, String description) {
        super();
        this.input = input;
        this.position = position;
        this.expectedOutput = expectedOutput;
        this.location = location;
        this.total = total;
        this.description = description;
    }

    @Before
    public void setUp(){
        rule = new NumberSignMutationRule();
    }
    
    @Test
    public void testApply() {
        String actual = rule.apply(input, position);
        Assert.assertEquals(expectedOutput, actual);
        Assert.assertTrue(location.equals(rule.getLocation()));
    }

    @Test
    public void testTotal() {
        int actual = rule.total(input);
        Assert.assertEquals(total.intValue(),actual);
    }

}

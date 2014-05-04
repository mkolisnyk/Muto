package com.github.mkolisnyk.muto.generator.rules;

import static org.junit.Assert.*;

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
public class BlockCleanMutationRuleTest {

    @Parameters(name="Test: {5}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                 { "sample {block} instruction", 0, "sample {} instruction", new MutationLocation(7,12) ,1 , "Simple replacement verification" },
                 { "sample {block} {block2} instruction", 1, "sample {block} {} instruction", new MutationLocation(15,21) ,2 , "Double blocks on one level verification" },
                 { "sample {{nested} block} instruction", 0, "sample {} instruction", new MutationLocation(7,21) ,2 , "Nested blocks. Replace top-level item" },
                 { "sample {{nested} block} instruction", 1, "sample {{} block} instruction", new MutationLocation(8,14) ,2 , "Nested blocks. Replace nested item" },
                 { "sample {{nested} {block}} instruction", 2, "sample {{nested} {}} instruction", new MutationLocation(17,22) ,3 , "Nested blocks. Replace nested item - changing levels up" },
                 { "sample {{nested} {{double}block}} instruction", 3, "sample {{nested} {{}block}} instruction", new MutationLocation(18,24) ,4 , "Nested blocks. The innermost block replacement" },
                 { "sample {block} instruction", 1, "sample {block} instruction", new MutationLocation(-1,-1) ,1 , "1 level block out of boundaries" },
                 { "sample {{nested} {{double}block}} instruction", 5, "sample {{nested} {{double}block}} instruction", new MutationLocation(-1,-1) ,4 , "Multi level block out of boundaries" },
           });
    }
    
    private String input;
    private Integer position;
    private String expectedOutput;
    private MutationLocation location;
    private Integer total;
    private String description;
    private MutationRule rule;
    
    public BlockCleanMutationRuleTest(String input,
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
        rule = new BlockCleanMutationRule();
    }
    
    @Test
    public void testApply() {
        String actual = rule.apply(input, position);
        Assert.assertEquals(expectedOutput, actual);
        Assert.assertTrue(
                "Locations are different. Expected: "
                        + location
                        + ". Actual: "
                        + rule.getLocation(),
                location.equals(rule.getLocation())
        );
    }

    @Test
    public void testTotal() {
        int actual = rule.total(input);
        Assert.assertEquals(total.intValue(),actual);
    }
}
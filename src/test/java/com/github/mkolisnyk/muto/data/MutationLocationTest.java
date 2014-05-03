package com.github.mkolisnyk.muto.data;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MutationLocationTest {
    private MutationLocation location;

    @Before
    public void setUp() {
        location = new MutationLocation();
    }

    @Test
    public void testEqualsSameObjectShouldReturnTrue(){
        Assert.assertTrue(location.equals(location));
    }

    @Test
    public void testEqualsSimilarObjectShouldReturnTrue(){
        MutationLocation additionalLocation = new MutationLocation();
        Assert.assertTrue(location.equals(additionalLocation));
    }

    @Test
    public void testEqualsDifferentObjectOfTheSameTypeShouldReturnFalse(){
        MutationLocation additionalLocation = new MutationLocation();
        additionalLocation.setFileName("test");
        Assert.assertFalse(location.equals(additionalLocation));

        additionalLocation = new MutationLocation(0,1);
        Assert.assertFalse(location.equals(additionalLocation));

        additionalLocation = new MutationLocation(1,0);
        Assert.assertFalse(location.equals(additionalLocation));
    }

    @Test
    public void testEqualsDifferentObjectTypeShouldReturnFalse(){
        Assert.assertFalse(location.equals("Sample"));
    }
    
    @Test
    public void testEqualsNullObjectTypeShouldReturnFalse(){
        Assert.assertFalse(location.equals(null));
    }

    @Test
    public void testHashCode() {
        MutationLocation additionalLocation = new MutationLocation();
        Assert.assertNotEquals(location.hashCode(), additionalLocation.hashCode());
    }
}

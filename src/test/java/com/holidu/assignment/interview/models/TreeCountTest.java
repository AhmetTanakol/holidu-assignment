package com.holidu.assignment.interview.models;

import java.util.HashMap;

import org.junit.Assert;
import org.junit.Test;

import com.holidu.interview.assignment.model.TreeCount;

public class TreeCountTest {

    @Test
    public void createTreeCountObject() {
    	TreeCount treeCount = new TreeCount();
    	Assert.assertNotNull(treeCount);
        Assert.assertTrue(treeCount.getTrees().isEmpty());
    }
    
    @Test
    public void addTreeCount() {
    	TreeCount treeCount = new TreeCount();
        Assert.assertTrue(treeCount.getTrees().isEmpty());
        treeCount.addTree("test", 5);
        HashMap<String, Integer> trees= treeCount.getTrees();
        Assert.assertTrue(!trees.isEmpty());
        Assert.assertTrue(trees.containsKey("test"));
        Assert.assertTrue(trees.get("test") == 5);
    }
    
    @Test
    public void incrementTreeCount() {
    	TreeCount treeCount = new TreeCount();
        Assert.assertTrue(treeCount.getTrees().isEmpty());
        treeCount.addTree("test", 5);
        HashMap<String, Integer> trees= treeCount.getTrees();
        Assert.assertTrue(!trees.isEmpty());
        Assert.assertTrue(trees.containsKey("test"));
        Assert.assertTrue(trees.get("test") == 5);
        treeCount.addTree("test", 5);
        Assert.assertTrue(trees.get("test") == 10);
    }
}

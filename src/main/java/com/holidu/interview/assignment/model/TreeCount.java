package com.holidu.interview.assignment.model;

import java.util.HashMap;

public class TreeCount {
	private HashMap<String,Integer> trees = new HashMap<String,Integer>();

	public HashMap<String, Integer> getTrees() {
		return trees;
	}

	public void addTree(String name, int count) {
		trees.merge(name, count, Integer::sum);
	}


}

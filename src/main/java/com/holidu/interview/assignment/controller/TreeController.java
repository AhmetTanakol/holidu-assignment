package com.holidu.interview.assignment.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.holidu.interview.assignment.model.Tree;
import com.holidu.interview.assignment.services.MultithreadedFetchService;

@RestController
public class TreeController {
	// private FetchService fetchService = new FetchService();
	private MultithreadedFetchService fetchService;

    @GetMapping(path = "/trees")
    public ResponseEntity<String> getTreeData(@RequestParam int x, @RequestParam int y, @RequestParam int radius) {
    	String[] urls = {
    			"https://data.cityofnewyork.us/resource/uvpi-gqnh.json?$limit=5&$offset=0",
    			"https://data.cityofnewyork.us/resource/uvpi-gqnh.json?$limit=5&$offset=4",
    			"https://data.cityofnewyork.us/resource/uvpi-gqnh.json?$limit=5&$offset=8"
    	};
    	
    	this.fetchService = new MultithreadedFetchService(urls);
    	try {
			fetchService.fetchData();
			return new ResponseEntity<String>("ok", HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>("ok", HttpStatus.BAD_REQUEST);
		}
    	
    }
}

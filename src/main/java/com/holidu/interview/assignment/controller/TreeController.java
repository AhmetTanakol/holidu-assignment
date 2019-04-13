package com.holidu.interview.assignment.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.holidu.interview.assignment.services.MultithreadedFetchService;
import com.holidu.interview.assignment.services.TreeService;
import com.holidu.interview.assignment.utils.Utils;

@RestController
public class TreeController {
	// private FetchService fetchService = new FetchService();
	private MultithreadedFetchService fetchService = new MultithreadedFetchService();
	private TreeService treeService = new TreeService();
	private Utils utils = new Utils();

    @GetMapping(path = "/trees")
    public ResponseEntity<Map<String, Integer>> getTreeData(@RequestParam int x, @RequestParam int y, @RequestParam int radius) {
    	String url = "https://data.cityofnewyork.us/resource/nwxe-4ae8.json";
    	JSONObject boundaries = utils.calculateCircleBoundaries(x, y, radius);
    	try {
    		URI uri = treeService.uriBuilder(url, boundaries, "", "");
        	URI[] uris = {uri};
        	fetchService.setURI(uris);
			fetchService.fetchData();
			List<String> responses = fetchService.getResponses();
			return new ResponseEntity<Map<String, Integer>>(treeService.formatResponse(responses).getTrees(), HttpStatus.OK);
		} catch (URISyntaxException e1) {
			e1.printStackTrace();
			return new ResponseEntity<Map<String, Integer>>(new HashMap<String, Integer>(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
    	catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Map<String, Integer>>(new HashMap<String, Integer>(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
    	
    }
}

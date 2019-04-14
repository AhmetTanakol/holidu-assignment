package com.holidu.interview.assignment.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.holidu.interview.assignment.errors.InvalidRadiusException;
import com.holidu.interview.assignment.errors.InvalidURLException;
import com.holidu.interview.assignment.errors.UnexpectedServerErrorException;
import com.holidu.interview.assignment.model.Boundaries;
import com.holidu.interview.assignment.model.TreeCount;
import com.holidu.interview.assignment.services.MultithreadedFetchService;
import com.holidu.interview.assignment.services.TreeService;
import com.holidu.interview.assignment.utils.Utils;

@RestController
public class TreeController {
	/// Load services
	private MultithreadedFetchService fetchService = new MultithreadedFetchService();
	private TreeService treeService = new TreeService();
	private Utils utils = new Utils();
	
	@Autowired
	private Environment env;

    @GetMapping(path = "/trees")
    public ResponseEntity<TreeCount> getTreeData(@RequestParam double x, @RequestParam double y, @RequestParam double radius) {
    	String url = env.getProperty("app.datasource.endpoint");
    	if (radius < 0) {
    		throw new InvalidRadiusException();
    	}
    	/// API service that returns information about trees use feet as unit
    	/// for cartesian coordinates
    	double radiusInFeet = utils.convertMetersToFeet(radius);
    	
    	/// To decrease the number of results, limit the search range
    	/// API service supports searching rows based on the location data
    	/// However, for tree data there is no 'Location' data type
    	Boundaries boundaries = new Boundaries(x, y, radiusInFeet);
    	try {
    		/// Prepare query params for search area
    		/// We can filter results based on the given boundaries
    		String locationQueryParams = treeService.prepareQueryParamsForLocation(boundaries);
    		
    		/// Select only relevant fields
    		String fields = "spc_common, x_sp, y_sp";
    		
    		/// Set uris
    		URI uri = treeService.uriBuilder(url, fields, locationQueryParams, "", "");
        	URI[] uris = {uri};
        	fetchService.setURI(uris);
        	
        	/// Make request
			fetchService.fetchData();
			
			/// Read results
			List<String> responses = fetchService.getResponses();
			
			/// Calculate tree counts
			TreeCount treeCounts = treeService.calculateTreeCount(responses, x, y, radiusInFeet);
			return new ResponseEntity<TreeCount>(treeCounts, HttpStatus.OK);
		} catch (URISyntaxException e1) {
			e1.printStackTrace();
			throw new InvalidURLException();
		}
    	catch (Exception e) {
			e.printStackTrace();
			throw new UnexpectedServerErrorException();
		}
    	
    }
}

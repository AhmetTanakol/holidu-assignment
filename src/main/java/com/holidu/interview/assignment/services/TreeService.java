package com.holidu.interview.assignment.services;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import org.apache.http.client.utils.URIBuilder;
import org.json.JSONArray;
import org.json.JSONObject;

import com.holidu.interview.assignment.model.Boundaries;
import com.holidu.interview.assignment.model.TreeCount;
import com.holidu.interview.assignment.utils.Utils;

public class TreeService{
	private Utils utils = new Utils();
	
	/**
	 * Prepare query parameters to get trees for the given boundaries
	 * 
	 * @param boundaries
	 * @return
	 */
	public String prepareQueryParamsForLocation(Boundaries boundaries) {
		double maxXBoundary = boundaries.getMaxX();
		double minXBoundary = boundaries.getMinX();
		double maxYBoundary = boundaries.getMaxY();
		double minYBoundary = boundaries.getMinY();
		
		String whereClause = "x_sp <= " + Double.toString(maxXBoundary) + " AND " +
							 "x_sp >= " + Double.toString(minXBoundary) + " AND " +
							 "y_sp <= " + Double.toString(maxYBoundary) + " AND " +
							 "y_sp >= " + Double.toString(minYBoundary);
		
		return whereClause;
	}

	/**
	 * Create the URI to make a get request
	 * 
	 * @param url
	 * @param fields
	 * @param whereClause
	 * @param limit
	 * @param offset
	 * @return
	 * @throws URISyntaxException
	 */
	public URI uriBuilder(String url, String fields, String whereClause, String limit, String offset) throws URISyntaxException {
		if (url.isEmpty()) {
			throw new URISyntaxException(url, "URL is empty");
		}
		URIBuilder builder = new URIBuilder(url);

		if (!fields.isEmpty()) {
			builder.addParameter("$select", fields);	
		}
		
		if (!whereClause.isEmpty()) {
			builder.addParameter("$where", whereClause);
		}
		
		if (!limit.isEmpty()) {
			builder.addParameter("$limit", limit);
		}
		if (!offset.isEmpty()) {
			builder.addParameter("$offset", offset);
		}

		return builder.build();
	}

	
	/**
	 * Check if the tree is given radius
	 * If yes, increase the count
	 * 
	 * @param treeCounts
	 * @param response
	 * @param x
	 * @param y
	 * @param radius
	 */
	public void processResponse(TreeCount treeCounts,String response, double x, double y, double radius) {
		JSONArray treeData = new JSONArray(response);
		for (int i = 0; i < treeData.length(); i++) {
		    JSONObject treeCount = treeData.getJSONObject(i);
		    if (!treeCount.isNull("spc_common") && !treeCount.getString("spc_common").isEmpty()) {
		    	double x_sp = Double.parseDouble(treeCount.getString("x_sp"));
		    	double y_sp = Double.parseDouble(treeCount.getString("y_sp"));
		    	if (utils.isWithinRadius(x, y, x_sp, y_sp, radius)) {
			    	treeCounts.addTree(treeCount.getString("spc_common"), 1);	
		    	}
			}
		}
	}
	
	/**
	 * Calculate tree counts
	 * 
	 * @param responses
	 * @param x
	 * @param y
	 * @param radius
	 * @return
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	public TreeCount calculateTreeCount(List<String> responses, double x, double y, double radius) throws InterruptedException, ExecutionException {
		TreeCount treeCounts = new TreeCount();
		if (responses.isEmpty()) {
			return treeCounts;
		}
		/// Invoke parallelstream in ForkJoinPool
		/// Parallelstreams use common pool from ForkJoinPool, so we can increase the level of parallelism
		ForkJoinPool customThreadPool = new ForkJoinPool(4);
		customThreadPool.submit(() -> 
	    	responses.parallelStream().forEach(response -> processResponse(treeCounts, response, x, y, radius))).get();
		
		return treeCounts;
	}
}

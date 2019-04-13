package com.holidu.interview.assignment.services;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;

import org.apache.http.client.utils.URIBuilder;
import org.json.JSONArray;
import org.json.JSONObject;

import com.holidu.interview.assignment.utils.Utils;

public class TreeService {
	private Utils utils = new Utils();
	
	/**
	 * Create the URI to make a get request
	 * 
	 * @param url
	 * @param limit
	 * @param offset
	 * @param boundaries
	 * @return
	 * @throws URISyntaxException
	 */
	public URI uriBuilder(String url, JSONObject boundaries, String limit, String offset) throws URISyntaxException {
		URIBuilder builder = new URIBuilder(url);
		int maxXBoundary = utils.convertMetersToFeet(boundaries.getJSONObject("xBoundaries").getInt("max"));
		int minXBoundary = utils.convertMetersToFeet(boundaries.getJSONObject("xBoundaries").getInt("min"));
		int maxYBoundary = utils.convertMetersToFeet(boundaries.getJSONObject("yBoundaries").getInt("max"));
		int minYBoundary = utils.convertMetersToFeet(boundaries.getJSONObject("yBoundaries").getInt("min"));
		
		String whereClause = "x_sp <= " + maxXBoundary + " AND " +
							 "x_sp >= " + minXBoundary + " AND " +
							 "y_sp <= " + maxYBoundary + " AND " +
							 "y_sp >= " + minYBoundary;
		
		builder.addParameter("$select", "spc_common, count(spc_common)");
		builder.addParameter("$where", whereClause);
		builder.addParameter("$group", "spc_common");
		
		if (!limit.isEmpty()) {
			builder.addParameter("$limit", limit);
		}
		if (!offset.isEmpty()) {
			builder.addParameter("$offset", offset);
		}

		return builder.build();
	}
	
	public HashMap<String,Integer> formatResponse(List<String> responses) {
		HashMap<String, Integer> treeCounts = new HashMap<String, Integer>();
		//  { "red maple": 30, "American linden": 1, "London planetree": 3 } 
		responses.forEach((responseSet) -> {
			JSONArray treeData = new JSONArray(responseSet);
			for (int i = 0; i < treeData.length(); i++) {
			    JSONObject treeCount = treeData.getJSONObject(i);
			    if (!treeCount.isNull("spc_common") && !treeCount.isNull("count_spc_common")) {
					treeCounts.put(treeCount.getString("spc_common"),
								   Integer.parseInt(treeCount.getString("count_spc_common")));
				}
			}
		}); 
		
		return treeCounts;
	}

}

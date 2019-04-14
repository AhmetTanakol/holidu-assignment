package com.holidu.assignment.interview.services;

import static org.hamcrest.Matchers.is;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.holidu.interview.assignment.model.Boundaries;
import com.holidu.interview.assignment.model.TreeCount;
import com.holidu.interview.assignment.services.TreeService;


public class TreeServiceTest {
	TreeService treeService;
	TreeCount treeCounts;
	List<String> responses;

	@Before
	public void setTreeService() {
		treeService = new TreeService();
	}

	@Before
	public void setSampleFetchResultAndTreeCounts() {
		treeCounts = new TreeCount();
		responses = new ArrayList<String>();
		String sampleResponse =
				"[{\"spc_common\":\"crepe myrtle\",\"x_sp\":\"-1\",\"y_sp\":\"-1\"},"
						+ "{\"spc_common\":\"\"},"
						+ "{\"spc_common\": null},"
						+ "{\"x_sp\":\"-1\",\"y_sp\":\"-1\"},"
						+ "{\"spc_common\":\"hedge maple\",\"x_sp\":\"1\",\"y_sp\":\"1\"}]";
		responses.add(sampleResponse);
	}


	@Test
	public void createQueryParamsForLocation() {
		Boundaries boundaries = new Boundaries(0, 0 ,2);
		String actual = treeService.prepareQueryParamsForLocation(boundaries);
		String expected = "x_sp <= 2.0 AND x_sp >= -2.0 AND y_sp <= 2.0 AND y_sp >= -2.0";
		Assert.assertEquals("boundaries for both axis should be between 2 and -2",
				expected, actual);

	}

	@Test(expected = URISyntaxException.class)
	public void uriBuildFailsMissingUrl() throws URISyntaxException {
		treeService.uriBuilder("", "", "", "", "");
	}

	@Test(expected = Test.None.class /* no exception expected */)
	public void buidlURIWithFields() throws URISyntaxException {
		URI newURI = treeService.uriBuilder("https://localhost:8080", "name", "", "", "");
		Assert.assertEquals("should set host","localhost", newURI.getHost());
		Assert.assertEquals("should set port", 8080, newURI.getPort());
		Assert.assertEquals("should set fields","$select=name", newURI.getQuery());
	}

	@Test(expected = Test.None.class /* no exception expected */)
	public void buidlURIWithWhereClause() throws URISyntaxException {
		URI newURI = treeService.uriBuilder("https://localhost:8080", "", "x<2", "", "");
		Assert.assertEquals("should set where clause","$where=x<2", newURI.getQuery());
	}

	@Test(expected = Test.None.class /* no exception expected */)
	public void buidlURIWithLimit() throws URISyntaxException {
		URI newURI = treeService.uriBuilder("https://localhost:8080", "", "", "5", "");
		Assert.assertEquals("should set limit clause","$limit=5", newURI.getQuery());
	}

	@Test(expected = Test.None.class /* no exception expected */)
	public void buidlURIWithOffset() throws URISyntaxException {
		URI newURI = treeService.uriBuilder("https://localhost:8080", "", "", "", "5");
		Assert.assertEquals("should set offset clause","$offset=5", newURI.getQuery());
	}

	@Test
	public void processStringOfTreeData(){
		treeService.processResponse(treeCounts, responses.get(0), 0, 0, 2);
		Assert.assertTrue(!treeCounts.getTrees().isEmpty());
		Assert.assertThat(treeCounts.getTrees().size(), is(2));
		Assert.assertTrue(treeCounts.getTrees().containsKey("crepe myrtle"));
		Assert.assertTrue(treeCounts.getTrees().containsKey("hedge maple"));
		Assert.assertThat(treeCounts.getTrees().get("crepe myrtle"), is(1));
		Assert.assertThat(treeCounts.getTrees().get("hedge maple"), is(1));
	}

	@Test(expected = Test.None.class /* no exception expected */)
	public void calculateTreeCount() throws InterruptedException, ExecutionException{
		TreeCount countedTrees = treeService.calculateTreeCount(responses, 0, 0, 2);
		Assert.assertTrue(!countedTrees.getTrees().isEmpty());
		Assert.assertThat(countedTrees.getTrees().size(), is(2));
		Assert.assertTrue(countedTrees.getTrees().containsKey("crepe myrtle"));
		Assert.assertTrue(countedTrees.getTrees().containsKey("hedge maple"));
		Assert.assertThat(countedTrees.getTrees().get("crepe myrtle"), is(1));
		Assert.assertThat(countedTrees.getTrees().get("hedge maple"), is(1));
	}

	@Test(expected = Test.None.class /* no exception expected */)
	public void calculateTreeCountWhereResponsesIsEmpty() throws InterruptedException, ExecutionException{
		TreeCount countedTrees = treeService.calculateTreeCount(new ArrayList<String>(), 0, 0, 2);
		Assert.assertTrue(countedTrees.getTrees().isEmpty());
		Assert.assertThat(countedTrees.getTrees().size(), is(0));
	}

}

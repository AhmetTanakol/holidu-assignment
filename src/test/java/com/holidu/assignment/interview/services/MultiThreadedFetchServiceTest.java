package com.holidu.assignment.interview.services;

import static org.hamcrest.Matchers.is;


import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.holidu.interview.assignment.services.MultithreadedFetchService;
import com.holidu.interview.assignment.services.TreeService;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = ClassUsingProperty.class)
@TestPropertySource(locations="classpath:application.properties")
public class MultiThreadedFetchServiceTest {

	MultithreadedFetchService fetchService;
	TreeService treeService;

	@Autowired
	ClassUsingProperty classUsingProperty;

	@Before
	public void setServices() {
		fetchService = new MultithreadedFetchService();
		treeService = new TreeService();
	}

	@Test(expected = Test.None.class /* no exception expected */)
	public void setURIForFetchService() throws URISyntaxException {
		URI uri = treeService.uriBuilder("localhost:8080", "name", "", "", "");
		URI[] uris = {uri};
		fetchService.setURI(uris);
		Assert.assertArrayEquals(uris, fetchService.getURI());
	}

	@Test
	public void checkFetchServiceResponses() {
		Assert.assertTrue(fetchService.getResponses().isEmpty());
		Assert.assertThat(fetchService.getResponses().size(), is(0));
	}

	@Test(expected = Test.None.class /* no exception expected */)
	public void fetchDataFromURL() throws URISyntaxException {
		String url = classUsingProperty.retrieveURL();
		System.out.println(url);
		URI uri = treeService.uriBuilder(url, "spc_common", "", "1", "");
		URI[] uris = {uri};
		fetchService.setURI(uris);
		fetchService.fetchData();
		List<String> responses = fetchService.getResponses();
		Assert.assertTrue(!responses.isEmpty());
		Assert.assertThat(responses.size(), is(1));
	}
}

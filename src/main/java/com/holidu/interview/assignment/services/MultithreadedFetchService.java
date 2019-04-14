package com.holidu.interview.assignment.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;


/**
 * A multithreaded service that can fetch data from the same route by using up to 20 threads
 * 
 * 
 * @author ahmettanakol
 *
 */
public class MultithreadedFetchService {
	private URI[] uris;
	private CloseableHttpClient httpClient;
	private List<String> responses = new ArrayList<String>();

	public MultithreadedFetchService() {
        // Pool of client connections to serve connection requests from multiple execution threads
		// Works on a per route basis 
		PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
		// Set max connections to 100 for now
		cm.setMaxTotal(100);
		// Increase max connection per route to 20
		cm.setDefaultMaxPerRoute(20);
		this.httpClient = HttpClients.custom().setConnectionManager(cm).build();
	}
	
	public List<String> getResponses() {
		return this.responses;
	}
	
	public void setURI(URI[] uris) throws URISyntaxException {
		// URIs to perform GET requests on
		this.uris = uris;
	
	}
	
	public void fetchData() {
		// create a thread for each URL
		FetchThread[] threads = new FetchThread[this.uris.length];
		for (int i = 0; i < threads.length; i++) {
		    HttpGet httpget = new HttpGet(this.uris[i]);
		    threads[i] = new FetchThread(this.httpClient, httpget);
		}

		// start the threads
		for (int j = 0; j < threads.length; j++) {
		    threads[j].start();
		}

		// join the threads
		for (int j = 0; j < threads.length; j++) {
		    try {
				threads[j].join();
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public class FetchThread extends Thread {
        private final CloseableHttpClient httpClient;
        private final HttpGet request;
        private final HttpContext context;

        public FetchThread(CloseableHttpClient httpClient, HttpGet request) {
            super();
            this.httpClient = httpClient;
            this.request = request;
            this.context = HttpClientContext.create();
        }

        @Override
        public void run() {
            try {
                CloseableHttpResponse response = this.httpClient.execute(this.request, this.context);
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode != HttpStatus.SC_OK) {
                    System.err.println("Method failed: " + statusCode);
                }

                // Reading the status body.  
                try {
                    HttpEntity entity = response.getEntity();
                    BufferedReader rd = new BufferedReader(new InputStreamReader(entity.getContent()));
            		StringBuffer result = new StringBuffer();
            		String line = "";
            		while ((line = rd.readLine()) != null) {
            			result.append(line);
            		}
            		responses.add(result.toString());
                } finally {
                    response.close();
                }
            } catch (ClientProtocolException ex) {
            	System.err.println("Fatal transport error: " + ex.getMessage());
            } catch (IOException e) {
                System.err.println("Fatal transport error: " + e.getMessage());
            }
        }

    }
}

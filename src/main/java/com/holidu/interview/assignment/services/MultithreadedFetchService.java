package com.holidu.interview.assignment.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

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

public class MultithreadedFetchService {
	private String[] urls;
	private CloseableHttpClient httpClient;

	public MultithreadedFetchService(String[] urls) {
        // Pool of client connections to serve connection requests from multiple execution threads
		// Works on a per route basis 
		PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
		// Set max connections to 100 for now
		cm.setMaxTotal(100);
		// Increase max connection per route to 20
		cm.setDefaultMaxPerRoute(20);
		this.httpClient = HttpClients.custom().setConnectionManager(cm).build();
		
		// URIs to perform GETs on
		this.urls = urls;
	}
	
	public void fetchData() {
		// create a thread for each URI
		FetchThread[] threads = new FetchThread[this.urls.length];
		for (int i = 0; i < threads.length; i++) {
		    HttpGet httpget = new HttpGet(this.urls[i]);
		    threads[i] = new FetchThread(this.httpClient, httpget, "Thread-" + i);
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
	static class FetchThread extends Thread {
        private final CloseableHttpClient httpClient;
        private final HttpGet request;
        private final HttpContext context;

        public FetchThread(CloseableHttpClient httpClient, HttpGet request, String name) {
            super(name);
            this.httpClient = httpClient;
            this.request = request;
            this.context = HttpClientContext.create();
        }

        @Override
        public void run() {
            try {
                System.out.println(Thread.currentThread().getName() + " - about to get something from "
                        + this.request.getURI());


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
            		System.out.println(result.toString());
                } finally {
                    response.close();
                }
                System.out.println(Thread.currentThread().getName() + " " + statusCode);
            } catch (ClientProtocolException ex) {
            	System.err.println("Fatal transport error: " + ex.getMessage());
            } catch (IOException e) {
                System.err.println("Fatal transport error: " + e.getMessage());
            }
        }

    }
}

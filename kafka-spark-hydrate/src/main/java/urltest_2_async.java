package com.gap.first;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.BoundRequestBuilder;
import org.asynchttpclient.Dsl;
import org.asynchttpclient.Request;
import org.asynchttpclient.Response;

import java.util.ArrayList;
import java.util.List;

class urltest_2_async {
	//private static final int numThreads =8;
	private static String URL = "http://192.168.158.1:8080/demo/user";
	public static void main(String args[]) throws Exception {
		long pid = getPID();
		System.out.println(pid);
		Thread.currentThread().sleep(20000);
		AsyncHttpClient client = Dsl.asyncHttpClient();
		
		String[] urls= new String[1000];
		for(int i=0;i<1000;i++){
			urls[i]=URL+"/"+i;
		}
		
		List <Future<Response>> list = new ArrayList<Future<Response>>();
 
		long startTime = System.currentTimeMillis();

		for (int i = 0; i < urls.length; i++) {
 
			String url = urls[i];
			//BoundRequestBuilder request = client.prepareGet(url);
			Request request = Dsl.get(url).build();
			Future<Response> responseFuture = client.executeRequest(request);
			list.add(responseFuture);
		}
		for(Future<Response> fut : list){
			System.out.println(fut.get());

		}		
		client.close();
		long endTime   = System.currentTimeMillis();
		double totalTime = (endTime - startTime)/1000.0;
		System.out.println(totalTime);   
		
	}
 	
	public static long getPID() {
	    String processName = java.lang.management.ManagementFactory.getRuntimeMXBean().getName();
	    if (processName != null && processName.length() > 0) {
	        try {
	            return Long.parseLong(processName.split("@")[0]);
	        }
	        catch (Exception e) {
	            return 0;
	        }
	    }
	
	    return 0;
	}
}

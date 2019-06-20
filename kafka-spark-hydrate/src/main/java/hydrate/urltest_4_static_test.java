package com.gap.first;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.asynchttpclient.AsyncCompletionHandler;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.BoundRequestBuilder;
import org.asynchttpclient.Dsl;
import org.asynchttpclient.Request;
import org.asynchttpclient.Response;

import java.util.ArrayList;
import java.util.List;

class urltest_4_static_test {
	
	//private static final int numThreads =8;
	private static String URL = "http://192.168.158.1:8080/demo/user";
	static AsyncHttpClient client = Dsl.asyncHttpClient();
	
	public static void main(String args[]) throws Exception {

		long pid = getPID();
		System.out.println(pid);
		//Thread.currentThread().sleep(20000);

		/*String[] urls= { "http://crunchify.com", "http://yahoo.com", "http://www.ebay.com", "http://google.com", 
				"https://paypal.com", "http://bing.com/", "http://techcrunch.com/", "http://mashable.com/",
				"http://thenextweb.com/", "http://wordpress.com/", "http://wordpress.org/", "http://example.com/", "http://sjsu.edu/",
				"http://ebay.co.uk/", "http://wikipedia.org/", "http://en.wikipedia.org"};
		*/
		String[] urls= new String[1000];
		for(int i=0;i<1000;i++){
			urls[i]=URL+"/"+i;
		}		
		
		while(true) {
			List <Future<Response>> list = new ArrayList<Future<Response>>();
			
			long startTime = System.currentTimeMillis();
	
			for (int i = 0; i < urls.length; i++) {
				long starTime = System.currentTimeMillis();
	 
				String url = urls[i];
				/*boolean flag=false;
				if(i==urls.length-1)
					flag=true;*/
				asyncOutput(url,list);
				long enTime   = System.currentTimeMillis();
				double totaTime = (enTime - starTime)/1000.0;
				System.out.println(totaTime);   
			}
			
			for(Future<Response> fut : list){
				System.out.println(fut.get());
			}		
	
			long endTime   = System.currentTimeMillis();
			double totalTime = (endTime - startTime)/1000.0;
			System.out.println(totalTime);   
			//client.close();
			//System.exit(0);
		}
	}
	
	public static void asyncOutput(String url, List<Future<Response>>list) throws Exception {
		Request request = client
                .prepareGet(String.format(url))
                .build();
		
		Future<Response> responseFuture = client.executeRequest(request);
		
		//responseFuture.get();
		list.add(client.executeRequest(request, new AsyncCompletionHandler<Response>() {
            @Override
            public Response onCompleted(Response response) throws Exception {
                return response;
            }
            @Override
            public void onThrowable(Throwable t) {
            	System.out.println("Error");
            }
        }));		
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

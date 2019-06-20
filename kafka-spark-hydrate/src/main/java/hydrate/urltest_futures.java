package com.gap.first;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.ArrayList;
import java.util.List;

class urltest_futures {
	private static final int numThreads =12;
	private static String URL = "http://192.168.158.1:8080/demo/user";
	public static void main(String args[]) throws Exception {
		ExecutorService executor = Executors.newFixedThreadPool(numThreads);
		List <Future<String>> list = new ArrayList<Future<String>>();
		String[] urls= new String[48];
		for(int i=0;i<48;i++){
			urls[i]=URL+"/"+i;
		}
 
		long startTime = System.currentTimeMillis();

		for (int i = 0; i < urls.length; i++) {
 
			String url = urls[i];
			Future<String> worker = executor.submit(new CallableUrl(url));
			list.add(worker);
			//executor.submit(worker);
		}
		executor.shutdown();
		for(Future<String> fut : list){
			try{
				long st = System.currentTimeMillis();
				//if(fut.isDone()){
					System.out.println(fut.get());
					System.out.println(System.currentTimeMillis()-st);
				//}

			} 
			catch(InterruptedException e){                                               
				e.printStackTrace();
			}
			/*catch (ExecutionException e) {
				e.printStackTrace();
			}*/
		}        
		long endTime   = System.currentTimeMillis();
		double totalTime = (endTime - startTime)/1000.0;
		System.out.println(totalTime);               
		System.out.println("\nFinished all threads");
	}
 
	public static class CallableUrl implements Callable {
		
		private final String url;
		 
		CallableUrl(String url) {
			this.url = url;
		}
 
		@Override
		public String call() throws Exception {
 
			String result="";
			
			int code = 200;
			try {
				URL siteURL = new URL(url);
				HttpURLConnection connection = (HttpURLConnection) siteURL.openConnection();
				connection.setRequestMethod("GET");
				connection.setConnectTimeout(3000);
				connection.connect();
 
				code = connection.getResponseCode();
				if (code == 200) {
					result = "-> Green <-\t" + "Code: " + code;
					;
				} else {
					result = "-> Yellow <-\t" + "Code: " + code;
				}
			} catch (Exception e) {
				result = "-> Red <-\t" + "Wrong domain - Exception: " + e.getMessage();
			}
			return (url + "\t\tStatus:" + result);
		}
	}
}

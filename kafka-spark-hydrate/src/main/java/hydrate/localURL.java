package com.gap.first;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.ArrayList;
import java.util.List;

import org.apache.spark.api.java.*;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.streaming.StreamingQueryException;

 class localURL {

	private static String TOPICNAME = "hellokafka";
	private static String OFFSET = "earliest";
	private static String BROKERID = "localhost:9092";
	private static String URL="http://192.168.158.1:8080/demo/users";
	
	public static void main(String[] args) throws Exception {
		SparkSession spark = SparkSession
				.builder()
				.appName("SalesRealTimestreaming")
				.master("local[4]")												
				.config("spark.driver.allowMultipleContexts", "true").getOrCreate();
				spark.sparkContext().setLogLevel("WARN");
				
		JavaSparkContext sc = new JavaSparkContext(spark.sparkContext());
		
		List<String> urls= new ArrayList();
		for(int i=0;i<50;i++) {
			urls.add(URL);
		}
		JavaRDD<String> data=sc.parallelize(urls);
		//JavaRDD<Object> lines = data.map(x -> x.split(" "));
		System.out.println(data.count());
		
		 for(String n:data.collect()){
	            System.out.println(n);
	     }
	}
 }
 
 
 
 
 
 
 
 
 
 
 
 
package com.gap.first;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.streaming.StreamingQueryException;

public class SparkStreamingKafkaConsumer {

	private static String TOPICNAME = "hellokafka";
	private static String OFFSET = "earliest";
	private static String BROKERID = "localhost:9092";
	
	public static void main(String[] args) throws StreamingQueryException {
		SparkSession spark = SparkSession
				.builder()
				.appName("SalesRealTimestreaming")
				.master("local[4]")												//TODO : comment in cluster mode
				.config("spark.driver.allowMultipleContexts", "true").getOrCreate();
				spark.sparkContext().setLogLevel("WARN");
				
		Dataset<Row> inputStream = spark.readStream().format("kafka")
				.option("kafka.bootstrap.servers", BROKERID)
				.option("subscribe", TOPICNAME)
				 .option("startingOffsets", OFFSET)
				// .option("enable.auto.commit", true)
				.load();
		
		Dataset<Row> streamWithSchema = inputStream.selectExpr("CAST(value AS STRING) as value","topic","partition",
				"offset","timestamp","timestampType");
		
		streamWithSchema.createOrReplaceTempView("streamWithSchemaObject");
		spark.sql("SELECT * FROM streamWithSchemaObject")
		.writeStream()
		.outputMode("append")
		.format("console")
		.start();
		spark.streams().awaitAnyTermination();
	}
	
	
}

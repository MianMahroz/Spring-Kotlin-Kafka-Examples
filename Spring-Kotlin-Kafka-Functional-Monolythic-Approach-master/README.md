# Kafka-kotlin-Consumer-Producer-Processor (ALL IN ONE)


A simple application that shows the working of producer ,consumer and processor at same time.

We produce data using **FAKER JAVA LIBRARY**.

yml based kafka configuration.

Rest Endpoint is also available to query data from store.


# Notes
- Consumer,Producer & Processor are **FUNCTIONAL INTERFACES** (Such interface that contains only one abstract method)


# KEY VALUE STORE

- Processor takes and return streams of data,  in between you can manipulate data as you want.

- This example uses **Materialized.`as`** to provide the definition of key value store.

- Kafka by default uses **ROCKSDB** to create key value store.

- You can query data from any store by store name using InteractiveQueryService


  i.e 
        val store=iqService.getQueryableStore("word-count-state-store",QueryableStoreTypes.keyValueStore<String,Long>())


**METHODS TO CREATE KEY VALUE STORE**



**Example 1**


        @Bean
	fun processWords(): Function<KStream<String?, String>, KStream<String, String>> {
		return Function { inputStream: KStream<String?, String> ->
                
			val countsStream = inputStream
				.flatMapValues { value:String->value.lowercase().split("\\W".toRegex()) }
				.map{_:String?,value:String -> KeyValue(value,value)}
				.groupByKey(Grouped.with(Serdes.String(),Serdes.String()))
				.count(Materialized.`as`("word-count-state-store"))       
				.mapValues { value:Long -> value.toString() }
				.toStream();
			
                        countsStream.to("counts", Produced.with(Serdes.String(),Serdes.String()));
			countsStream
		}
	}
  
  
**EXAMPLE 2**

-You can read a topic as KTable and force a state store materialization to access the content via Interactive Queries API:


         StreamsBuilder builder = new StreamsBuilder();
         KTable<Integer, Integer> table = builder.table(
           "topicName",
           Materialized.as("queryable-store-name"));
  
  



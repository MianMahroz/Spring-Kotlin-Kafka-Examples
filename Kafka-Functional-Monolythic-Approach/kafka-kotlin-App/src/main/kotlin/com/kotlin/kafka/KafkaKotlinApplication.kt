package com.kotlin.kafka

import com.github.javafaker.Faker
import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.KeyValue
import org.apache.kafka.streams.kstream.Grouped
import org.apache.kafka.streams.kstream.KStream
import org.apache.kafka.streams.kstream.Materialized
import org.apache.kafka.streams.kstream.Produced
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.messaging.Message
import org.springframework.messaging.support.MessageBuilder
import java.util.function.Consumer
import java.util.function.Function
import java.util.function.Supplier

@SpringBootApplication
class KafkaKotlinApplication{


//	Functional Interface (such interface that contains only one abstract method) for producer
	@Bean
	fun produceChuckNorris(): Supplier<Message<String>> {
		return Supplier { MessageBuilder.withPayload(Faker.instance().chuckNorris().fact()).build() }
	}

//	Consumer
	@Bean
	fun consumeChuckNorris(): Consumer<Message<String>> {
		return Consumer { s: Message<String> -> println("FACT: \u001B[3m «" + s.payload + "\u001B[0m»") }
	}

//  Counts topic consumer
	@Bean
	fun countsConsumer(): Consumer<Message<String>> {
		return Consumer { s: Message<String> -> println("COUNTS: \u001B[3m «" + s.payload + "\u001B[0m»") }
	}

//	processor
	@Bean
	fun processWords(): Function<KStream<String?, String>, KStream<String, String>> {
		return Function { inputStream: KStream<String?, String> ->
			val countsStream = inputStream
				.flatMapValues { value:String->value.lowercase().split("\\W".toRegex()) }
//				.peek { _, v -> println(v) }
				.map{_:String?,value:String -> KeyValue(value,value)}
				.groupByKey(Grouped.with(Serdes.String(),Serdes.String()))
				.count(Materialized.`as`("word-count-state-store"))
				.mapValues { value:Long -> value.toString() }
				.toStream();

			countsStream.to("counts", Produced.with(Serdes.String(),Serdes.String()));
			countsStream
		}
	}
}


fun main(args: Array<String>) {
	runApplication<KafkaKotlinApplication>(*args)

}

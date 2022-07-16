package com.mahroz.processor.kafkaprocessor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
public class KafkaProcessorApplication {

	public static void main(String[] args) {
		SpringApplication.run(KafkaProcessorApplication.class, args);
	}

}

package com.mahroz.consumer.kafkaconsumer;

import com.mahroz.producer.kafkaproducer.Domain;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
public class DomainConsumer {

    @Bean
    public Consumer<KStream<String, Domain>> domainService(){
        return stringDomainKStream -> stringDomainKStream.foreach((key,value)->{
            System.out.println("DOMAIN CONSUMED "+value.getDomain());
            System.out.println("DOMAIN STATUS "+value.isDead());

        });
    }

}

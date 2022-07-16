package com.mahroz.processor.kafkaprocessor;


import com.mahroz.producer.kafkaproducer.Domain;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Function;

@Configuration
public class DomainKafkaProcessor {


    @Bean
    public Function<KStream<String, Domain>,KStream<String,Domain>> domainProcessor(){
        return kstream-> kstream.filter((key,value)-> {
            if(!value.isDead()){
                System.out.println("ACTIVE DOMAINS"+value.getDomain());
            }else {
                System.out.println("INACTIVE DOMAINS"+value.getDomain());
            }
            return !value.isDead();

        });
    }



}

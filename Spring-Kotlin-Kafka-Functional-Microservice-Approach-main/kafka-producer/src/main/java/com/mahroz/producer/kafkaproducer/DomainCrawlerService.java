package com.mahroz.producer.kafkaproducer;

import org.springframework.http.MediaType;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class DomainCrawlerService {

    private static String KAFKA_WEB_DOMAIN_TOPIC="web-domains";
    private KafkaTemplate<String, Domain> kafkaTemplate;

    public DomainCrawlerService(KafkaTemplate<String, Domain> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void crawl(final String name){
       Mono<DomainDto> domainListMono= WebClient.create().get()
                .uri("https://api.domainsdb.info/v1/domains/search?domain="+name)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(DomainDto.class);

       domainListMono.subscribe(domainDto -> {
          domainDto.getDomains().forEach(domain -> {
              kafkaTemplate.send(KAFKA_WEB_DOMAIN_TOPIC,domain);
              System.out.println("DOMAIN MSG"+domain.getDomain());
          });
       });

    }

}

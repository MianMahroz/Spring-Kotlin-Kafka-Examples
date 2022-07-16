package com.mahroz.producer.kafkaproducer;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProducerController {

    private DomainCrawlerService domainCrawlerService;

    public ProducerController(DomainCrawlerService domainCrawlerService) {
        this.domainCrawlerService = domainCrawlerService;
    }

    @GetMapping("/domains")
    public String getDomains(@RequestParam (name="q")String query){
        domainCrawlerService.crawl(query);
        return "domain crawler has scraped your data";
    }
}

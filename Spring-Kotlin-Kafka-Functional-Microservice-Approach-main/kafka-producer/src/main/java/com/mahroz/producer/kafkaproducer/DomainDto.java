package com.mahroz.producer.kafkaproducer;

import java.util.List;

public class DomainDto {
    private List<Domain> domains;

    public List<Domain> getDomains() {
        return domains;
    }

    public void setDomains(List<Domain> domains) {
        this.domains = domains;
    }
}

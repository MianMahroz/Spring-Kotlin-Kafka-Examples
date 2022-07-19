package com.mahroz.producer.kafkaproducer;

public class Domain {

    private String domain;
    private boolean isDead;

    public Domain() {
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public boolean isDead() {
        return isDead;
    }

    public void setDead(boolean dead) {
        isDead = dead;
    }
}

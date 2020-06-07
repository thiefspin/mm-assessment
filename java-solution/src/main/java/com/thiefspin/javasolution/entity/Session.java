package com.thiefspin.javasolution.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Getter
@NoArgsConstructor
@Entity(name = "sessions")
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String sessionId;
    private String msisdn;
    private String currentMenu;
    private Long countryId;
    private Double amount;
    private boolean completed;
    private Date lastUpdated;

    public Session(String sessionId, String msisdn, String currentMenu, Long countryId) {
        this.sessionId = sessionId;
        this.msisdn = msisdn;
        this.currentMenu = currentMenu;
        this.countryId = countryId;
        this.lastUpdated = new Date();
    }

    public Session complete() {
        this.completed = true;
        return this;
    }

    public Session setAmount(Double amount) {
        this.amount = amount;
        return this;
    }

    public Session setMenu(String menu) {
        this.currentMenu = menu;
        return this;
    }
}

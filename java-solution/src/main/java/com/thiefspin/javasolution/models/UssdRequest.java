package com.thiefspin.javasolution.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UssdRequest {

    private String sessionId;
    private String msisdn;
    private String userEntry;

}

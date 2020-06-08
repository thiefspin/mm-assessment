package com.thiefspin.javasolution.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UssdResponse {

    private String sessionId;
    private String message;

}

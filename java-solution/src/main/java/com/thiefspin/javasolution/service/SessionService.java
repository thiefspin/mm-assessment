package com.thiefspin.javasolution.service;

import com.thiefspin.javasolution.models.UssdRequest;
import com.thiefspin.javasolution.models.UssdResponse;

import java.util.Optional;

public interface SessionService {

    Optional<UssdResponse> handleRequest(UssdRequest req);
}

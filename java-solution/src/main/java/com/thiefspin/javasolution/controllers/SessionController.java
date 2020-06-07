package com.thiefspin.javasolution.controllers;

import com.thiefspin.javasolution.controllers.responses.NotFoundException;
import com.thiefspin.javasolution.models.UssdRequest;
import com.thiefspin.javasolution.models.UssdResponse;
import com.thiefspin.javasolution.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class SessionController {

    @Autowired
    private SessionService service;

    @PostMapping(value = "/api/ussd", produces = MediaType.APPLICATION_JSON_VALUE)
    public UssdResponse ussdRequest(@RequestBody UssdRequest req) {
        Optional<UssdResponse> result = service.handleRequest(req);
        if (result.isPresent()) {
            return result.get();
        }
        throw new NotFoundException();
    }
}

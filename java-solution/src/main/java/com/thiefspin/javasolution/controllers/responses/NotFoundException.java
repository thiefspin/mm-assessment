package com.thiefspin.javasolution.controllers.responses;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Requested data was not found")
public class NotFoundException extends RuntimeException {
}

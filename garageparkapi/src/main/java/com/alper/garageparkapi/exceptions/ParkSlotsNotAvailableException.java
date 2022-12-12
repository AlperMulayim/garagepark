package com.alper.garageparkapi.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class ParkSlotsNotAvailableException extends  RuntimeException {
    public ParkSlotsNotAvailableException(String message){
        super(message);
    }
}

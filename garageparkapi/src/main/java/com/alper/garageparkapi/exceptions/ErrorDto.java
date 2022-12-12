package com.alper.garageparkapi.exceptions;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorDto {
    private String message;
    private String date;
}

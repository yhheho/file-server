package com.woven.file.server.model;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorResponse {
    public String errorCode;
    public String message;
}

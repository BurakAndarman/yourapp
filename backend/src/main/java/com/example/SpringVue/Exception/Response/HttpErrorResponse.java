package com.example.SpringVue.Exception.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HttpErrorResponse extends ErrorResponse{

    private Map<String,String> providerResponse;

    public HttpErrorResponse(int status, String message, long timeStamp, Map<String,String> providerResponse) {
        super(status, message, timeStamp);
        this.providerResponse = providerResponse;
    }

    public HttpErrorResponse(int status, String message, long timeStamp) {
        super(status, message, timeStamp);
    }

}
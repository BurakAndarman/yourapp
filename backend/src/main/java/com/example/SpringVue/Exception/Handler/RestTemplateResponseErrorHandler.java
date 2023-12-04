package com.example.SpringVue.Exception.Handler;

import com.example.SpringVue.Dto.NewsApi.Everything.Everything;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

@Component
public class RestTemplateResponseErrorHandler implements ResponseErrorHandler {

    @Override
    public boolean hasError(ClientHttpResponse httpResponse)
            throws IOException {

        return (httpResponse.getStatusCode().is4xxClientError() || httpResponse.getStatusCode().is5xxServerError());
    }

    @Override
    public Everything handleError(ClientHttpResponse httpResponse) throws IOException {

        Everything everything = new Everything();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode nameNode = mapper.readTree(httpResponse.getBody());

        everything.setStatus(nameNode.get("status").asText());
        everything.setCode(nameNode.get("code").asText());
        everything.setMessage(nameNode.get("message").asText());

        return everything;
    }
}

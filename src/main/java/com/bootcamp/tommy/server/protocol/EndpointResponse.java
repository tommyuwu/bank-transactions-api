package com.bootcamp.tommy.server.protocol;

public class EndpointResponse<T> extends EndpointEnvelope<T> {
    public EndpointResponse status(String id, T message) {
        this.setId(id);
        this.setMessage(message);
        return this;
    }
}

package com.bootcamp.tommy.server.protocol;

public class EndpointEnvelope<T> {
    private String id;
    private T message;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public T getMessage() {
        return message;
    }

    public void setMessage(T message) {
        this.message = message;
    }
}

package de.dhbw.modellbahn.adapter.api;

public class SendDTO {
    private Integer hashValue;
    private Boolean response;

    public void setHashValue(Integer hashValue) {
        this.hashValue = hashValue;
    }

    public void setResponse(Boolean response) {
        this.response = response;
    }
}

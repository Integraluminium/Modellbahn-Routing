/*
 * FastAPI
 * No description provided (generated by Openapi Generator https://github.com/openapitools/openapi-generator)
 *
 * The version of the OpenAPI document: 0.1.0
 *
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */


package de.dhbw.modellbahn.adapter.physical.railway.communication.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.Objects;

/**
 * SystemStopCommand
 */
@JsonPropertyOrder({
        SystemStopCommand.JSON_PROPERTY_HASH_VALUE,
        SystemStopCommand.JSON_PROPERTY_RESPONSE,
        SystemStopCommand.JSON_PROPERTY_ID
})
public class SystemStopCommand {
    public static final String JSON_PROPERTY_HASH_VALUE = "hash_value";
    public static final String JSON_PROPERTY_RESPONSE = "response";
    public static final String JSON_PROPERTY_ID = "id";
    private Integer hashValue;
    private Boolean response;
    private Integer id;

    public SystemStopCommand(Integer hashValue, Boolean response, Integer id) {
        this.hashValue = hashValue;
        this.response = response;
        this.id = id;
    }

    public SystemStopCommand() {
    }

    public SystemStopCommand hashValue(Integer hashValue) {
        this.hashValue = hashValue;
        return this;
    }

    /**
     * Get hashValue
     *
     * @return hashValue
     */
    @JsonProperty(JSON_PROPERTY_HASH_VALUE)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public Integer getHashValue() {
        return hashValue;
    }


    @JsonProperty(JSON_PROPERTY_HASH_VALUE)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setHashValue(Integer hashValue) {
        this.hashValue = hashValue;
    }


    public SystemStopCommand response(Boolean response) {
        this.response = response;
        return this;
    }

    /**
     * Get response
     *
     * @return response
     */
    @JsonProperty(JSON_PROPERTY_RESPONSE)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public Boolean getResponse() {
        return response;
    }


    @JsonProperty(JSON_PROPERTY_RESPONSE)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setResponse(Boolean response) {
        this.response = response;
    }


    public SystemStopCommand id(Integer id) {
        this.id = id;
        return this;
    }

    /**
     * Get id
     *
     * @return id
     */
    @JsonProperty(JSON_PROPERTY_ID)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public Integer getId() {
        return id;
    }


    @JsonProperty(JSON_PROPERTY_ID)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(hashValue, response, id);
    }

    /**
     * Return true if this SystemStopCommand object is equal to o.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SystemStopCommand systemStopCommand = (SystemStopCommand) o;
        return Objects.equals(this.hashValue, systemStopCommand.hashValue) &&
                Objects.equals(this.response, systemStopCommand.response) &&
                Objects.equals(this.id, systemStopCommand.id);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class SystemStopCommand {\n");
        sb.append("    hashValue: ").append(toIndentedString(hashValue)).append("\n");
        sb.append("    response: ").append(toIndentedString(response)).append("\n");
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces
     * (except the first line).
     */
    private String toIndentedString(Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }


}


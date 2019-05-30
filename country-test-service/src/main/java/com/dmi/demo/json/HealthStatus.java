package com.dmi.demo.json;

import lombok.Data;

/**
 * @author Sophea <a href='mailto:smak@dminc.com'> sophea </a>
 * @date Jan-2019
 */
@Data
public class HealthStatus {
    /**check webserver ok*/
    private boolean webserverOK;
    /** Quoted "Z" to indicate UTC, no timezone offset - yyyy-MM-dd'T'HH:mm'Z' */
    private String localTime;
    /** Quoted "Z" to indicate UTC, no timezone offset - yyyy-MM-dd'T'HH:mm'Z' */
    private String serverTime;
    /** version of MW */
    private String version;

    private String applicationId;

    public boolean isWebserverOK() {
        return webserverOK;
    }

}

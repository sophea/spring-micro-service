package com.sma.demo.web;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sma.demo.json.HealthStatus;

import io.swagger.annotations.Api;

/**
 * @author Sophea <a href='mailto:smak@dminc.com'> sophea </a>
 * @version $id$ - $Revision$
 * @date Jan-2019
 */
@RestController
@RequestMapping(value = "api/healthcheck")
@Api(value = "healthcheck", description = "Healh check Service")
public class HealthCheckController {

    @Value(value = "${application.version}")
    private String version;

    @Value(value = "${application.id}")
    private String applicationId;

    @RequestMapping(value = "/v1", method = RequestMethod.GET)
    public HealthStatus getHealthCheck(HttpServletRequest request) {
        final HealthStatus status = new HealthStatus();
        status.setVersion(version);
        status.setApplicationId(applicationId);
        status.setWebserverOK(true); // If we are here, definitely webserver is running.
        status.setServerTime(renderDate(new Date(System.currentTimeMillis())));
        status.setLocalTime(renderDate(new Date()));

        return status;
    }

    private String renderDate(Date date) {
        final TimeZone tz = TimeZone.getTimeZone("UTC");
        // Quoted "Z" to indicate UTC, no timezone offset
        final DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'", Locale.ENGLISH);
        df.setTimeZone(tz);
        return df.format(date);
    }
}

package com.sma.demo.web;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sma.demo.json.JCountry;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author Sophea <a href='mailto:smak@dminc.com'> sophea </a>
 * @version $id$ - $Revision$
 * @date Jan-2019
 */
@RestController
@RequestMapping(value = "api/countries")
@Api(tags = "Countries Management", description = "Microservice for Country Management")
public class CountryController {

    @RequestMapping(value = "/v1", method = RequestMethod.GET)
    @ApiOperation(value = "get all countries")
    public Collection<JCountry> getCountries(HttpServletRequest request) {
        final Collection<JCountry> list = new ArrayList<>();
        list.add(JCountry.builder().code("KHM").name("Cambodia").build());
        list.add(JCountry.builder().code("IND").name("India").build());
        list.add(JCountry.builder().code("SPA").name("Spain").build());
        return list;
    }

}

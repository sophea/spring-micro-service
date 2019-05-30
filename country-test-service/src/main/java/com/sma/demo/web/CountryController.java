package com.sma.demo.web;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sma.demo.datastore.CountryDSRepository;
import com.sma.demo.domain.DCountry;
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

    @Autowired
    private CountryDSRepository dao;

    @RequestMapping(value = "/v1", method = RequestMethod.GET)
    @ApiOperation(value = "get all countries")
    public Iterable<DCountry> getCountries() {
        return dao.findAll();
    }

    @RequestMapping(value = "/v1", method = RequestMethod.POST)
    @ApiOperation(value = "get all countries")
    public JCountry getCountries(@RequestBody JCountry request) {

        dao.save(DCountry.builder().code(request.getCode()).name(request.getName()).build());

        return request;
    }

}

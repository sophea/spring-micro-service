package com.sma.demo.web;

import com.sma.demo.domain.DCountry;
import com.sma.demo.json.JCountry;
import com.sma.demo.service.CountryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Sophea <a href='mailto:smak@dminc.com'> sophea </a>
 * @version $id$ - $Revision$
 * @date Jan-2019
 */
@RestController
@RequestMapping(value = "api/countries")
@Api(tags = "Countries Management", description = "Microservice for Country Management")
public class CountryController {

   // @Autowired
    //private CountryDSRepository dao;
    @Autowired
    private CountryService service;

    @RequestMapping(value = "/v1", method = RequestMethod.GET)
    @ApiOperation(value = "get all countries")
    public Iterable<DCountry> getCountries() {
        return service.getAll();
    }

    @RequestMapping(value = "/v1", method = RequestMethod.POST)
    @ApiOperation(value = "get all countries")
    public JCountry createCountry(@RequestBody JCountry request) {

     //   dao.save(DCountry.builder().code(request.getCode()).name(request.getName()).build());

        return request;
    }

}

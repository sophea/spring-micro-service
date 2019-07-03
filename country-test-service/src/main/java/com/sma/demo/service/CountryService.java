package com.sma.demo.service;

import com.sma.demo.domain.DCountry;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class CountryService {

    public List<DCountry> getAll() {

        return Arrays.asList(DCountry.builder().code("KH").name("Cambodia").build(),
                DCountry.builder().code("SPA").name("Spain").build());
    }
}

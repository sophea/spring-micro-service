package com.sma.demo.domain;

import org.springframework.cloud.gcp.data.datastore.core.mapping.Entity;
import org.springframework.data.annotation.Id;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Entity(name = "countries")
public class DCountry {
    @Id
    private String code;

    private String name;
}

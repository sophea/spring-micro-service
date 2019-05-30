package com.dmi.demo.json;

import lombok.Builder;
import lombok.Data;

@Data(staticConstructor = "of")
@Builder
public class JCountry {
    private String code;
    private String name;

}

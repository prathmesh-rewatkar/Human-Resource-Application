package com.example.HumanResourceApplication.projection;

import com.example.HumanResourceApplication.entity.Location;
import org.springframework.data.rest.core.config.Projection;

@Projection(name = "locationView", types = { Location.class })
public interface LocationProjection {

    String getStreetAddress();
    String getPostalCode();
    String getCity();
    String getStateProvince();
}

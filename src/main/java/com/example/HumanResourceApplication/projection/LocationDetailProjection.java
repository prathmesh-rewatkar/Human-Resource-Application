package com.example.HumanResourceApplication.projection;

import com.example.HumanResourceApplication.entity.Country;
import com.example.HumanResourceApplication.entity.Region;
import org.springframework.data.rest.core.config.Projection;
import com.example.HumanResourceApplication.entity.Location;

@Projection(name = "locationDetail", types = {Location.class})
public interface LocationDetailProjection {
    Integer getLocationId();
    String getStreetAddress();
    String getCity();
    String getPostalCode();
    String getStateProvince();

    CountryInfo getCountry(); // nested projection for Country

    interface CountryInfo {
        String getCountryId();
        String getCountryName();
        RegionInfo getRegion(); // nested projection for Region
    }

    interface RegionInfo {
        Integer getRegionId() ;
        String getRegionName();
    }
}

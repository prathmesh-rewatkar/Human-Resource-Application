package com.example.HumanResourceApplication.event;

import com.example.HumanResourceApplication.entity.Country;
import com.example.HumanResourceApplication.exception.DuplicateEntityException;
import com.example.HumanResourceApplication.repository.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;

@Component
@RepositoryEventHandler(Country.class)
public class CountryEventHandler {

    @Autowired
    private CountryRepository countryRepository;

    @HandleBeforeCreate
    public void handleBeforeCreate(Country country) {
        if (countryRepository.existsById(country.getCountryId())) {
            throw new DuplicateEntityException(
                    "Country with ID " + country.getCountryId() + " already exists"
            );
        }
    }
}
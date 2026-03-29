package com.example.HumanResourceApplication.event;

import com.example.HumanResourceApplication.entity.Region;
import com.example.HumanResourceApplication.exception.DuplicateEntityException;
import com.example.HumanResourceApplication.repository.RegionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;

@Component
@RepositoryEventHandler(Region.class)
public class RegionEventHandler {

    @Autowired
    private RegionRepository regionRepository ;
    @HandleBeforeCreate
    public void handleBeforeCreate(Region region) {
        if (regionRepository.existsById(region.getRegionId())) {
            throw new DuplicateEntityException(
                    "Region with ID " + region.getRegionId() + " already exists"
            );
        }
    }

}

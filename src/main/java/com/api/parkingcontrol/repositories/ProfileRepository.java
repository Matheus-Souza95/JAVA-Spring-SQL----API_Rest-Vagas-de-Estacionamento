package com.api.parkingcontrol.repositories;

import com.api.parkingcontrol.models.CarModel;
import com.api.parkingcontrol.security.ProfileAccess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileRepository extends JpaRepository<ProfileAccess, Long> {

    ProfileAccess findByName(String name);
}


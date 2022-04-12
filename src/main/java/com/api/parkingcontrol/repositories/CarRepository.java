package com.api.parkingcontrol.repositories;

import com.api.parkingcontrol.models.CarModel;
import com.api.parkingcontrol.models.UserModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface CarRepository extends JpaRepository<CarModel, Long>, JpaSpecificationExecutor<CarModel> {

    boolean existsByLicensePlate(String string);

    CarModel findByLicensePlate(String number);

}


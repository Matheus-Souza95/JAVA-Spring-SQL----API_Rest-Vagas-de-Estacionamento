package com.api.parkingcontrol.repositories;

import com.api.parkingcontrol.models.ParkingSpotModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface ParkingSpotRepository extends JpaRepository<ParkingSpotModel, Long>, JpaSpecificationExecutor<ParkingSpotModel> {

    boolean existsByParkingSpotNumber(String string);

    Optional<ParkingSpotModel> findByParkingSpotNumber(String number);

    Page<ParkingSpotModel> findAll(Pageable pageable);


}


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

    //boolean existsByLicensePlateCar(String string);

    boolean existsByApartmentAndBlock(String string, String string2);

    boolean existsByParkingSpotNumber(String string);

    //ParkingSpotModel findByLicensePlateCar(String number);

    ParkingSpotModel findByParkingSpotNumber(String number);

    //List<ParkingSpotModel> findAll(String brandCar, String modelCar, String colorCar);

    Page<ParkingSpotModel> findAll(Pageable pageable);

    Optional<ParkingSpotModel> findById(long id);

    //ResponseEntity<Object> patch(@RequestBody Map<Object, Object> fields, @PathVariable(value = "id") long id);

}


package com.api.parkingcontrol.services;

import com.api.parkingcontrol.models.ParkingSpotModel;
import com.api.parkingcontrol.repositories.ParkingSpotRepository;


import com.api.parkingcontrol.specification.ParkingSpotSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Optional;

@Service
public class ParkingSpotService {

    ParkingSpotRepository parkingSpotRepository;

    @Autowired
    public ParkingSpotService(ParkingSpotRepository parkingSpotRepository) {
        this.parkingSpotRepository = parkingSpotRepository;
    }

    @Transactional //garante rollback
    public Object save(ParkingSpotModel parkingSpotModel) {

        return parkingSpotRepository.save(parkingSpotModel);
    }

    public boolean existsByApartmentAndBlock(String string, String string2) {
        return parkingSpotRepository.existsByApartmentAndBlock(string, string2);
    }

    public boolean existsByParkingSpotNumber(String string) {
        return parkingSpotRepository.existsByParkingSpotNumber(string);
    }

    public ParkingSpotModel findByLicensePlateCar(String number) {
        return parkingSpotRepository.findByLicensePlateCar(number);
    }

    public ParkingSpotModel findByParkingSpotNumber(String number) {
        return parkingSpotRepository.findByParkingSpotNumber(number);
    }

    public Optional<ParkingSpotModel> findById(long id) {

        return parkingSpotRepository.findById(id);
    }
    public Page<ParkingSpotModel> findAll(Pageable pageable) {

        return parkingSpotRepository.findAll(pageable);
    }

    public List<ParkingSpotModel> findAll(String brandCar, String modelCar, String colorCar) {
        List<ParkingSpotModel> parkingSpotModelList;
        return parkingSpotModelList = parkingSpotRepository.findAll(Specification.where(
                ParkingSpotSpecification.brandCarEquals(brandCar)
                        .or(ParkingSpotSpecification.modelCarEquals(modelCar))
                        .or(ParkingSpotSpecification.colorCarEquals(colorCar))));
    }

    public boolean existsByLicensePlateCar(String string) {
        return parkingSpotRepository.existsByLicensePlateCar(string);
    }
}

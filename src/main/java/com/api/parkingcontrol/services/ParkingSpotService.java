package com.api.parkingcontrol.services;

import com.api.parkingcontrol.models.ParkingSpotModel;
import com.api.parkingcontrol.repositories.ParkingSpotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public ParkingSpotModel findByParkingSpotNumber(String number) {
        return parkingSpotRepository.findByParkingSpotNumber(number);
    }

    public Optional<ParkingSpotModel> findById(long id) {

        return parkingSpotRepository.findById(id);
    }

    public Page<ParkingSpotModel> findAll(Pageable pageable) {

        return parkingSpotRepository.findAll(pageable);
    }

}

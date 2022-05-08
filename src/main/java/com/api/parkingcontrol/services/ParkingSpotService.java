package com.api.parkingcontrol.services;

import com.api.parkingcontrol.exception.AlreadyExistException;
import com.api.parkingcontrol.exception.ResourceNotFoundException;
import com.api.parkingcontrol.form.ParkingSpotForm;
import com.api.parkingcontrol.models.ParkingSpotModel;
import com.api.parkingcontrol.repositories.ParkingSpotRepository;
import com.api.parkingcontrol.utils.Vacant;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ParkingSpotService {

    ParkingSpotRepository parkingSpotRepository;

    @Autowired
    public ParkingSpotService(ParkingSpotRepository parkingSpotRepository) {
        this.parkingSpotRepository = parkingSpotRepository;
    }

    @Transactional //garante rollback
    public ParkingSpotModel saveNew(ParkingSpotForm parkingSpotSource) {

        if (existsByParkingSpotNumber(parkingSpotSource.getParkingSpotNumber()))
            throw new AlreadyExistException("Essa vaga ja existe");


        ParkingSpotModel parkingSpotTarget = new ParkingSpotModel();
        BeanUtils.copyProperties(parkingSpotSource, parkingSpotTarget);
        parkingSpotTarget.setIsVacant(Vacant.TRUE);

        return parkingSpotRepository.save(parkingSpotTarget);
    }

    public ParkingSpotModel saveExistent(ParkingSpotModel parkingSpotModel) {
        return parkingSpotRepository.save(parkingSpotModel);
    }

    public boolean existsByParkingSpotNumber(String string) {
        return parkingSpotRepository.existsByParkingSpotNumber(string);
    }

    public ParkingSpotModel findByParkingSpotNumber(String string) {
        return parkingSpotRepository.findByParkingSpotNumber(string).orElseThrow(
                () -> new ResourceNotFoundException("Vaga nao encontrada"));
    }

    public ParkingSpotModel findById(long id) {
        return parkingSpotRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Vaga nao encontrada"));
    }

    public Page<ParkingSpotModel> findAll(Pageable pageable) {
        Page<ParkingSpotModel> pageParkingSpot = parkingSpotRepository.findAll(pageable);

        pageParkingSpot.stream().findFirst().orElseThrow(() -> new ResourceNotFoundException("Nao há vagas registradas"));
        return pageParkingSpot;
    }

}

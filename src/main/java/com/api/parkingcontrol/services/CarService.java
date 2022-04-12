package com.api.parkingcontrol.services;

import com.api.parkingcontrol.models.CarModel;
import com.api.parkingcontrol.models.ParkingSpotModel;
import com.api.parkingcontrol.repositories.CarRepository;
import com.api.parkingcontrol.repositories.ParkingSpotRepository;
import com.api.parkingcontrol.specification.CarModelSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CarService {

    CarRepository carRepository;

    @Autowired
    public CarService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    @Transactional //garante rollback
    public Object save(CarModel carModel) {

        return carRepository.save(carModel);
    }

    public CarModel findByLicensePlate(String number) {
        return carRepository.findByLicensePlate(number);
    }

    public Optional<CarModel> findById(long id) {

        return carRepository.findById(id);
    }

    public Page<CarModel> findAll(Pageable pageable) {

        return carRepository.findAll(pageable);
    }

    public List<CarModel> findAll(String brandCar, String modelCar, String colorCar) {
        List<CarModel> carList;
        return carList = carRepository.findAll((Sort) Specification.where(
                CarModelSpecification.brandCarEquals(brandCar)
                        .or(CarModelSpecification.modelCarEquals(modelCar))
                        .or(CarModelSpecification.colorCarEquals(colorCar))));
    }

    public boolean existsByLicensePlate(String string) {
        return carRepository.existsByLicensePlate(string);
    }
}

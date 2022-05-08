package com.api.parkingcontrol.services;

import com.api.parkingcontrol.exception.AlreadyExistException;
import com.api.parkingcontrol.exception.ResourceNotFoundException;
import com.api.parkingcontrol.models.CarModel;
import com.api.parkingcontrol.repositories.CarRepository;
import com.api.parkingcontrol.specification.CarModelSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

@Service
public class CarService {

    CarRepository carRepository;
    ParkingSpotService parkingSpotService;

    @Autowired
    public CarService(CarRepository carRepository, ParkingSpotService parkingSpotService) {
        this.carRepository = carRepository;
        this.parkingSpotService = parkingSpotService;
    }

    @Transactional //garante rollback
    public Object saveNew(CarModel carModel) {
        return carRepository.save(carModel);
    }

    public CarModel findByLicensePlate(String number) {
        return carRepository.findByLicensePlate(number).orElseThrow(() -> new ResourceNotFoundException("Carro nao encontrado"));
    }

    public CarModel findById(long id) {
        return carRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Carro nao encontrado"));
    }

    public Page<CarModel> findAll(Pageable pageable) {
        Page<CarModel> pageCar = carRepository.findAll(pageable);

        pageCar.stream().findFirst().orElseThrow(() -> new ResourceNotFoundException("Nenhum carro registrado"));
        return pageCar;
    }

    public List<CarModel> findAll(String brandCar, String modelCar, String colorCar) {
        List<CarModel> carList;

        return carList = carRepository.findAll(Specification.where(
                CarModelSpecification.brandCarEquals(brandCar)
                        .or(CarModelSpecification.modelCarEquals(modelCar))
                        .or(CarModelSpecification.colorCarEquals(colorCar))));
    }

    public boolean existsByLicensePlate(String string) {
        if (carRepository.existsByLicensePlate(string)) throw new AlreadyExistException();
        return false;
    }

    public CarModel patch(Map<String, String> fields, String id) {
        CarModel carTarget = findById(Long.parseLong(id));

        if (parkingSpotService.existsByParkingSpotNumber(fields.get("parkingSpotNumber")))
            throw new AlreadyExistException("Essa vaga ja existe");

        fields.forEach((key, value) -> {
            Field field = ReflectionUtils.findField(CarModel.class, key);
            assert field != null;
            field.setAccessible(true);
            ReflectionUtils.setField(field, carTarget, value);
        });
        return carRepository.save(carTarget);
    }
}

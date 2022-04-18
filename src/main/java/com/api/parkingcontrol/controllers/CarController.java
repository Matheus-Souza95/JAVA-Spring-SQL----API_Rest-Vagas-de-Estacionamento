package com.api.parkingcontrol.controllers;

import com.api.parkingcontrol.DTO.CarDto;
import com.api.parkingcontrol.DTO.CarForm;
import com.api.parkingcontrol.models.CarModel;
import com.api.parkingcontrol.models.ParkingSpotModel;
import com.api.parkingcontrol.models.UserModel;
import com.api.parkingcontrol.services.CarService;
import com.api.parkingcontrol.services.ParkingSpotService;
import com.api.parkingcontrol.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@EntityScan("com.api.parkingcontrol")
@RestController
@CrossOrigin(originPatterns = "*", maxAge = 3600)
@RequestMapping("/parking-control")
public class CarController {

    final CarService carService;
    final UserService userService;
    final ParkingSpotService parkingSpotService;
    final ModelMapper modelMapper;

    @Autowired
    public CarController(CarService carService, ParkingSpotService parkingSpotService, UserService userService, ModelMapper modelMapper) {

        this.carService = carService;
        this.userService = userService;
        this.parkingSpotService = parkingSpotService;
        this.modelMapper = modelMapper;
    }

    @PostMapping(("car/registration/{id}"))
    public ResponseEntity<Object> saveCar(@RequestBody @Valid CarForm carSource, @PathVariable(value = "id") Long id, @RequestParam String parkingSpotNumber) {

        Optional<ParkingSpotModel> parkingSpotOptional = parkingSpotService.findByParkingSpotNumber(parkingSpotNumber);
        if (parkingSpotService.existsByParkingSpotNumber(parkingSpotNumber)) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Vaga ja esta em uso");
        }

        Optional<UserModel> userOptional = userService.findById(id);
        if (carService.existsByLicensePlate(carSource.getLicensePlate())) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Placa ja cadastrada");
        }

        if (parkingSpotOptional.isPresent() && userOptional.isPresent()) {
            ParkingSpotModel parkingSpotTarget = new ParkingSpotModel();
            CarModel carTarget = new CarModel();
            UserModel userTarget = new UserModel();

            BeanUtils.copyProperties(userOptional.get(), userTarget);
            BeanUtils.copyProperties(parkingSpotOptional.get(), parkingSpotTarget);
            BeanUtils.copyProperties(carSource, carTarget);

            carTarget.setParkingSpot(parkingSpotOptional.get());
            carTarget.setUser(userOptional.get());

            carService.save(carTarget);

            userOptional.get().setCar(carTarget);
            parkingSpotOptional.get().setCar(carTarget);

            userService.save(userOptional.get());
            parkingSpotService.save(parkingSpotOptional.get());

            return ResponseEntity.status(HttpStatus.CREATED).body(carTarget);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("car/all")
    public ResponseEntity<Page<CarModel>> getAllCar(@PageableDefault(page = 0, size = 2, direction = Sort.Direction.ASC) Pageable pageable) {
        Page<CarModel> carList = carService.findAll(pageable);
        if (carList.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            for (CarModel car : carList) {
                long id = car.getId();
                car.add(linkTo(methodOn(CarController.class).getById(id)).withSelfRel());
            }
        }
        return ResponseEntity.ok().body(carList);
    }

    @GetMapping("car/{id}")
    public ResponseEntity<Object> getById(@PathVariable(value = "id") long id) {
        Optional<CarModel> carOptional = carService.findById(id);
        if (carOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Vaga nao encontrada");
        }

        carOptional.get().add(linkTo(methodOn(ParkingSpotController.class).getById(id)).withSelfRel());
        return ResponseEntity.ok().body(carOptional);
    }

    @PatchMapping("car/patch/{id}")
    @Transactional
    public ResponseEntity<Object> patchCar(@RequestBody Map<String, String> sourceFields, @PathVariable(value = "id") long id) {
        Optional<CarModel> carOptional = carService.findById(id);
        if (parkingSpotService.existsByParkingSpotNumber(sourceFields.get("parkingSpotNumber"))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Vaga ja esta em uso");
        }
        if (carOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Veiculo nao encontrado");
        }

        sourceFields.forEach((key, value) -> {
            Field field = ReflectionUtils.findField(CarModel.class, key);
            assert field != null;
            field.setAccessible(true);
            ReflectionUtils.setField(field, carOptional.get(), value);
        });

        carService.save(carOptional.get());

        carOptional.get().add(linkTo(methodOn(CarController.class).getById(id)).withSelfRel());
        return ResponseEntity.status(HttpStatus.OK).body(carOptional.get());

    }

/*     @PatchMapping("car/patch/{id}")
    @Transactional
    public ResponseEntity<Object> patchCar(@RequestBody Map<String, String> sourceFields, @PathVariable(value = "id") long id) {
        Optional<CarModel> carOptional = carService.findById(id);
        if (!parkingSpotService.existsByParkingSpotNumber(sourceFields.get("parkingSpotNumber"))) {
            if (carOptional.isPresent()) {
                sourceFields.forEach((key, value) -> {
                    Field field = ReflectionUtils.findField(CarModel.class, key);
                    assert field != null;
                    field.setAccessible(true);
                    ReflectionUtils.setField(field, carOptional.get(), value);
                });
                carService.save(carOptional.get());
                carOptional.get().add(linkTo(methodOn(CarController.class).getById(id)).withSelfRel());
                return ResponseEntity.status(HttpStatus.OK).body(carOptional.get());
            } else {
                return ResponseEntity.status(HttpStatus.OK).body("Veiculo nao encontrado");
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Vaga ja esta em uso");
        }
    }
    }*/

    @GetMapping("car/filter")
    @Transactional
    public ResponseEntity<Object> getCarByFilter(@RequestParam("brandCar") String
                                                         brandCar, @RequestParam("modelCar") String modelCar, @RequestParam("colorCar") String colorCar, Pageable
                                                         pageable) {

        Optional<List<CarModel>> carOptional = Optional.ofNullable(carService.findAll(brandCar, modelCar, colorCar));

        if (carOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhum carro encontrado");
        }
        List<CarDto> listDto = new ArrayList<>();
        carOptional.get().forEach(p -> listDto.add(convertToDto(p, CarDto.class)));
        listDto.forEach(p -> p.add(linkTo(methodOn(ParkingSpotController.class).getAllParkingSpot(pageable)).withRel("Lista de todas vagas")));
        return ResponseEntity.status(HttpStatus.OK).body(listDto);
    }

    public CarDto convertToDto(CarModel carModel, Class<CarDto> carDtoClass) {
        return modelMapper.map(carModel, CarDto.class);
    }
/*
    public ParkingSpotDto convertEntityToDto(ParkingSpotModel parkingSpotModel, Class<ParkingSpotDto> parkingSpotDtoClass) {
        return modelMapper.map(parkingSpotModel, ParkingSpotDto.class);
    }*/
}

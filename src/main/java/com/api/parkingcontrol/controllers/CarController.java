package com.api.parkingcontrol.controllers;

import com.api.parkingcontrol.dto.CarDto;
import com.api.parkingcontrol.form.CarForm;
import com.api.parkingcontrol.models.CarModel;
import com.api.parkingcontrol.models.ParkingSpotModel;
import com.api.parkingcontrol.models.UserModel;
import com.api.parkingcontrol.services.CarService;
import com.api.parkingcontrol.services.ParkingSpotService;
import com.api.parkingcontrol.services.UserService;
import com.api.parkingcontrol.utils.Vacant;
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
import org.springframework.security.access.prepost.PreAuthorize;
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

    @PreAuthorize("hasRole('ROLE_DEFAULT')")
    @PostMapping(("car/registration/{id}"))
    public ResponseEntity<Object> saveCar(@RequestBody @Valid CarForm carSource, @PathVariable(value = "id") Long id, @RequestParam String parkingSpotNumber) {

        ParkingSpotModel parkingSpotTarget = parkingSpotService.findByParkingSpotNumber(parkingSpotNumber);

        if (parkingSpotTarget.getIsVacant().equals(Vacant.FALSE)) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("Vaga ocupada");
        }
        if (carService.existsByLicensePlate(carSource.getLicensePlate())) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("Placa ja cadastrada");
        }
        UserModel userTarget = userService.findById(id);

        parkingSpotTarget.setIsVacant(Vacant.FALSE);

        CarModel carTarget = new CarModel();

        BeanUtils.copyProperties(carSource, carTarget);

        carTarget.setParkingSpot(parkingSpotTarget);
        carTarget.setUser(userTarget);

        carService.saveNew(carTarget);

        userTarget.setCar(carTarget);
        parkingSpotTarget.setCar(carTarget);

        userService.saveExistent(userTarget);
        parkingSpotService.saveExistent(parkingSpotTarget);

        return ResponseEntity.status(HttpStatus.CREATED).body(carTarget);

    }

    @PreAuthorize("permitAll= true")
    @GetMapping("car/all")
    public ResponseEntity<Object> getAllCar(@PageableDefault(page = 0, size = 2, direction = Sort.Direction.ASC) Pageable pageable) {
        Page<CarModel> carList = carService.findAll(pageable);
        if (carList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nao há carros registrados");
        }
        for (CarModel car : carList) {
            long id = car.getId();
            car.add(linkTo(methodOn(CarController.class).getById(id)).withSelfRel());
        }
        return ResponseEntity.ok().body(carList);
    }

    @PreAuthorize("permitAll= true")
    @GetMapping("car/{id}")
    public ResponseEntity<Object> getById(@PathVariable(value = "id") long id) {
        CarModel carTarget = carService.findById(id);

        carTarget.add(linkTo(methodOn(ParkingSpotController.class).getById(id)).withSelfRel());
        return ResponseEntity.ok().body(carTarget);
    }

    @PreAuthorize("hasRole('ROLE_DEFAULT')")
    @PatchMapping("car/patch/{id}")
    @Transactional
    public ResponseEntity<Object> patchCar(@RequestBody Map<String, String> sourceFields, @PathVariable(value = "id") long id) {
        CarModel carTarget = carService.patch(sourceFields, String.valueOf(id));

        carTarget.add(linkTo(methodOn(CarController.class).getById(id)).withSelfRel());
        return ResponseEntity.status(HttpStatus.OK).body(carTarget);
    }

    @PreAuthorize("permitAll= true")
    @GetMapping("car/filter")
    @Transactional
    public ResponseEntity<Object> getCarByFilter(@RequestParam("brandCar") String brandCar,
                                                 @RequestParam("modelCar") String modelCar,
                                                 @RequestParam("colorCar") String colorCar,
                                                 Pageable pageable) {

        Optional<List<CarModel>> carOptional = Optional.ofNullable(carService.findAll(brandCar, modelCar, colorCar));

        if (carOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhum carro encontrado");
        }
        List<CarDto> listDto = new ArrayList<>();
        carOptional.get().forEach(p -> listDto.add(convertToDto(p, CarDto.class)));
        listDto.forEach(p -> p.add(linkTo(methodOn(ParkingSpotController.class).getAll(pageable)).withRel("Lista de todas vagas")));
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

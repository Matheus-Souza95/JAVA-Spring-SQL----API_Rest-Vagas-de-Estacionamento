package com.api.parkingcontrol.controllers;

import com.api.parkingcontrol.DTO.ParkingSpotDto;
import com.api.parkingcontrol.DTO.ParkingSpotForm;
import com.api.parkingcontrol.models.ParkingSpotModel;

import com.api.parkingcontrol.services.ParkingSpotService;
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

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@EntityScan("com.api.parkingcontrol")
@RestController
@CrossOrigin(originPatterns = "*", maxAge = 3600)
@RequestMapping("/parking-spot")
public class ParkingSpotController {

    final ParkingSpotService parkingSpotService;
    private final ModelMapper modelMapper;

    @Autowired
    public ParkingSpotController(ParkingSpotService parkingSpotService, ModelMapper modelMapper) {
        this.parkingSpotService = parkingSpotService;
        this.modelMapper = modelMapper;
    }
    @PostMapping(("/registration"))
    public ResponseEntity<Object> saveParkingSpot(@RequestBody @Valid ParkingSpotForm parkingSpotForm) {

        if (parkingSpotService.existsByLicensePlateCar(parkingSpotForm.getLicensePlateCar())) {

            return ResponseEntity.status(HttpStatus.CONFLICT).body("O Veículo com esta placa ja esta cadastrado " + parkingSpotForm.getLicensePlateCar());
        }
        if (parkingSpotService.existsByApartmentAndBlock(parkingSpotForm.getApartment(), parkingSpotForm.getBlock())) {

            return ResponseEntity.status(HttpStatus.CONFLICT).body("A vaga deste apartamento e bloco ja esta em uso ");
        }
        if (parkingSpotService.existsByParkingSpotNumber(parkingSpotForm.getParkingSpotNumber())) {

            return ResponseEntity.status(HttpStatus.CONFLICT).body("Esta vaga ja esta ocupada");
        }
        var parkingSpotModel = new ParkingSpotModel();
        var parkingSpotDto = new ParkingSpotDto();

        BeanUtils.copyProperties(parkingSpotForm, parkingSpotModel);
        parkingSpotModel.setRegistrationDate(LocalDateTime.now(ZoneId.of("UTC")));
        parkingSpotService.save(parkingSpotModel);

        BeanUtils.copyProperties(parkingSpotModel, parkingSpotDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(parkingSpotDto);
    }

    @GetMapping("/all")
    public ResponseEntity<Page<ParkingSpotModel>> getAllParkingSpot(@PageableDefault(page = 0, size = 2, sort = "registrationDate", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<ParkingSpotModel> parkingSpotModelList = parkingSpotService.findAll(pageable);
        if (parkingSpotModelList.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            for (ParkingSpotModel p : parkingSpotModelList) {
                long id = p.getId();
                p.add(linkTo(methodOn(ParkingSpotController.class).getOneById(id)).withSelfRel());
            }
        }
        return ResponseEntity.ok().body(parkingSpotModelList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getOneById(@PathVariable(value = "id") long id) {
        Optional<ParkingSpotModel> parkingSpotOptional = parkingSpotService.findById(id);
        if (parkingSpotOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Parking Spot not found.");
        }
        var parkingSpotDTO = new ParkingSpotDto();

        parkingSpotDTO = convertToDto(parkingSpotOptional.get(), ParkingSpotDto.class);

        parkingSpotDTO.add(linkTo(methodOn(ParkingSpotController.class).getOneById(id)).withSelfRel());
        return ResponseEntity.ok().body(parkingSpotDTO);
    }

    @PutMapping("/updateId/{id}")
    @Transactional
    public ResponseEntity<Object> updateParkingSpot(@RequestBody ParkingSpotForm parkingSpotBody, @PathVariable(value = "id") long id) {
        Optional<ParkingSpotModel> parkingSpotModelOptional = parkingSpotService.findById(id);
        if (parkingSpotModelOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Parking Spot not found.");
        }
        var parkingSpotModel = new ParkingSpotModel();

        BeanUtils.copyProperties(parkingSpotBody, parkingSpotModel);
        parkingSpotModel.setId(parkingSpotModelOptional.get().getId());
        parkingSpotModel.setRegistrationDate(parkingSpotModelOptional.get().getRegistrationDate());

        parkingSpotService.save(parkingSpotModel);

        var parkingSpotDto = new ParkingSpotDto();
        parkingSpotDto = convertToDto(parkingSpotModel, ParkingSpotDto.class);
        parkingSpotDto.add(linkTo(methodOn(ParkingSpotController.class).getOneById(id)).withSelfRel());

        return ResponseEntity.status(HttpStatus.OK).body(parkingSpotDto);
    }
 /*   @PutMapping("/updateId/{id}")
    @Transactional
    public ResponseEntity<Object> updateParkingSpot(@RequestBody ParkingSpotModel parkingSpotBody, @PathVariable(value = "id") long id) {

        Optional<ParkingSpotModel> parkingSpotModelOptional = parkingSpotService.findById(id);
        if (!parkingSpotModelOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Parking Spot not found.");
        }
        var parkingSpotModel = new ParkingSpotModel();
        BeanUtils.copyProperties(parkingSpotBody, parkingSpotModel);
        parkingSpotModel.setId(parkingSpotModelOptional.get().getId());
        parkingSpotModel.setRegistrationDate(parkingSpotModelOptional.get().getRegistrationDate());
        parkingSpotModel.add(linkTo(methodOn(ParkingSpotController.class).getOneById(id)).withSelfRel());
        return ResponseEntity.status(HttpStatus.OK).body(parkingSpotService.save(parkingSpotModel));
    }*/

    @PatchMapping("/patchId/{id}")
    @Transactional
    public ResponseEntity<ParkingSpotModel> patchParkingSpot(@RequestBody Map<Object, Object> fields, @PathVariable(value = "id") long id) {
        Optional<ParkingSpotModel> optional = parkingSpotService.findById(id);
        if (optional.isPresent()) {
            fields.forEach((key, value) -> {
                Field field = ReflectionUtils.findField(ParkingSpotModel.class, (String) key);
                assert field != null;
                field.setAccessible(true);
                ReflectionUtils.setField(field, optional.get(), value);
            });
            ParkingSpotModel updateParkingSpot = (ParkingSpotModel) parkingSpotService.save(optional.get());
            updateParkingSpot.add(linkTo(methodOn(ParkingSpotController.class).getOneById(id)).withSelfRel());
            return ResponseEntity.status(HttpStatus.OK).body(updateParkingSpot);

        }
        return null;
    }

    @GetMapping("/")
    @Transactional
    public ResponseEntity<Object> getFilterParkingSpot(@RequestParam("brandCar") String brandCar, @RequestParam("modelCar") String modelCar, @RequestParam("colorCar") String colorCar, Pageable pageable) {

        Optional<List<ParkingSpotModel>> parkingSpotModelOptional = Optional.ofNullable(parkingSpotService.findAll(brandCar, modelCar, colorCar));

        if (parkingSpotModelOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Parking Spot not found.");
        }
        List<ParkingSpotDto> listDTO = new ArrayList<>();
        parkingSpotModelOptional.get().forEach(p -> listDTO.add(convertToDto(p, ParkingSpotDto.class)));
        listDTO.forEach(p -> p.add(linkTo(methodOn(ParkingSpotController.class).getAllParkingSpot(pageable)).withRel("Lista de todas vagas")));
        return ResponseEntity.status(HttpStatus.OK).body(listDTO);
    }


    public ParkingSpotDto convertToDto(ParkingSpotModel parkingSpotModel, Class<ParkingSpotDto> parkingSpotDtoClass) {
        return modelMapper.map(parkingSpotModel, ParkingSpotDto.class);
    }

    public ParkingSpotDto convertEntityToDto(ParkingSpotModel parkingSpotModel, Class<ParkingSpotDto> parkingSpotDtoClass) {
        return modelMapper.map(parkingSpotModel, ParkingSpotDto.class);
    }
}

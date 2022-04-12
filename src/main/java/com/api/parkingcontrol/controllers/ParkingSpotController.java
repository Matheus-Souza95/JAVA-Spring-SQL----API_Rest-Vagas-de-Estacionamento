package com.api.parkingcontrol.controllers;

import com.api.parkingcontrol.DTO.ParkingSpotForm;
import com.api.parkingcontrol.models.ParkingSpotModel;
import com.api.parkingcontrol.services.CarService;
import com.api.parkingcontrol.services.ParkingSpotService;
import com.api.parkingcontrol.services.UserService;
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
    final UserService userService;
    final CarService carService;
    //private final ModelMapper modelMapper;

    @Autowired
    public ParkingSpotController(ParkingSpotService parkingSpotService, CarService carService, UserService userService) {
        this.parkingSpotService = parkingSpotService;
        this.userService = userService;
        this.carService = carService;
        // this.modelMapper = modelMapper;
    }

    @PostMapping(("parkingSpot/registration"))
    public ResponseEntity<Object> saveParkingSpot(@RequestBody @Valid ParkingSpotModel psSource) {

        if (parkingSpotService.existsByParkingSpotNumber(psSource.getParkingSpotNumber())) {

            return ResponseEntity.status(HttpStatus.CONFLICT).body("Esta vaga ja esta em uso" + psSource.getParkingSpotNumber());
        }
        if (parkingSpotService.existsByApartmentAndBlock(psSource.getApartment(), psSource.getBlock())) {

            return ResponseEntity.status(HttpStatus.CONFLICT).body("A vaga deste apartamento e bloco ja esta em uso ");
        }

        ParkingSpotModel psTarget = new ParkingSpotModel();
        BeanUtils.copyProperties(psSource, psTarget);
        psTarget.setRegistrationDate(LocalDateTime.now(ZoneId.of("UTC")));
        parkingSpotService.save(psTarget);

        return ResponseEntity.status(HttpStatus.CREATED).body(psTarget);
    }

    @GetMapping("parkingSpot/all")
    public ResponseEntity<Page<ParkingSpotModel>> getAllParkingSpot(@PageableDefault(page = 0, size = 2, sort = "registrationDate", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<ParkingSpotModel> psList = parkingSpotService.findAll(pageable);
        if (psList.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            for (ParkingSpotModel ps : psList) {
                long id = ps.getId();
                ps.add(linkTo(methodOn(ParkingSpotController.class).getById(id)).withSelfRel());
            }
        }
        return ResponseEntity.ok().body(psList);
    }

    @GetMapping("parkingSpot/{id}")
    public ResponseEntity<Object> getById(@PathVariable(value = "id") long id) {
        Optional<ParkingSpotModel> psOptional = parkingSpotService.findById(id);
        if (psOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Vaga nao encontrada");
        }

        psOptional.get().add(linkTo(methodOn(ParkingSpotController.class).getById(id)).withSelfRel());
        return ResponseEntity.ok().body(psOptional);
    }

    @PutMapping("parkingSpot/update/{id}")
    @Transactional
    public ResponseEntity<Object> updateParkingSpot(@RequestBody ParkingSpotForm parkingSpotBody, @PathVariable(value = "id") long id) {
        Optional<ParkingSpotModel> psOptional = parkingSpotService.findById(id);
        if (psOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Vaga nao encontrada");
        }
        var psTarget = new ParkingSpotModel();

        BeanUtils.copyProperties(parkingSpotBody, psTarget);
        psTarget.setId(psOptional.get().getId());
        psTarget.setRegistrationDate(psOptional.get().getRegistrationDate());

        parkingSpotService.save(psTarget);

        psTarget.add(linkTo(methodOn(ParkingSpotController.class).getById(id)).withSelfRel());

        return ResponseEntity.status(HttpStatus.OK).body(psTarget);
    }

    @PatchMapping("parkingSpot/patch/{id}")
    @Transactional
    public ResponseEntity<ParkingSpotModel> patchParkingSpot(@RequestBody Map<Object, Object> fields, @PathVariable(value = "id") long id) {
        Optional<ParkingSpotModel> psOptional = parkingSpotService.findById(id);
        if (psOptional.isPresent()) {
            fields.forEach((key, value) -> {
                Field field = ReflectionUtils.findField(ParkingSpotModel.class, (String) key);
                assert field != null;
                field.setAccessible(true);
                ReflectionUtils.setField(field, psOptional.get(), value);
            });
            parkingSpotService.save(psOptional.get());
            psOptional.get().add(linkTo(methodOn(ParkingSpotController.class).getById(id)).withSelfRel());
            return ResponseEntity.status(HttpStatus.OK).body(psOptional.get());
        }
        ResponseEntity.status(HttpStatus.NOT_FOUND).body("Vaga nao encontrada");
        return null;
    }

    /*public ParkingSpotDto convertToDto(ParkingSpotModel parkingSpotModel, Class<ParkingSpotDto> parkingSpotDtoClass) {
        return modelMapper.map(parkingSpotModel, ParkingSpotDto.class);
    }

    public ParkingSpotDto convertEntityToDto(ParkingSpotModel parkingSpotModel, Class<ParkingSpotDto> parkingSpotDtoClass) {
        return modelMapper.map(parkingSpotModel, ParkingSpotDto.class);
    }*/
}

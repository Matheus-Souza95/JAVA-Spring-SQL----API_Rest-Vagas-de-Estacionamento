package com.api.parkingcontrol.controllers;

import com.api.parkingcontrol.form.ParkingSpotForm;
import com.api.parkingcontrol.models.ParkingSpotModel;
import com.api.parkingcontrol.services.CarService;
import com.api.parkingcontrol.services.ParkingSpotService;
import com.api.parkingcontrol.services.UserService;
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
import java.util.Map;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@EntityScan("com.api.parkingcontrol")
@RestController
@CrossOrigin(originPatterns = "*", maxAge = 3600)
@RequestMapping("/parking-control")
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

    @PreAuthorize("hasRole('ROLE_DEFAULT')")
    @PostMapping(("parkingSpot/registration"))
    public ResponseEntity<Object> save(@RequestBody @Valid ParkingSpotForm parkingSpotSource) {
        ParkingSpotModel parkingSpotModel = parkingSpotService.saveNew(parkingSpotSource);
        return ResponseEntity.status(HttpStatus.OK).body(parkingSpotModel);
    }

    @PreAuthorize("permitAll= true")
    @GetMapping("parkingSpot/all")
    public ResponseEntity<Page<ParkingSpotModel>> getAll(@PageableDefault(page = 0, size = 2, direction = Sort.Direction.ASC) Pageable pageable) {
        Page<ParkingSpotModel> psList = parkingSpotService.findAll(pageable);

        for (ParkingSpotModel ps : psList) {
            long id = ps.getId();
            ps.add(linkTo(methodOn(ParkingSpotController.class).getById(id)).withSelfRel());
        }

        return ResponseEntity.ok().body(psList);
    }

    @GetMapping("parkingSpot/{id}")
    public ResponseEntity<Object> getById(@PathVariable(value = "id") long id) {
        ParkingSpotModel parkingSpot = parkingSpotService.findById(id);

        parkingSpot.add(linkTo(methodOn(ParkingSpotController.class).getById(id)).withSelfRel());

        return ResponseEntity.ok().body(parkingSpot);
    }

    @PreAuthorize("hasRole('ROLE_DEFAULT')")
    @PutMapping("parkingSpot/update/{id}")
    @Transactional
    public ResponseEntity<ParkingSpotModel> update(@RequestBody ParkingSpotForm parkingSpotSource, @PathVariable(value = "id") long id) {
        ParkingSpotModel parkingSpotModel = parkingSpotService.saveNew(parkingSpotSource);

        parkingSpotModel.add(linkTo(methodOn(ParkingSpotController.class).getById(id)).withSelfRel());

        return ResponseEntity.status(HttpStatus.OK).body(parkingSpotModel);
    }
}

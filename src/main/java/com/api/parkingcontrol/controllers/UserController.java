package com.api.parkingcontrol.controllers;

import com.api.parkingcontrol.models.UserModel;
import com.api.parkingcontrol.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@EntityScan("com.api.parkingcontrol")
@RestController
@CrossOrigin(originPatterns = "*", maxAge = 3600)
@RequestMapping("/parking-spot")
public class UserController {

    final UserService userService;

    @Autowired
    public UserController(UserService userService) {

        this.userService = userService;
    }

    @GetMapping("user/all")
    public ResponseEntity<Page<UserModel>> getAllUser(@PageableDefault(page = 0, size = 2, direction = Sort.Direction.ASC) Pageable pageable) {
        Page<UserModel> userList = userService.findAll(pageable);
        if (userList.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            for (UserModel u : userList) {
                long id = u.getId();
                u.add(linkTo(methodOn(ParkingSpotController.class).getById(id)).withSelfRel());
            }
        }
        return ResponseEntity.ok().body(userList);
    }

    @GetMapping("user/{id}")
    public ResponseEntity<Object> getById(@PathVariable(value = "id") long id) {
        Optional<UserModel> uOptional = userService.findById(id);
        if (uOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Vaga nao encontrada");
        }

        uOptional.get().add(linkTo(methodOn(ParkingSpotController.class).getById(id)).withSelfRel());
        return ResponseEntity.ok().body(uOptional);
    }

    /*@GetMapping("car/filter")
    @Transactional
    public ResponseEntity<Object> getCarByFilter(@RequestParam("brandCar") String brandCar, @RequestParam("modelCar") String modelCar, @RequestParam("colorCar") String colorCar, Pageable pageable) {

        Optional<List<CarModel>> parkingSpotModelOptional = Optional.ofNullable(userService.findAll(brandCar, modelCar, colorCar));

        if (parkingSpotModelOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Parking Spot not found.");
        }
        List<ParkingSpotDto> listDTO = new ArrayList<>();
        parkingSpotModelOptional.get().forEach(p -> listDTO.add(convertToDto(p, ParkingSpotDto.class)));
        listDTO.forEach(p -> p.add(linkTo(methodOn(ParkingSpotController.class).getAllParkingSpot(pageable)).withRel("Lista de todas vagas")));
        return ResponseEntity.status(HttpStatus.OK).body(listDTO);
    }*/
}

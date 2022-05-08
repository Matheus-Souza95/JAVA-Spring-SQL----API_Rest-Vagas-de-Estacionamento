package com.api.parkingcontrol.controllers;

import com.api.parkingcontrol.form.UserForm;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@EntityScan("com.api.parkingcontrol")
@RestController
@CrossOrigin(originPatterns = "*", maxAge = 3600)
@RequestMapping("/parking-control")
public class UserController {

    final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("permitAll= true")
    @PostMapping(("user/registration"))
    public ResponseEntity<Object> saveUser(@RequestBody @Valid UserForm userSource) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.saveNew(userSource));
    }

    @PreAuthorize("permitAll= true")
    @GetMapping("user/all")
    public ResponseEntity<Page<UserModel>> getAllUser(@PageableDefault(page = 0, size = 2, direction = Sort.Direction.ASC) Pageable pageable) {
        Page<UserModel> userList = userService.findAll(pageable);

        for (UserModel user : userList) {
            long id = user.getId();
            user.add(linkTo(methodOn(ParkingSpotController.class).getById(user.getId())).withSelfRel());
        }

        return ResponseEntity.ok().body(userList);
    }

    @PreAuthorize("permitAll= true")
    @GetMapping("user/{id}")
    public ResponseEntity<Object> getById(@PathVariable(value = "id") long id) {
        UserModel userTarget = userService.findById(id);

        userTarget.add(linkTo(methodOn(ParkingSpotController.class).getById(id)).withSelfRel());
        return ResponseEntity.ok().body(userTarget);
    }

    @PreAuthorize("hasRole('ROLE_DEFAULT')")
    @PatchMapping("user/patch/{id}")
    @Transactional
    public ResponseEntity<Object> patchUser(@RequestBody Map<String, String> sourceFields, @PathVariable(value = "id") long id) {
        UserModel userTarget = userService.update(sourceFields, String.valueOf(id));
        userTarget.add(linkTo(methodOn(UserController.class).getById(id)).withSelfRel());
        return ResponseEntity.status(HttpStatus.OK).body(userTarget);
    }
}

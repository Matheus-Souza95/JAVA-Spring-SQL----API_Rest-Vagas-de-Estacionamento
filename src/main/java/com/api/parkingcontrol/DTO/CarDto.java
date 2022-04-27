package com.api.parkingcontrol.DTO;

import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serializable;

@Getter
@Setter
public class CarDto extends RepresentationModel<CarDto> implements Serializable {

    private String licensePlateCar;
    private String brandCar;
    private String modelCar;
    private String colorCar;
}

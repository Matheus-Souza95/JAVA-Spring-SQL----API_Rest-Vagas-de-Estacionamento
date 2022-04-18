package com.api.parkingcontrol.DTO;

import com.api.parkingcontrol.models.CarModel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Getter
@Setter
public class CarForm extends RepresentationModel<CarModel> implements Serializable {

    @NotBlank(message = "Este campo nao pode ser vazio")
    private String licensePlate;
    @NotBlank(message = "Este campo nao pode ser vazio")
    private String brandCar;
    @NotBlank(message = "Este campo nao pode ser vazio")
    private String modelCar;
    @NotBlank(message = "Este campo nao pode ser vazio")
    private String colorCar;
}

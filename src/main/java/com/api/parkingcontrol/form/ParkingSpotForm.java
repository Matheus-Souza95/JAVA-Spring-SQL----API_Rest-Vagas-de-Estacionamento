package com.api.parkingcontrol.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Getter
@Setter
public class ParkingSpotForm implements Serializable {

    @NotBlank(message = "Este campo nao pode ser vazio")
    private String parkingSpotNumber;
}

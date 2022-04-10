package com.api.parkingcontrol.DTO;

import com.api.parkingcontrol.validation.contrains.LicensePlateCarAnnotation;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Getter
@Setter
public class ParkingSpotForm implements Serializable {

    @NotBlank(message = "Este campo nao pode ser vazio")
    private String parkingSpotNumber;
    @NotBlank(message = "Este campo nao pode ser vazio")

    //@Size(min = 7, max = 7, message = "Insira a placa do veiculo no formato ABC1234")
    @LicensePlateCarAnnotation
    private String licensePlateCar;
    @NotBlank(message = "Este campo nao pode ser vazio")
    private String brandCar;
    @NotBlank(message = "Este campo nao pode ser vazio")
    private String modelCar;
    @NotBlank(message = "Este campo nao pode ser vazio")
    private String colorCar;
    @NotBlank(message = "Este campo nao pode ser vazio")
    private String responsibleName;
    @NotBlank(message = "Este campo nao pode ser vazio")
    private String apartment;
    @NotBlank(message = "Este campo nao pode ser vazio")
    private String block;

}

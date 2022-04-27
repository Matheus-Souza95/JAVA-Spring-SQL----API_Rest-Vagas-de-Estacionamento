package com.api.parkingcontrol.DTO;

import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serializable;

@Getter
@Setter
public class ParkingSpotDto extends RepresentationModel<ParkingSpotDto> implements Serializable {

    private String parkingSpotNumber;
    private String licensePlateCar;
    private String brandCar;
    private String modelCar;
    private String colorCar;

    @Override
    public String toString() {
        return "ParkingSpotDto{" +
                "parkingSpotNumber='" + parkingSpotNumber + '\'' +
                ", licensePlateCar='" + licensePlateCar + '\'' +
                ", brandCar='" + brandCar + '\'' +
                ", modelCar='" + modelCar + '\'' +
                ", colorCar='" + colorCar + '\'' +
                '}';
    }
}

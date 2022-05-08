package com.api.parkingcontrol;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        var modelMapper = new ModelMapper();

      /*  modelMapper.createTypeMap(ParkingSpotModel.class, ParkingSpotDto.class).
                addMapping(src -> src.getBlock(), (dest, value) -> dest.setBlock((String) value));*/

       /* modelMapper.createTypeMap(ParkingSpotModel.class, ParkingSpotDto.class).
                addMapping(src -> src.getApartment(), (dest, value) -> dest.setApartment((String) value));

        modelMapper.createTypeMap(ParkingSpotModel.class, ParkingSpotDto.class).
                addMapping(src -> src.getParkingSpotNumber(), (dest, value) -> dest.setParkingSpotNumber((String) value));

        modelMapper.createTypeMap(ParkingSpotModel.class, ParkingSpotDto.class).
                addMapping(src -> src.getLicensePlateCar(), (dest, value) -> dest.setLicensePlateCar((String) value));

        modelMapper.createTypeMap(ParkingSpotModel.class, ParkingSpotDto.class).
                addMapping(src -> src.getBrandCar(), (dest, value) -> dest.setBrandCar((String) value));

        modelMapper.createTypeMap(ParkingSpotModel.class, ParkingSpotDto.class).
                addMapping(src -> src.getModelCar(), (dest, value) -> dest.setModelCar((String) value));
        modelMapper.createTypeMap(ParkingSpotModel.class, ParkingSpotDto.class).
                addMapping(src -> src.getColorCar(), (dest, value) -> dest.setColorCar((String) value));
        modelMapper.createTypeMap(ParkingSpotModel.class, ParkingSpotDto.class).
                addMapping(src -> src.getResponsibleName(), (dest, value) -> dest.setResponsibleName((String) value));*/

        return modelMapper;
    }
}

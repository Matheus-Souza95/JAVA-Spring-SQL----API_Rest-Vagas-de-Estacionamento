package com.api.parkingcontrol.specification;

import com.api.parkingcontrol.models.ParkingSpotModel;
import org.springframework.data.jpa.domain.Specification;


public class ParkingSpotSpecification {

    public static Specification<ParkingSpotModel> licensePlateCar(String licensePlateCar) {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get("licensePlateCar"), licensePlateCar);
    }

    public static Specification<ParkingSpotModel> brandCarEquals(String brandCar) {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get("brandCar"), brandCar);
    }

    public static Specification<ParkingSpotModel> modelCarEquals(String modelCar) {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get("modelCar"), modelCar);
    }

    public static Specification<ParkingSpotModel> colorCarEquals(String colorCar) {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get("colorCar"), colorCar);
    }

    public static Specification<ParkingSpotModel> responsibleNameEquals(String responsibleName) {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get("responsibleName"), responsibleName);
    }



    /*public static Specification<ParkingSpotModel> name(String name) {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.greaterThan(root.get("salario"), salario);
    }*/
}

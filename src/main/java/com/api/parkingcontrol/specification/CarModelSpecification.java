package com.api.parkingcontrol.specification;

import com.api.parkingcontrol.models.CarModel;
import com.api.parkingcontrol.models.ParkingSpotModel;
import org.springframework.data.jpa.domain.Specification;


public class CarModelSpecification {

    public static Specification<CarModel> licensePlateCar(String licensePlateCar) {
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

}

package com.api.parkingcontrol.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "TB_CAR")
public class CarModel extends RepresentationModel<CarModel> implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false, unique = true, length = 7)
    private String licensePlate;
    @Column(nullable = false, length = 70)
    private String brandCar;
    @Column(nullable = false, length = 70)
    private String modelCar;
    @Column(nullable = false, length = 70)
    private String colorCar;
    @OneToOne
    @JsonBackReference
    @JoinColumn(nullable = false, name = "user_id")
    private UserModel user;
    @OneToOne
    @JsonBackReference
    @JoinColumn(nullable = false, name = "parkingSpot_id")
    private ParkingSpotModel parkingSpot;

    @Override
    public String toString() {
        return "CarModel{" +
                "id=" + id +
                ", licensePlate='" + licensePlate + '\'' +
                ", brandCar='" + brandCar + '\'' +
                ", modelCar='" + modelCar + '\'' +
                ", colorCar='" + colorCar + '\'' +
                ", user=" + user +
                ", parkingSpot=" + parkingSpot +
                '}';
    }
}

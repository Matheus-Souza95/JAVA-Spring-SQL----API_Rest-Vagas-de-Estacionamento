package com.api.parkingcontrol.models;

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
    @Column(name = "id", nullable = false)
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
    @JoinColumn(nullable = false, name = "user_id")
    private UserModel user;
}

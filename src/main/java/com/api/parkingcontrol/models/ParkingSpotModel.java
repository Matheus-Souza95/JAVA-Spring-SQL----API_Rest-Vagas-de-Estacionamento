package com.api.parkingcontrol.models;

import com.api.parkingcontrol.utils.Vacant;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cascade;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "TB_PARKING_SPOT")
public class ParkingSpotModel extends RepresentationModel<ParkingSpotModel> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false, unique = true, length = 5)
    private String parkingSpotNumber;
    @Column(nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private Vacant isVacant;
    @OneToOne
    @JsonManagedReference
    @JoinColumn(nullable = true, name = "car_id")
    private CarModel car;

    @Override
    public String toString() {
        return "ParkingSpotModel{" +
                "id=" + id +
                ", parkingSpotNumber='" + parkingSpotNumber + '\'' +
                ", isVacant=" + isVacant +
                ", car=" + car +
                '}';
    }
}

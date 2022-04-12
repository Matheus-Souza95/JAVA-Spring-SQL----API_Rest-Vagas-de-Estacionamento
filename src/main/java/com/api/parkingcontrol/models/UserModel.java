package com.api.parkingcontrol.models;

import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "TB_USER")
public class UserModel extends RepresentationModel<UserModel> implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(nullable = false,length = 50)
    private String name;
    @Column(nullable = false, unique = true, length = 11)
    private String cpf;
    @OneToOne
    @JoinColumn(nullable = false,name = "car_id")
    private CarModel car;


}

package com.api.parkingcontrol.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
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
    private Long id;
    @Column(nullable = false,length = 50)
    private String name;
    @Column(nullable = false, unique = true, length = 11)
    private String cpf;
    @OneToOne
    @JsonManagedReference
    @JoinColumn(nullable = true,name = "car_id")
    private CarModel car;


}

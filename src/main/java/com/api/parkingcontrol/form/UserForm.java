package com.api.parkingcontrol.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Getter
@Setter
public class UserForm implements Serializable {

    @NotBlank(message = "Este campo nao pode ser vazio")
    private String name;
    @NotBlank(message = "Este campo nao pode ser vazio")
    private String cpf;
    @Size(min = 6, max = 50)
    @NotBlank(message = "Este campo nao pode ser vazio")
    private String password;

}

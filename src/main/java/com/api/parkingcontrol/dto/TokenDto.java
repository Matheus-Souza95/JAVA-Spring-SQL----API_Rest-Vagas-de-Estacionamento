package com.api.parkingcontrol.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class TokenDto {

    private final String token;
    private final String type;

    public TokenDto(String token, String type) {

        this.token = token;
        this.type = type;
    }
}

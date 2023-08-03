package com.ssafy.jarviser.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonProperty("USERINFO")
public class RequestUpdateUserDto {
    private long id;

    private String password;

    private String name;

}

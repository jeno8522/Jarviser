package com.ssafy.jarviser.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RequestUpdateUserDto {
    private long id;

    private String password;

    private String name;

}

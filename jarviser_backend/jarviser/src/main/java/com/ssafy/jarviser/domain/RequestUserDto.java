package com.ssafy.jarviser.domain;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Builder
public class RequestUserDto {

    private String userId;

    private String password;

    private String name;

    private String email;

    private String provider;

    private String providedId;

    public User toEntity() {
        return User.builder()
                .userId(userId)
                .password(password)
                .name(name)
                .email(email)
                .build();
    }

    @Builder
    public RequestUserDto(String userId, String password, String name, String email, String provider, String providedId) {
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.email = email;
        this.provider = provider;
        this.providedId = providedId;
    }
}

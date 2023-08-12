package com.ssafy.jarviser.dto;

import lombok.*;

@Builder
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class ParticipantsStaticsDTO {
    long id;
    String name;
    double percentage;
}
